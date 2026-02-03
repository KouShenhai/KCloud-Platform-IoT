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
唤醒词模板匹配模块 (优化版)

改进：
1. 使用更多 MFCC 特征 (20维 + delta + delta-delta)
2. 添加语音活动检测 (VAD)
3. 优化 DTW 算法
4. 更好的归一化
"""

import os
import numpy as np
import librosa
from typing import List, Tuple
from scipy.spatial.distance import cdist
import json


# 音频参数
SAMPLE_RATE = 16000
N_MFCC = 20  # 增加到 20 维
N_FFT = 512
HOP_LENGTH = 160
WIN_LENGTH = 400


def voice_activity_detection(audio: np.ndarray, 
                              sr: int = SAMPLE_RATE,
                              threshold: float = 0.02) -> np.ndarray:
    """
    简单的语音活动检测，去除静音部分
    
    Args:
        audio: 音频数据
        sr: 采样率
        threshold: 能量阈值
        
    Returns:
        去除静音后的音频
    """
    # 计算短时能量
    frame_length = int(0.025 * sr)  # 25ms
    hop = int(0.010 * sr)  # 10ms
    
    energy = []
    for i in range(0, len(audio) - frame_length, hop):
        frame = audio[i:i + frame_length]
        energy.append(np.sqrt(np.mean(frame ** 2)))
    
    energy = np.array(energy)
    
    if len(energy) == 0:
        return audio
    
    # 动态阈值
    dynamic_threshold = max(threshold, np.mean(energy) * 0.5)
    
    # 找到语音段
    speech_frames = energy > dynamic_threshold
    
    if not np.any(speech_frames):
        return audio
    
    # 扩展边界
    speech_indices = np.where(speech_frames)[0]
    start_frame = max(0, speech_indices[0] - 5)
    end_frame = min(len(energy), speech_indices[-1] + 5)
    
    start_sample = start_frame * hop
    end_sample = min(len(audio), (end_frame + 1) * hop + frame_length)
    
    return audio[start_sample:end_sample]


def extract_mfcc(audio: np.ndarray, sr: int = SAMPLE_RATE) -> np.ndarray:
    """
    提取 MFCC 特征 (优化版)
    
    使用 20 维 MFCC + delta + delta-delta = 60 维特征
    
    Args:
        audio: 音频数据
        sr: 采样率
        
    Returns:
        MFCC 特征 (n_frames, 60)
    """
    # 预加重
    audio = librosa.effects.preemphasis(audio, coef=0.97)
    
    # 语音活动检测
    audio = voice_activity_detection(audio, sr)
    
    # 确保音频长度足够
    min_length = int(0.1 * sr)  # 至少 100ms
    if len(audio) < min_length:
        audio = np.pad(audio, (0, min_length - len(audio)))
    
    # 提取 MFCC
    mfcc = librosa.feature.mfcc(
        y=audio,
        sr=sr,
        n_mfcc=N_MFCC,
        n_fft=N_FFT,
        hop_length=HOP_LENGTH,
        win_length=WIN_LENGTH,
        window='hamming'
    )
    
    # 一阶差分
    mfcc_delta = librosa.feature.delta(mfcc, order=1)
    
    # 二阶差分
    mfcc_delta2 = librosa.feature.delta(mfcc, order=2)
    
    # 合并
    features = np.vstack([mfcc, mfcc_delta, mfcc_delta2])
    
    # CMVN 归一化 (倒谱均值方差归一化)
    features = (features - np.mean(features, axis=1, keepdims=True)) / (np.std(features, axis=1, keepdims=True) + 1e-8)
    
    return features.T.astype(np.float64)  # (n_frames, 60)


def dtw_distance(template: np.ndarray, query: np.ndarray) -> float:
    """
    计算 DTW 距离 (优化版)
    
    使用 Sakoe-Chiba 带约束
    """
    n, m = len(template), len(query)
    
    if n == 0 or m == 0:
        return float('inf')
    
    # Sakoe-Chiba 带宽
    window = max(10, abs(n - m) + 5)
    
    # 计算代价矩阵
    cost = cdist(template, query, metric='euclidean')
    
    # DTW 动态规划
    dtw = np.full((n + 1, m + 1), np.inf)
    dtw[0, 0] = 0
    
    for i in range(1, n + 1):
        j_start = max(1, i - window)
        j_end = min(m + 1, i + window + 1)
        for j in range(j_start, j_end):
            dtw[i, j] = cost[i-1, j-1] + min(
                dtw[i-1, j],      # 插入
                dtw[i, j-1],      # 删除
                dtw[i-1, j-1]     # 匹配
            )
    
    # 归一化：除以路径长度
    distance = dtw[n, m] / (n + m)
    return distance


class WakeWordMatcher:
    """
    唤醒词模板匹配器 (优化版)
    """
    
    def __init__(self, templates_dir: str = "wake_templates"):
        """
        初始化匹配器
        """
        self.templates_dir = templates_dir
        self.templates: List[np.ndarray] = []
        self.threshold = 1.5  # 降低默认阈值，更严格
        
        os.makedirs(templates_dir, exist_ok=True)
        self._load_templates()
    
    def _load_templates(self):
        """加载已保存的模板"""
        templates_file = os.path.join(self.templates_dir, "templates.npz")
        config_file = os.path.join(self.templates_dir, "config.json")
        
        if os.path.exists(templates_file):
            try:
                data = np.load(templates_file, allow_pickle=True)
                loaded = data['templates']
                self.templates = []
                for t in loaded:
                    arr = np.asarray(t, dtype=np.float64)
                    if arr.ndim == 2:
                        self.templates.append(arr)
                print(f"[OK] Loaded {len(self.templates)} templates")
            except Exception as e:
                print(f"[WARN] Load failed: {e}")
                self.templates = []
        
        if os.path.exists(config_file):
            with open(config_file, 'r') as f:
                config = json.load(f)
                self.threshold = config.get('threshold', 1.5)
    
    def _save_templates(self):
        """保存模板"""
        templates_file = os.path.join(self.templates_dir, "templates.npz")
        config_file = os.path.join(self.templates_dir, "config.json")
        
        np.savez(templates_file, templates=np.array(self.templates, dtype=object))
        
        with open(config_file, 'w') as f:
            json.dump({'threshold': self.threshold}, f)
    
    def clear_templates(self):
        """清除所有模板"""
        self.templates = []
        templates_file = os.path.join(self.templates_dir, "templates.npz")
        if os.path.exists(templates_file):
            os.remove(templates_file)
        print("[OK] Templates cleared")
    
    def enroll_template(self, audio: np.ndarray, clear_existing: bool = False):
        """
        注册唤醒词模板
        """
        if clear_existing:
            self.templates = []
        
        # 提取 MFCC 特征
        mfcc = extract_mfcc(audio)
        
        if mfcc.shape[0] < 5:
            print("[WARN] Audio too short, skipping")
            return
        
        self.templates.append(mfcc)
        self._save_templates()
        
        print(f"[OK] Template enrolled ({mfcc.shape[0]} frames). Total: {len(self.templates)}")
    
    def match(self, audio: np.ndarray) -> Tuple[bool, float]:
        """
        匹配唤醒词
        """
        if len(self.templates) == 0:
            return False, float('inf')
        
        # 提取特征
        query_mfcc = extract_mfcc(audio)
        
        if query_mfcc.shape[0] < 5:
            return False, float('inf')
        
        # 与所有模板比较，取最小距离
        min_distance = float('inf')
        for template in self.templates:
            try:
                distance = dtw_distance(template, query_mfcc)
                min_distance = min(min_distance, distance)
            except Exception as e:
                continue
        
        is_match = min_distance < self.threshold
        return is_match, min_distance
    
    def set_threshold(self, threshold: float):
        """设置匹配阈值"""
        self.threshold = threshold
        self._save_templates()
        print(f"[OK] Threshold: {threshold}")
    
    def calibrate_threshold(self, 
                           positive_audios: List[np.ndarray],
                           negative_audios: List[np.ndarray]) -> float:
        """
        自动校准阈值
        """
        if len(self.templates) == 0:
            print("[ERROR] No templates!")
            return 1.5
        
        # 计算正样本距离
        pos_dists = []
        for audio in positive_audios:
            _, dist = self.match(audio)
            if dist < float('inf'):
                pos_dists.append(dist)
        
        # 计算负样本距离
        neg_dists = []
        for audio in negative_audios:
            _, dist = self.match(audio)
            if dist < float('inf'):
                neg_dists.append(dist)
        
        if not pos_dists:
            print("[ERROR] No valid positive samples!")
            return 1.5
        
        print(f"  Positive: min={min(pos_dists):.2f}, max={max(pos_dists):.2f}, avg={np.mean(pos_dists):.2f}")
        if neg_dists:
            print(f"  Negative: min={min(neg_dists):.2f}, max={max(neg_dists):.2f}, avg={np.mean(neg_dists):.2f}")
        
        # 设置阈值：正样本最大值 + 20% 余量
        max_pos = max(pos_dists)
        min_neg = min(neg_dists) if neg_dists else float('inf')
        
        if max_pos < min_neg:
            # 可分，取中间值
            threshold = (max_pos + min_neg) / 2
        else:
            # 有重叠，用正样本最大值 + 余量
            threshold = max_pos * 1.2
        
        self.threshold = threshold
        self._save_templates()
        print(f"  [OK] Threshold: {threshold:.2f}")
        
        return threshold
    
    def get_info(self) -> dict:
        """获取匹配器信息"""
        return {
            'num_templates': len(self.templates),
            'threshold': self.threshold
        }


if __name__ == '__main__':
    print("Wake Word Matcher Test")
    print("=" * 40)
    
    matcher = WakeWordMatcher()
    info = matcher.get_info()
    print(f"Templates: {info['num_templates']}")
    print(f"Threshold: {info['threshold']}")
