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

"""
音频工具模块
"""

import numpy as np
import librosa

# 音频归一化
def normalize_audio(audio: np.ndarray) -> np.ndarray:
	max_val = np.max(np.abs(audio))
	if max_val > 0:
		return audio / max_val
	return audio

def load_audio(file_path: str, sr: int = 16000) -> np.ndarray:
	"""
	加载音频文件

	Args:
		file_path: 音频文件路径
		sr: 目标采样率

	Returns:
		音频数据 (numpy array)
	"""
	audio, _ = librosa.load(file_path, sr=sr, mono=True)
	return audio


def preprocess_audio(audio: np.ndarray) -> np.ndarray:
	"""
	音频预处理流程

	Args:
	    audio: 原始音频

	Returns:
	    预处理后的音频
	"""
	# 1. 去除静音
	audio = remove_silence(audio)

	# 2. 归一化
	audio = normalize_audio(audio)

	# 3. 填充/裁剪到固定长度
	audio = pad_or_trim(audio)
	return audio


def remove_silence(audio: np.ndarray,
				   top_db: int = 20,
				   frame_length: int = 2048,
				   hop_length: int = 512) -> np.ndarray:
	"""
	移除音频首尾静音

	Args:
		audio: 输入音频
		top_db: 静音阈值 (dB)
		frame_length: 帧长
		hop_length: 帧移

	Returns:
		去除静音后的音频
	"""
	trimmed, _ = librosa.effects.trim(
		audio,
		top_db=top_db,
		frame_length=frame_length,
		hop_length=hop_length
	)
	return trimmed


def pad_or_trim(audio: np.ndarray, target_length: int = 32000) -> np.ndarray:
	"""
	填充或裁剪音频到目标长度

	Args:
		audio: 输入音频
		target_length: 目标采样点数

	Returns:
		固定长度的音频
	"""
	if len(audio) > target_length:
		# 居中裁剪
		start = (len(audio) - target_length) // 2
		return audio[start:start + target_length]
	elif len(audio) < target_length:
		# 零填充
		padding = target_length - len(audio)
		pad_left = padding // 2
		pad_right = padding - pad_left
		return np.pad(audio, (pad_left, pad_right), mode='constant')
	return audio
