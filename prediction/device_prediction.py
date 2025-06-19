import json
import time
import random
import numpy as np
import pandas as pd
import tensorflow as tf
from tensorflow.keras.models import Model, load_model
from tensorflow.keras.layers import Input, Conv1D, Dense, Activation, Add, Dropout, BatchNormalization
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.callbacks import EarlyStopping
from kafka import KafkaProducer, KafkaConsumer
from sklearn.preprocessing import StandardScaler
from collections import deque
import threading

# 设置随机种子以保证可复现性
random.seed(42)
np.random.seed(42)
tf.random.set_seed(42)

# ====================== 1. 数据模拟与特征工程 ======================
class FeatureEngineer:
	"""特征工程类，用于实时特征转换"""
	def __init__(self):
		self.scaler = StandardScaler()
		self.is_fitted = False

	def fit(self, X):
		"""拟合特征转换器"""
		self.scaler.fit(X)
		self.is_fitted = True

	def transform(self, X):
		"""转换特征"""
		if not self.is_fitted:
			raise RuntimeError("Feature engineer not fitted yet")

		# 基本特征
		X = X.copy()

		# 添加统计特征
		if len(X) > 1:
			X['temp_diff'] = X['temperature'].diff().fillna(0)
			X['vib_rolling'] = X['vibration'].rolling(3, min_periods=1).mean()
			X['noise_rolling'] = X['noise'].rolling(3, min_periods=1).mean()
			X['temp_vib_ratio'] = X['temperature'] / (X['vibration'] + 1e-5)
			X['noise_temp_product'] = X['noise'] * X['temperature']

		# 标准化特征
		features = ['temperature', 'vibration', 'noise', 'temp_diff',
					'vib_rolling', 'noise_rolling', 'temp_vib_ratio', 'noise_temp_product']
		available_features = [f for f in features if f in X.columns]

		X_scaled = self.scaler.transform(X[available_features])
		return X_scaled, available_features

# 生成模拟设备数据
def generate_device_data(num_samples=1000, seq_length=60):
	"""生成带时间序列特征的模拟设备数据"""
	data = []

	# 生成正常设备序列
	for _ in range(int(num_samples * 0.85)):
		base_temp = random.uniform(50, 70)
		base_vib = random.uniform(2, 4)
		base_noise = random.uniform(30, 50)

		sequence = []
		for i in range(seq_length):
			# 正常波动
			temp = base_temp + random.uniform(-3, 3)
			vib = base_vib + random.uniform(-0.5, 0.5)
			noise = base_noise + random.uniform(-5, 5)

			sequence.append({
				'temperature': temp,
				'vibration': vib,
				'noise': noise,
				'failure': 0
			})
		data.append(sequence)

	# 生成即将故障的设备序列
	for _ in range(int(num_samples * 0.15)):
		base_temp = random.uniform(50, 70)
		base_vib = random.uniform(2, 4)
		base_noise = random.uniform(30, 50)

		sequence = []
		failure_start = random.randint(seq_length - 20, seq_length - 10)

		for i in range(seq_length):
			# 正常波动
			temp = base_temp + random.uniform(-3, 3)
			vib = base_vib + random.uniform(-0.5, 0.5)
			noise = base_noise + random.uniform(-5, 5)

			# 在故障开始点之后添加异常
			if i >= failure_start:
				# 逐渐增加异常程度
				severity = (i - failure_start) / 10
				temp += random.uniform(5, 20) * severity
				vib += random.uniform(1, 4) * severity
				noise += random.uniform(10, 30) * severity

			# 标记故障（在故障开始点之后）
			failure = 1 if i >= failure_start else 0

			sequence.append({
				'temperature': temp,
				'vibration': vib,
				'noise': noise,
				'failure': failure
			})
		data.append(sequence)

	random.shuffle(data)
	return data

# ====================== 2. TCN模型构建 ======================
def residual_block(x, filters, kernel_size, dilation_rate):
	"""构建TCN残差块"""
	# 残差连接
	res = x

	# 因果卷积（不访问未来数据）
	x = Conv1D(filters, kernel_size, padding='causal',
			   dilation_rate=dilation_rate)(x)
	x = BatchNormalization()(x)
	x = Activation('relu')(x)
	x = Dropout(0.2)(x)

	# 1x1卷积确保残差维度匹配
	if res.shape[-1] != filters:
		res = Conv1D(filters, 1, padding='same')(res)

	return Add()([res, x])

def build_tcn_model(input_shape, num_classes=1):
	"""构建时间卷积网络模型"""
	inputs = Input(shape=input_shape)

	# 初始卷积层
	x = Conv1D(64, 3, padding='causal')(inputs)
	x = BatchNormalization()(x)
	x = Activation('relu')(x)

	# 堆叠残差块（扩张率指数增长）
	dilation_rates = [1, 2, 4, 8, 16]
	for dilation in dilation_rates:
		x = residual_block(x, filters=64, kernel_size=3, dilation_rate=dilation)

	# 输出层
	x = Conv1D(32, 1, activation='relu')(x)
	outputs = Conv1D(num_classes, 1, activation='sigmoid')(x)

	model = Model(inputs, outputs)
	model.compile(optimizer=Adam(learning_rate=0.001),
				  loss='binary_crossentropy',
				  metrics=['accuracy', tf.keras.metrics.Precision(), tf.keras.metrics.Recall()])

	return model

# ====================== 3. 增量学习处理 ======================
class IncrementalTCN:
	"""支持增量学习的TCN模型管理类"""
	def __init__(self, seq_length, feature_count, model_path='tcn_model.h5'):
		self.seq_length = seq_length
		self.feature_count = feature_count
		self.model_path = model_path
		self.model = None
		self.feature_engineer = FeatureEngineer()
		self.data_buffer = deque(maxlen=1000)  # 存储(X, y)对

	def load_or_create_model(self):
		"""加载或创建模型"""
		try:
			self.model = load_model(self.model_path)
			print(f"Loaded existing model from {self.model_path}")
		except:
			print("Creating new model")
			self.model = build_tcn_model((self.seq_length, self.feature_count))
			self.model.save(self.model_path)

	def initial_training(self, train_data):
		"""初始模型训练"""
		# 准备训练数据
		X_train, y_train = [], []
		for sequence in train_data:
			# 转换为DataFrame用于特征工程
			df = pd.DataFrame(sequence)
			X_seq, _ = self.feature_engineer.transform(df)
			y_seq = df['failure'].values

			X_train.append(X_seq)
			y_train.append(y_seq)

		X_train = np.array(X_train)
		y_train = np.array(y_train)

		# 拟合特征转换器
		all_features = np.vstack([x.reshape(-1, x.shape[-1]) for x in X_train])
		self.feature_engineer.fit(all_features)

		# 重新转换数据确保一致性
		for i, sequence in enumerate(train_data):
			df = pd.DataFrame(sequence)
			X_seq, _ = self.feature_engineer.transform(df)
			X_train[i] = X_seq

		print(f"Training data shape: {X_train.shape}")

		# 训练模型
		early_stop = EarlyStopping(monitor='val_loss', patience=3, restore_best_weights=True)
		self.model.fit(
			X_train, y_train,
			epochs=20,
			batch_size=32,
			validation_split=0.2,
			callbacks=[early_stop]
		)

		# 保存模型
		self.model.save(self.model_path)
		print(f"Initial training completed. Model saved to {self.model_path}")

	def incremental_update(self, X_new, y_new):
		"""增量更新模型"""
		if not self.model:
			raise RuntimeError("Model not initialized")

		# 将新数据添加到缓冲区
		for i in range(len(X_new)):
			self.data_buffer.append((X_new[i], y_new[i]))

		# 当缓冲区有足够数据时进行更新
		if len(self.data_buffer) >= 64:  # 当有64个序列时更新
			print(f"Incremental update with {len(self.data_buffer)} new sequences")

			# 准备增量训练数据
			X_inc, y_inc = [], []
			while self.data_buffer:
				X_seq, y_seq = self.data_buffer.popleft()
				X_inc.append(X_seq)
				y_inc.append(y_seq)

			X_inc = np.array(X_inc)
			y_inc = np.array(y_inc)

			# 增量训练
			self.model.fit(
				X_inc, y_inc,
				epochs=5,
				batch_size=16,
				shuffle=True
			)

			# 保存更新后的模型
			self.model.save(self.model_path)
			print(f"Incremental update completed. Model saved to {self.model_path}")

	def predict(self, sequence):
		"""预测序列故障概率"""
		if not self.model:
			raise RuntimeError("Model not initialized")

		# 特征工程
		df = pd.DataFrame(sequence)
		X_seq, features = self.feature_engineer.transform(df)

		# 确保序列长度正确
		if len(X_seq) < self.seq_length:
			# 填充序列
			padding = np.zeros((self.seq_length - len(X_seq), len(features)))
			X_seq = np.vstack([padding, X_seq])
		elif len(X_seq) > self.seq_length:
			# 截断序列
			X_seq = X_seq[-self.seq_length:]

		# 预测
		X_seq = np.expand_dims(X_seq, axis=0)  # 添加批次维度
		predictions = self.model.predict(X_seq)[0]

		# 返回最后时间步的预测（当前状态）
		return predictions[-1][0]

# ====================== 4. Kafka集成 ======================
class KafkaDeviceMonitor:
	"""Kafka设备监控类"""
	def __init__(self, tcn_model, seq_length=60):
		self.tcn_model = tcn_model
		self.seq_length = seq_length
		self.device_sequences = {}  # 存储每个设备的序列数据
		self.prediction_history = {}

	def start_producer(self, run_time=300, devices=10):
		"""启动Kafka生产者（模拟设备数据）"""
		def producer_task():
			producer = KafkaProducer(
				bootstrap_servers='localhost:9092',
				value_serializer=lambda v: json.dumps(v).encode('utf-8')
			)

			topic = 'device-monitoring'
			start_time = time.time()
			device_counter = 1

			print("\n===== 开始模拟设备数据流 =====")

			while time.time() - start_time < run_time:
				# 选择设备
				device_id = f"DEV{device_counter:04d}"
				device_counter = device_counter % devices + 1

				# 生成设备状态
				if random.random() < 0.95:  # 95% 正常设备
					temp = random.uniform(50, 80)
					vib = random.uniform(2, 5)
					noise = random.uniform(30, 60)
					status = "normal"
					failure = 0
				else:  # 5% 可能故障设备
					temp = random.uniform(70, 100)
					vib = random.uniform(4, 10)
					noise = random.uniform(50, 90)
					status = "warning"
					failure = 1

				# 创建消息
				message = {
					'device_id': device_id,
					'timestamp': time.strftime("%Y-%m-%d %H:%M:%S"),
					'temperature': round(temp, 2),
					'vibration': round(vib, 2),
					'noise': round(noise, 2),
					'status': status,
					'failure': failure
				}

				# 发送到Kafka
				producer.send(topic, value=message)
				time.sleep(random.uniform(0.1, 0.3))  # 模拟实时数据流

			producer.flush()
			print("\n===== 数据模拟完成 =====")

		# 在后台线程中启动生产者
		self.producer_thread = threading.Thread(target=producer_task)
		self.producer_thread.start()

	def start_consumer(self):
		"""启动Kafka消费者（实时监控和增量学习）"""
		consumer = KafkaConsumer(
			'device-monitoring',
			bootstrap_servers='localhost:9092',
			auto_offset_reset='earliest',
			value_deserializer=lambda m: json.loads(m.decode('utf-8')),
			consumer_timeout_ms=10000
		)

		print("\n===== 开始监控设备数据流 =====")

		try:
			for message in consumer:
				data = message.value
				device_id = data['device_id']

				# 初始化设备序列
				if device_id not in self.device_sequences:
					self.device_sequences[device_id] = []
					self.prediction_history[device_id] = []

				# 添加新数据点
				self.device_sequences[device_id].append({
					'temperature': data['temperature'],
					'vibration': data['vibration'],
					'noise': data['noise'],
					'failure': data['failure']
				})

				# 保持序列长度
				if len(self.device_sequences[device_id]) > self.seq_length * 2:
					self.device_sequences[device_id] = self.device_sequences[device_id][-self.seq_length * 2:]

				# 当有足够数据时进行预测
				if len(self.device_sequences[device_id]) >= self.seq_length:
					# 获取最近seq_length个数据点
					sequence = self.device_sequences[device_id][-self.seq_length:]

					# 进行预测
					failure_prob = self.tcn_model.predict(sequence)
					self.prediction_history[device_id].append(failure_prob)

					# 获取实际故障状态（序列中最后一个数据点）
					actual_failure = sequence[-1]['failure']

					# 生成警报
					alert = ""
					if failure_prob > 0.8:
						alert = "CRITICAL ALERT: 设备可能即将故障！"
					elif failure_prob > 0.6:
						alert = "WARNING: 设备状态异常"

					print(f"\n设备 {device_id} | 预测故障概率: {failure_prob:.4f} | 实际状态: {'故障' if actual_failure else '正常'} | {alert}")

					# 准备增量学习数据
					# 我们使用整个序列作为训练样本，标签是序列中是否有故障
					has_failure = any(point['failure'] for point in sequence)

					# 转换为训练格式
					df = pd.DataFrame(sequence)
					X_seq, _ = self.tcn_model.feature_engineer.transform(df)
					y_seq = [1 if has_failure else 0] * len(sequence)

					# 添加到增量学习队列
					self.tcn_model.incremental_update([X_seq], [y_seq])

		except KeyboardInterrupt:
			print("\n===== 监控停止 =====")
		finally:
			consumer.close()

# ====================== 5. 主程序 ======================
def main():
	# 配置参数
	SEQ_LENGTH = 60  # 时间序列长度
	NUM_TRAIN_SEQUENCES = 500  # 初始训练序列数量
	FEATURE_COUNT = 8  # 特征数量（由特征工程决定）

	# 步骤1: 创建增量TCN模型
	tcn_model = IncrementalTCN(
		seq_length=SEQ_LENGTH,
		feature_count=FEATURE_COUNT,
		model_path='predictive_maintenance_tcn.h5'
	)
	tcn_model.load_or_create_model()

	# 步骤2: 生成初始训练数据并进行初始训练
	print("===== 生成初始训练数据 =====")
	train_data = generate_device_data(NUM_TRAIN_SEQUENCES, SEQ_LENGTH)
	print(f"生成 {len(train_data)} 个训练序列")

	print("\n===== 进行初始模型训练 =====")
	tcn_model.initial_training(train_data)

	# 步骤3: 启动Kafka监控系统
	monitor = KafkaDeviceMonitor(tcn_model, SEQ_LENGTH)

	# 启动生产者（模拟设备数据）
	monitor.start_producer(run_time=600, devices=20)  # 运行10分钟，20台设备

	# 等待生产者启动
	time.sleep(2)

	# 启动消费者（实时监控和增量学习）
	monitor.start_consumer()

if __name__ == "__main__":
	main()
