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
数据增强模块
"""

import numpy as np
import librosa
from typing import List

def time_stretch(audio: np.ndarray, rate: float = None) -> np.ndarray:
    """
    时间拉伸 (不改变音高)

    Args:
        audio: 输入音频
        rate: 拉伸率 (>1加速, <1减速), None则随机

    Returns:
        拉伸后的音频
    """
    if rate is None:
        rate = np.random.uniform(*(0.8, 1.2))

    return librosa.effects.time_stretch(audio, rate=rate)


def pitch_shift(audio: np.ndarray,
                n_steps: float = None,
                sr: int = 16000) -> np.ndarray:
    """
    音高变换

    Args:
        audio: 输入音频
        n_steps: 变换半音数, None则随机
        sr: 采样率

    Returns:
        变换后的音频
    """
    if n_steps is None:
        n_steps = np.random.uniform(*(-2, 2))

    return librosa.effects.pitch_shift(audio, sr=sr, n_steps=n_steps)


def add_noise(audio: np.ndarray, noise_factor: float = None) -> np.ndarray:
    """
    添加高斯白噪声

    Args:
        audio: 输入音频
        noise_factor: 噪声强度, None则使用默认值

    Returns:
        添加噪声后的音频
    """
    if noise_factor is None:
        noise_factor = 0.005 * np.random.uniform(0.5, 1.5)

    noise = np.random.randn(len(audio))
    return audio + noise_factor * noise


def time_shift(audio: np.ndarray, shift_max: float = 0.1) -> np.ndarray:
    """
    时间偏移

    Args:
        audio: 输入音频
        shift_max: 最大偏移比例

    Returns:
        偏移后的音频
    """
    shift = int(len(audio) * np.random.uniform(-shift_max, shift_max))
    return np.roll(audio, shift)


def change_volume(audio: np.ndarray,
                  gain_range: tuple = (0.7, 1.3)) -> np.ndarray:
    """
    音量变化

    Args:
        audio: 输入音频
        gain_range: 增益范围

    Returns:
        变化后的音频
    """
    gain = np.random.uniform(*gain_range)
    return audio * gain


def augment_audio(audio: np.ndarray) -> np.ndarray:
    """
    随机应用一种或多种数据增强

    Args:
        audio: 输入音频

    Returns:
        增强后的音频
    """
    augmented = audio.copy()

    # 随机选择增强方法
    augmentations = [
        lambda x: time_stretch(x),
        lambda x: pitch_shift(x),
        lambda x: add_noise(x),
        lambda x: time_shift(x),
        lambda x: change_volume(x)
    ]

    # 随机应用1-3种增强
    num_augments = np.random.randint(1, 4)
    selected = np.random.choice(len(augmentations), num_augments, replace=False)

    for idx in selected:
        augmented = augmentations[idx](augmented)

    return augmented


def generate_augmented_samples(audio: np.ndarray,
                                num_samples: int = 5) -> List[np.ndarray]:
    """
    生成多个增强样本

    Args:
        audio: 原始音频
        num_samples: 生成样本数量

    Returns:
        增强样本列表
    """
    samples = []
    for _ in range(num_samples):
        augmented = augment_audio(audio)
        samples.append(augmented)
    return samples
