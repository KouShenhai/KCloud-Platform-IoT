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
声纹处理模块
使用 Resemblyzer 预训练模型提取和比对声纹特征
"""

import numpy as np
from typing import List, Tuple, Optional, Union
from pathlib import Path

from config import (
    VOICEPRINT_SIMILARITY_THRESHOLD,
    VOICEPRINT_EMBEDDING_DIM,
    SAMPLE_RATE,
    ENABLE_DEBUG
)

# 延迟导入 Resemblyzer (加载较慢)
_encoder = None


def get_encoder():
    """获取 Resemblyzer 编码器 (单例模式)"""
    global _encoder
    if _encoder is None:
        print("🔄 加载声纹模型 (首次加载可能需要几秒)...")
        from resemblyzer import VoiceEncoder
        _encoder = VoiceEncoder()
        print("✅ 声纹模型加载完成!")
    return _encoder


class VoiceprintEncoder:
    """声纹特征编码器"""
    
    def __init__(self):
        self.encoder = get_encoder()
        
    def extract_embedding(self, audio: np.ndarray, 
                          sample_rate: int = SAMPLE_RATE) -> np.ndarray:
        """
        从音频中提取声纹嵌入向量
        
        Args:
            audio: 音频数据 (float32, -1 to 1)
            sample_rate: 采样率
            
        Returns:
            256维声纹嵌入向量
        """
        # Resemblyzer 需要 float64 数据，采样率 16000
        if audio.dtype != np.float64:
            audio = audio.astype(np.float64)
            
        # 如果采样率不是 16000，需要重采样
        if sample_rate != 16000:
            audio = self._resample(audio, sample_rate, 16000)
            
        # 提取嵌入向量
        try:
            from resemblyzer import preprocess_wav
            processed = preprocess_wav(audio)
            embedding = self.encoder.embed_utterance(processed)
            return embedding
        except Exception as e:
            if ENABLE_DEBUG:
                print(f"⚠️ 声纹提取失败: {e}")
            # 返回零向量作为失败情况
            return np.zeros(VOICEPRINT_EMBEDDING_DIM, dtype=np.float32)
    
    def extract_embedding_from_file(self, file_path: Union[str, Path]) -> np.ndarray:
        """从音频文件提取声纹嵌入向量"""
        from audio_utils import load_audio
        audio, sr = load_audio(file_path)
        return self.extract_embedding(audio, sr)
    
    def _resample(self, audio: np.ndarray, orig_sr: int, target_sr: int) -> np.ndarray:
        """简单重采样"""
        if orig_sr == target_sr:
            return audio
        ratio = target_sr / orig_sr
        new_length = int(len(audio) * ratio)
        indices = np.linspace(0, len(audio) - 1, new_length)
        return np.interp(indices, np.arange(len(audio)), audio)
    
    @staticmethod
    def compare_embeddings(emb1: np.ndarray, emb2: np.ndarray) -> float:
        """
        计算两个声纹嵌入向量的相似度 (余弦相似度)
        
        Args:
            emb1: 第一个嵌入向量
            emb2: 第二个嵌入向量
            
        Returns:
            相似度分数 (0-1)
        """
        # 归一化
        norm1 = np.linalg.norm(emb1)
        norm2 = np.linalg.norm(emb2)
        
        if norm1 == 0 or norm2 == 0:
            return 0.0
            
        emb1_normalized = emb1 / norm1
        emb2_normalized = emb2 / norm2
        
        # 余弦相似度
        similarity = np.dot(emb1_normalized, emb2_normalized)
        
        # 确保在 [0, 1] 范围内
        return float(max(0.0, min(1.0, (similarity + 1) / 2)))
    
    @staticmethod
    def is_same_speaker(emb1: np.ndarray, emb2: np.ndarray,
                        threshold: float = VOICEPRINT_SIMILARITY_THRESHOLD) -> Tuple[bool, float]:
        """
        判断两个声纹是否属于同一说话人
        
        Args:
            emb1: 第一个嵌入向量
            emb2: 第二个嵌入向量
            threshold: 相似度阈值
            
        Returns:
            (是否同一人, 相似度分数)
        """
        similarity = VoiceprintEncoder.compare_embeddings(emb1, emb2)
        return similarity >= threshold, similarity


class VoiceprintMatcher:
    """声纹匹配器 - 用于与注册用户进行声纹比对"""
    
    def __init__(self):
        self.encoder = VoiceprintEncoder()
        self.registered_users = {}  # {user_id: embedding}
        
    def register_user(self, user_id: str, embeddings: List[np.ndarray]) -> np.ndarray:
        """
        注册用户声纹
        
        Args:
            user_id: 用户ID
            embeddings: 多个声纹嵌入向量列表 (用于取平均)
            
        Returns:
            平均后的用户声纹嵌入向量
        """
        # 计算平均嵌入向量
        avg_embedding = np.mean(embeddings, axis=0)
        # 归一化
        avg_embedding = avg_embedding / np.linalg.norm(avg_embedding)
        self.registered_users[user_id] = avg_embedding
        
        if ENABLE_DEBUG:
            print(f"✅ 用户 '{user_id}' 声纹注册成功 (基于 {len(embeddings)} 个样本)")
            
        return avg_embedding
    
    def unregister_user(self, user_id: str) -> bool:
        """注销用户"""
        if user_id in self.registered_users:
            del self.registered_users[user_id]
            return True
        return False
    
    def match(self, audio: np.ndarray, 
              sample_rate: int = SAMPLE_RATE) -> Tuple[Optional[str], float]:
        """
        将输入音频与所有注册用户进行声纹匹配
        
        Args:
            audio: 输入音频
            sample_rate: 采样率
            
        Returns:
            (匹配的用户ID, 最高相似度) 如果未匹配返回 (None, 0.0)
        """
        if not self.registered_users:
            return None, 0.0
            
        # 提取输入音频的声纹
        input_embedding = self.encoder.extract_embedding(audio, sample_rate)
        
        best_match = None
        best_score = 0.0
        
        for user_id, user_embedding in self.registered_users.items():
            is_match, score = VoiceprintEncoder.is_same_speaker(
                input_embedding, user_embedding
            )
            
            if ENABLE_DEBUG:
                print(f"   - 与用户 '{user_id}' 相似度: {score:.4f}")
                
            if score > best_score:
                best_score = score
                if is_match:
                    best_match = user_id
                    
        return best_match, best_score
    
    def match_embedding(self, input_embedding: np.ndarray) -> Tuple[Optional[str], float]:
        """
        使用预提取的嵌入向量进行匹配
        
        Args:
            input_embedding: 输入音频的嵌入向量
            
        Returns:
            (匹配的用户ID, 最高相似度)
        """
        if not self.registered_users:
            return None, 0.0
            
        best_match = None
        best_score = 0.0
        
        for user_id, user_embedding in self.registered_users.items():
            is_match, score = VoiceprintEncoder.is_same_speaker(
                input_embedding, user_embedding
            )
            
            if ENABLE_DEBUG:
                print(f"   - 与用户 '{user_id}' 相似度: {score:.4f}")
                
            if score > best_score:
                best_score = score
                if is_match:
                    best_match = user_id
                    
        return best_match, best_score
    
    def get_all_users(self) -> List[str]:
        """获取所有注册用户ID"""
        return list(self.registered_users.keys())
    
    def get_user_embedding(self, user_id: str) -> Optional[np.ndarray]:
        """获取用户声纹嵌入向量"""
        return self.registered_users.get(user_id)


if __name__ == "__main__":
    # 测试声纹模块
    print("=== 声纹模块测试 ===\n")
    
    # 初始化编码器
    encoder = VoiceprintEncoder()
    
    # 生成模拟音频数据
    print("1. 测试声纹提取")
    fake_audio = np.random.randn(SAMPLE_RATE * 3).astype(np.float32) * 0.1
    embedding = encoder.extract_embedding(fake_audio)
    print(f"   嵌入向量维度: {embedding.shape}")
    print(f"   嵌入向量范围: [{embedding.min():.4f}, {embedding.max():.4f}]")
    
    # 测试相似度计算
    print("\n2. 测试相似度计算")
    emb1 = np.random.randn(VOICEPRINT_EMBEDDING_DIM).astype(np.float32)
    emb2 = emb1 + np.random.randn(VOICEPRINT_EMBEDDING_DIM).astype(np.float32) * 0.1
    emb3 = np.random.randn(VOICEPRINT_EMBEDDING_DIM).astype(np.float32)
    
    sim_same = VoiceprintEncoder.compare_embeddings(emb1, emb2)
    sim_diff = VoiceprintEncoder.compare_embeddings(emb1, emb3)
    
    print(f"   相似声纹相似度: {sim_same:.4f}")
    print(f"   不同声纹相似度: {sim_diff:.4f}")
    
    print("\n✅ 声纹模块测试完成!")
