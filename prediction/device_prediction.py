import json
import time
import random
import joblib
import numpy as np
import pandas as pd
from kafka import KafkaProducer, KafkaConsumer
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import accuracy_score, classification_report
from sklearn.model_selection import train_test_split
from sklearn.pipeline import make_pipeline
from sklearn.base import BaseEstimator, TransformerMixin

# 设置随机种子以保证可复现性
random.seed(42)
np.random.seed(42)

# ====================== 1. 数据模拟与特征工程 ======================
class FeatureTransformer(BaseEstimator, TransformerMixin):
	"""自定义特征工程转换器"""
	def fit(self, X, y=None):
		return self

	def transform(self, X):
		# 添加统计特征
		X = X.copy()
		X['temp_vib_ratio'] = X['temperature'] / (X['vibration'] + 1e-5)
		X['noise_temp_product'] = X['noise'] * X['temperature']
		X['vib_rolling_avg'] = X['vibration'].rolling(5, min_periods=1).mean()
		return X

# 生成模拟设备数据集
def generate_device_data(num_samples=1000):
	"""生成模拟设备数据"""
	data = []

	# 生成正常设备数据
	for _ in range(int(num_samples * 0.85)):
		temp = random.uniform(50, 80)  # 正常温度范围
		vib = random.uniform(2, 5)     # 正常振动范围
		noise = random.uniform(30, 60) # 正常噪音范围
		data.append({
			'device_id': f"DEV{random.randint(1000, 9999)}",
			'temperature': temp,
			'vibration': vib,
			'noise': noise,
			'operating_hours': random.randint(0, 10000),
			'failure': 0  # 正常
		})

	# 生成即将故障的设备数据
	for _ in range(int(num_samples * 0.15)):
		# 故障前兆特征
		temp = random.uniform(80, 100)  # 温度升高
		vib = random.uniform(5, 10)     # 振动加剧
		noise = random.uniform(60, 90)  # 噪音增大
		data.append({
			'device_id': f"DEV{random.randint(1000, 9999)}",
			'temperature': temp,
			'vibration': vib,
			'noise': noise,
			'operating_hours': random.randint(8000, 10000),  # 接近设计寿命
			'failure': 1  # 即将故障
		})

	random.shuffle(data)
	return pd.DataFrame(data)

# ====================== 2. 初始模型训练 ======================
def train_initial_model(df):
	"""训练初始预测性维护模型"""
	# 特征和目标分离
	X = df.drop(['device_id', 'failure'], axis=1)
	y = df['failure']

	# 划分训练集和测试集
	X_train, X_test, y_train, y_test = train_test_split(
		X, y, test_size=0.2, stratify=y, random_state=42
	)

	# 创建模型管道
	model = make_pipeline(
		FeatureTransformer(),
		StandardScaler(),
		RandomForestClassifier(
			n_estimators=100,
			class_weight='balanced',
			random_state=42
		)
	)

	# 训练模型
	model.fit(X_train, y_train)

	# 评估模型
	y_pred = model.predict(X_test)
	print("\n===== 初始模型评估 =====")
	print(f"准确率: {accuracy_score(y_test, y_pred):.4f}")
	print(classification_report(y_test, y_pred))

	# 保存初始模型
	joblib.dump(model, 'predictive_maintenance_model.pkl')
	print("初始模型已保存到 predictive_maintenance_model.pkl")

	return model

# ====================== 3. Kafka生产者（模拟实时设备数据） ======================
def kafka_producer(run_time=300):
	"""模拟设备数据发送到Kafka"""
	producer = KafkaProducer(
		bootstrap_servers='localhost:9092',
		value_serializer=lambda v: json.dumps(v).encode('utf-8')
	)

	topic = 'device-monitoring'
	start_time = time.time()
	device_id = 0

	print("\n===== 开始模拟设备数据流 =====")

	while time.time() - start_time < run_time:
		# 生成设备数据
		device_id += 1
		if random.random() < 0.9:  # 90% 正常设备
			temp = random.uniform(50, 80)
			vib = random.uniform(2, 5)
			noise = random.uniform(30, 60)
			status = "normal"
			failure_prob = 0
		else:  # 10% 可能故障设备
			temp = random.uniform(75, 100)
			vib = random.uniform(4, 10)
			noise = random.uniform(50, 90)
			status = "warning"
			failure_prob = 1

		# 创建消息
		message = {
			'device_id': f"DEV{device_id:04d}",
			'timestamp': time.strftime("%Y-%m-%d %H:%M:%S"),
			'temperature': round(temp, 2),
			'vibration': round(vib, 2),
			'noise': round(noise, 2),
			'operating_hours': random.randint(0, 10000),
			'status': status,
			'failure_probability': failure_prob
		}

		# 发送到Kafka
		producer.send(topic, value=message)
		print(f"发送: {message}")
		time.sleep(random.uniform(0.1, 0.5))  # 模拟实时数据流

	producer.flush()
	print("\n===== 数据模拟完成 =====")

# ====================== 4. Kafka消费者（增量学习与预测） ======================
def kafka_consumer():
	"""从Kafka消费数据，进行增量学习和预测"""
	# 加载初始模型
	try:
		model = joblib.load('predictive_maintenance_model.pkl')
		print("\n===== 加载已保存模型 =====")
	except FileNotFoundError:
		print("未找到模型文件，请先训练初始模型")
		return

	# 创建Kafka消费者
	consumer = KafkaConsumer(
		'device-monitoring',
		bootstrap_servers='localhost:9092',
		auto_offset_reset='earliest',
		value_deserializer=lambda m: json.loads(m.decode('utf-8'))
	)

	# 用于增量学习的数据缓冲区
	buffer_size = 50
	X_buffer = []
	y_buffer = []

	# 模型性能跟踪
	update_count = 0
	start_time = time.time()

	print("\n===== 开始监控设备数据流 =====")

	try:
		for message in consumer:
			data = message.value
			print(f"\n收到数据: 设备 {data['device_id']} - 状态: {data['status']}")

			# 准备特征用于预测
			features = pd.DataFrame([{
				'temperature': data['temperature'],
				'vibration': data['vibration'],
				'noise': data['noise'],
				'operating_hours': data['operating_hours']
			}])

			# 使用模型进行预测
			prediction = model.predict_proba(features)[0]
			failure_prob = prediction[1]  # 故障概率

			# 决策阈值
			threshold = 0.7
			alert = "警告：设备可能即将故障！" if failure_prob > threshold else "设备状态正常"

			print(f"预测故障概率: {failure_prob:.4f} - {alert}")

			# 将实际标签添加到缓冲区用于增量学习
			if 'failure_probability' in data:
				# 实际标签（模拟中由生产者提供）
				actual_label = data['failure_probability']
				X_buffer.append(features.values[0])
				y_buffer.append(actual_label)

				# 当缓冲区满时进行增量学习
				if len(X_buffer) >= buffer_size:
					update_count += 1
					print(f"\n===== 执行增量学习更新 (#{update_count}) =====")

					# 转换为DataFrame用于特征工程
					buffer_df = pd.DataFrame(X_buffer, columns=features.columns)

					# 增量学习 - 使用部分拟合（如果支持）
					if hasattr(model, 'partial_fit'):
						# 获取类别列表
						classes = np.unique(y_buffer)

						# 应用特征工程
						buffer_df = model.named_steps['featuretransformer'].transform(buffer_df)

						# 标准化
						scaler = model.named_steps['standardscaler']
						if scaler.n_features_in_ is None:
							# 如果标准化器尚未拟合，先拟合
							scaler.fit(buffer_df)
						X_scaled = scaler.transform(buffer_df)

						# 部分拟合模型
						classifier = model.named_steps['randomforestclassifier']
						classifier.partial_fit(X_scaled, y_buffer, classes=classes)

						# 保存更新后的模型
						joblib.dump(model, 'predictive_maintenance_model.pkl')
						print(f"模型已更新并保存 (#{update_count})")
					else:
						# 如果不支持部分拟合，重新训练整个模型
						# 在实际应用中，应使用支持增量学习的模型
						print("模型不支持增量学习，将重新训练...")
						full_df = generate_device_data(500)  # 生成更多数据用于重新训练
						model = train_initial_model(full_df)

					# 清空缓冲区
					X_buffer = []
					y_buffer = []

	except KeyboardInterrupt:
		print("\n===== 监控停止 =====")
	finally:
		consumer.close()
		elapsed = time.time() - start_time
		print(f"监控运行时间: {elapsed:.2f}秒")
		print(f"执行增量更新次数: {update_count}")

# ====================== 主程序 ======================
if __name__ == "__main__":
	# 步骤1: 生成初始数据集并训练模型
	print("===== 生成初始训练数据 =====")
	initial_data = generate_device_data(1000)
	print(f"生成数据: {len(initial_data)}条记录 (故障率: {initial_data['failure'].mean():.2%})")

	print("\n===== 训练初始模型 =====")
	model = train_initial_model(initial_data)

	# 步骤2: 启动Kafka生产者（在后台线程中）
	import threading
	producer_thread = threading.Thread(target=kafka_producer, args=(180,))  # 运行3分钟
	producer_thread.start()

	# 步骤3: 启动Kafka消费者
	time.sleep(2)  # 等待生产者启动
	kafka_consumer()

	# 等待生产者线程结束
	producer_thread.join()
	print("\n===== 系统运行完成 =====")
