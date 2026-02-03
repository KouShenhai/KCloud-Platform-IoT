# /*
#  * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
#  *
#  * Licensed under the Apache License, Version 2.0 (the "License");
#  * you may not use this file except in compliance with the License.
#  * You may obtain a copy of the License at
#  *
#  *   http://www.apache.org/licenses/LICENSE-2.0
#  *
#  * Unless required by applicable law or agreed to in writing, software
#  * distributed under the License is distributed on an "AS IS" BASIS,
#  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  * See the License for the specific language governing permissions and
#  * limitations under the License.
#  *
#  */

from glob import glob

import numpy as np
from sklearn.model_selection import train_test_split
from tqdm import tqdm
from audio_utils import load_audio, preprocess_audio
import data_augmentation
from feature_extractor import extract_features
import os
from tensorflow import keras
from model import compile_model, create_wake_word_model
def train_model(X: np.ndarray,
				y: np.ndarray,
				model: keras.Model = None,
				epochs: int = 30,
				batch_size: int = 15,
				validation_split: float = 0.2) -> tuple:
	"""
	训练模型

	Args:
		X: 特征数据
		y: 标签
		model: Keras模型 (None则创建新模型)
		epochs: 训练轮数
		batch_size: 批大小
		validation_split: 验证集比例

	Returns:
		(model, history) 训练后的模型和历史
	"""
	# 划分数据集
	X_train, X_val, y_train, y_val = train_test_split(
		X, y, test_size=validation_split, random_state=42, stratify=y
	)

	print(f"\n训练集: {len(y_train)} 样本")
	print(f"验证集: {len(y_val)} 样本")

	# 创建模型
	if model is None:
		model = create_wake_word_model()
	model = compile_model(model)

	# 回调函数
	callbacks = [
		# 早停
		keras.callbacks.EarlyStopping(
			monitor='val_accuracy',
			patience=10,
			restore_best_weights=True,
			verbose=1
		),
		# 学习率衰减
		keras.callbacks.ReduceLROnPlateau(
			monitor='val_loss',
			factor=0.5,
			patience=5,
			min_lr=1e-6,
			verbose=1
		),
		# 模型检查点
		keras.callbacks.ModelCheckpoint(
			os.path.join("models", 'best_model.keras'),
			monitor='val_accuracy',
			save_best_only=True,
			verbose=1
		)
	]

	# 训练
	print(f"\n开始训练 (Epochs: {epochs})...")
	history = model.fit(
		X_train, y_train,
		validation_data=(X_val, y_val),
		epochs=epochs,
		batch_size=batch_size,
		callbacks=callbacks,
		verbose=1
	)

	# 评估
	print("\n最终评估:")
	loss, accuracy = model.evaluate(X_val, y_val, verbose=0)
	print(f"  验证损失: {loss:.4f}")
	print(f"  验证准确率: {accuracy:.4f}")

	return model, history


if __name__ == '__main__':
	# 加载唤醒词样本（正样本）
	wake_paths = glob(os.path.join('voice', '*.wav'))
	print(f"加载唤醒词样本: {len(wake_paths)} 个文件")
	X = []
	y = []
	for file_Path in tqdm(wake_paths,  desc="处理唤醒词"):
		_audio = load_audio(file_Path)
		_audio = preprocess_audio(_audio)

		# 原始样本
		_features = extract_features(_audio)
		X.append(_features)
		y.append(1)

		# 数据增强
		_augmented_samples = data_augmentation.generate_augmented_samples(_audio)
		for aug_audio in _augmented_samples:
			aug_audio = preprocess_audio(aug_audio)
			aug_features = extract_features(aug_audio)
			X.append(aug_features)
			y.append(1)

	# 加载非唤醒词样本（负样本）
	negative_paths = glob(os.path.join('negative', '*.wav'))
	print(f"加载非唤醒词样本: {len(negative_paths)} 个文件")
	for file_Path in tqdm(negative_paths, desc="处理非唤醒词"):
		_audio = load_audio(file_Path)
		_audio = preprocess_audio(_audio)

		# 原始样本
		_features = extract_features(_audio)
		X.append(_features)
		y.append(0)

		# 数据增强（可选，保持正负样本平衡）
		_augmented_samples = data_augmentation.generate_augmented_samples(_audio)
		for aug_audio in _augmented_samples:
			aug_audio = preprocess_audio(aug_audio)
			aug_features = extract_features(aug_audio)
			X.append(aug_features)
			y.append(0)

	# 检查是否有足够的样本
	unique_classes = set(y)
	if len(unique_classes) < 2:
		print("错误: 训练需要正样本和负样本！")
		print(f"当前只有类别: {unique_classes}")
		print("请在 'negative' 目录中添加非唤醒词音频文件")
		exit(1)

	X = np.array(X)
	y = np.array(y)
	print(f"\n总样本数: {len(y)}, 正样本: {sum(y)}, 负样本: {len(y) - sum(y)}")
	model = create_wake_word_model()
	model.summary()
	model, _history = train_model(X, y, model=model, epochs=30, batch_size=15)
	os.makedirs('models', exist_ok=True)
	model_path = os.path.join('models', 'wake_word_model.keras')
	model.save(model_path)
