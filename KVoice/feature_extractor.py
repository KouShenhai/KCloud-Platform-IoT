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
特征提取模块
"""

import numpy as np
import librosa

def extract_mfcc(audio: np.ndarray,
                 sr: int = 16000,
                 n_mfcc: int = 40,
                 n_fft: int = 512,
                 hop_length: int = 160,
                 win_length: int = 400) -> np.ndarray:
    """
    提取MFCC特征

    Args:
        audio: 输入音频
        sr: 采样率
        n_mfcc: MFCC系数数量
        n_fft: FFT窗口大小
        hop_length: 帧移
        win_length: 帧长

    Returns:
        MFCC特征矩阵 (n_mfcc, num_frames)
    """
    mfcc = librosa.feature.mfcc(
        y=audio,
        sr=sr,
        n_mfcc=n_mfcc,
        n_fft=n_fft,
        hop_length=hop_length,
        win_length=win_length
    )
    return mfcc


def extract_mel_spectrogram(audio: np.ndarray,
                             sr: int = 16000,
                             n_mels: int = 40,
                             n_fft: int = 512,
                             hop_length: int = 160) -> np.ndarray:
    """
    提取Mel频谱图

    Args:
        audio: 输入音频
        sr: 采样率
        n_mels: Mel滤波器数量
        n_fft: FFT窗口大小
        hop_length: 帧移

    Returns:
        Mel频谱图 (n_mels, num_frames)
    """
    mel_spec = librosa.feature.melspectrogram(
        y=audio,
        sr=sr,
        n_mels=n_mels,
        n_fft=n_fft,
        hop_length=hop_length
    )
    # 转换为对数刻度
    mel_spec_db = librosa.power_to_db(mel_spec, ref=np.max)
    return mel_spec_db


def normalize_features(features: np.ndarray) -> np.ndarray:
    """
    特征归一化 (零均值单位方差)

    Args:
        features: 输入特征

    Returns:
        归一化后的特征
    """
    mean = np.mean(features)
    std = np.std(features)
    if std > 0:
        return (features - mean) / std
    return features - mean


def pad_features(features: np.ndarray,
                 target_frames: int = 201) -> np.ndarray:
    """
    填充或裁剪特征到目标帧数

    Args:
        features: 输入特征 (n_features, num_frames)
        target_frames: 目标帧数

    Returns:
        固定尺寸的特征
    """
    current_frames = features.shape[1]

    if current_frames > target_frames:
        # 居中裁剪
        start = (current_frames - target_frames) // 2
        return features[:, start:start + target_frames]
    elif current_frames < target_frames:
        # 右侧零填充
        padding = target_frames - current_frames
        return np.pad(features, ((0, 0), (0, padding)), mode='constant')
    return features


def extract_features(audio: np.ndarray) -> np.ndarray:
    """
    完整特征提取流程

    Args:
        audio: 输入音频

    Returns:
        特征矩阵 (num_frames, n_mfcc, 1) - 适用于CNN输入
    """
    # 1. 提取MFCC
    mfcc = extract_mfcc(audio)

    # 2. 填充到固定长度
    mfcc = pad_features(mfcc, 201)

    # 3. 归一化
    mfcc = normalize_features(mfcc)

    # 4. 转置并添加通道维度 (num_frames, n_mfcc, 1)
    mfcc = mfcc.T[..., np.newaxis]

    return mfcc.astype(np.float32)
