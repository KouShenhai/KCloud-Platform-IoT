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
声纹管理器模块

管理说话人声纹的注册、验证和存储
"""

import os
import json
import numpy as np
from datetime import datetime
from typing import Dict, List, Optional, Tuple
from speaker_encoder import SpeakerEncoder, compute_similarity, EMBEDDING_DIM


# 默认存储目录
SPEAKERS_DIR = "speakers"

# 默认验证阈值 (Resemblyzer 推荐 0.75-0.85)
DEFAULT_THRESHOLD = 0.80


class SpeakerManager:
    """声纹管理器"""

    def __init__(self,
                 speakers_dir: str = SPEAKERS_DIR,
                 encoder: SpeakerEncoder = None,
                 threshold: float = DEFAULT_THRESHOLD):
        """
        初始化声纹管理器

        Args:
            speakers_dir: 声纹存储目录
            encoder: 声纹编码器实例
            threshold: 验证阈值
        """
        self.speakers_dir = speakers_dir
        self.threshold = threshold

        # 创建存储目录
        os.makedirs(speakers_dir, exist_ok=True)

        # 初始化编码器
        self.encoder = encoder if encoder else SpeakerEncoder()

        # 加载已注册的声纹
        self.speakers: Dict[str, dict] = {}
        self._load_speakers()

    def _get_speaker_path(self, name: str) -> str:
        """获取说话人存储路径"""
        return os.path.join(self.speakers_dir, f"{name}.npz")

    def _load_speakers(self):
        """加载所有已注册的说话人"""
        if not os.path.exists(self.speakers_dir):
            return

        for filename in os.listdir(self.speakers_dir):
            if filename.endswith('.npz'):
                name = filename[:-4]
                try:
                    self._load_speaker(name)
                except Exception as e:
                    print(f"警告: 加载说话人 {name} 失败: {e}")

        print(f"已加载 {len(self.speakers)} 个注册声纹")

    def _load_speaker(self, name: str):
        """加载单个说话人"""
        path = self._get_speaker_path(name)
        if os.path.exists(path):
            data = np.load(path, allow_pickle=True)
            self.speakers[name] = {
                'embedding': data['embedding'],
                'metadata': data['metadata'].item() if 'metadata' in data else {}
            }

    def _save_speaker(self, name: str):
        """保存说话人到文件"""
        if name not in self.speakers:
            return

        path = self._get_speaker_path(name)
        speaker = self.speakers[name]

        np.savez(
            path,
            embedding=speaker['embedding'],
            metadata=speaker['metadata']
        )

    def enroll_speaker(self,
                       name: str,
                       audio_samples: List[np.ndarray],
                       overwrite: bool = False) -> bool:
        """
        注册新说话人

        Args:
            name: 说话人名称
            audio_samples: 音频样本列表
            overwrite: 是否覆盖已存在的注册

        Returns:
            是否注册成功
        """
        if name in self.speakers and not overwrite:
            print(f"错误: 说话人 '{name}' 已存在，使用 overwrite=True 覆盖")
            return False

        if len(audio_samples) < 1:
            print("错误: 至少需要1个音频样本")
            return False

        # 计算平均声纹
        embedding = self.encoder.compute_embeddings_from_samples(audio_samples)

        # 保存
        self.speakers[name] = {
            'embedding': embedding,
            'metadata': {
                'enrolled_at': datetime.now().isoformat(),
                'num_samples': len(audio_samples)
            }
        }
        self._save_speaker(name)

        print(f"✓ 说话人 '{name}' 注册成功 (使用 {len(audio_samples)} 个样本)")
        return True

    def enroll_from_files(self,
                          name: str,
                          audio_files: List[str],
                          overwrite: bool = False) -> bool:
        """
        从音频文件注册说话人

        Args:
            name: 说话人名称
            audio_files: 音频文件路径列表
            overwrite: 是否覆盖

        Returns:
            是否注册成功
        """
        import librosa

        audio_samples = []
        for file_path in audio_files:
            try:
                audio, _ = librosa.load(file_path, sr=16000, mono=True)
                audio_samples.append(audio)
            except Exception as e:
                print(f"警告: 加载文件 {file_path} 失败: {e}")

        if len(audio_samples) == 0:
            print("错误: 没有成功加载任何音频文件")
            return False

        return self.enroll_speaker(name, audio_samples, overwrite)

    def verify_speaker(self,
                       audio: np.ndarray,
                       claimed_name: str = None) -> Tuple[bool, str, float]:
        """
        验证说话人身份

        Args:
            audio: 待验证的音频
            claimed_name: 声称的身份（可选，None则与所有注册者比对）

        Returns:
            (是否验证通过, 匹配的说话人, 相似度分数)
        """
        if len(self.speakers) == 0:
            return False, "", 0.0

        # 提取待验证音频的声纹
        test_embedding = self.encoder.extract_embedding(audio)

        if claimed_name:
            # 验证特定身份
            if claimed_name not in self.speakers:
                return False, "", 0.0

            speaker = self.speakers[claimed_name]
            similarity = compute_similarity(test_embedding, speaker['embedding'])

            if similarity >= self.threshold:
                return True, claimed_name, similarity
            else:
                return False, claimed_name, similarity
        else:
            # 与所有注册者比对，找最高匹配
            best_match = ""
            best_score = -1.0

            for name, speaker in self.speakers.items():
                similarity = compute_similarity(test_embedding, speaker['embedding'])
                if similarity > best_score:
                    best_score = similarity
                    best_match = name

            if best_score >= self.threshold:
                return True, best_match, best_score
            else:
                return False, best_match, best_score

    def identify_speaker(self, audio: np.ndarray) -> Tuple[str, float]:
        """
        识别说话人（返回最匹配的）

        Args:
            audio: 音频数据

        Returns:
            (最匹配的说话人, 相似度分数)
        """
        _, name, score = self.verify_speaker(audio, claimed_name=None)
        return name, score

    def delete_speaker(self, name: str) -> bool:
        """
        删除说话人

        Args:
            name: 说话人名称

        Returns:
            是否删除成功
        """
        if name not in self.speakers:
            print(f"说话人 '{name}' 不存在")
            return False

        # 删除内存中的记录
        del self.speakers[name]

        # 删除文件
        path = self._get_speaker_path(name)
        if os.path.exists(path):
            os.remove(path)

        print(f"✓ 说话人 '{name}' 已删除")
        return True

    def list_speakers(self) -> List[str]:
        """
        列出所有注册的说话人

        Returns:
            说话人名称列表
        """
        return list(self.speakers.keys())

    def get_speaker_info(self, name: str) -> Optional[dict]:
        """
        获取说话人信息

        Args:
            name: 说话人名称

        Returns:
            说话人信息字典
        """
        if name not in self.speakers:
            return None

        speaker = self.speakers[name]
        return {
            'name': name,
            'embedding_dim': len(speaker['embedding']),
            **speaker.get('metadata', {})
        }

    def set_threshold(self, threshold: float):
        """设置验证阈值"""
        if 0.0 <= threshold <= 1.0:
            self.threshold = threshold
            print(f"验证阈值已设置为: {threshold}")
        else:
            print("错误: 阈值必须在 0.0-1.0 之间")


if __name__ == '__main__':
    # 测试代码
    print("声纹管理器测试")
    print("-" * 40)

    # 创建管理器
    manager = SpeakerManager()

    # 列出已注册说话人
    speakers = manager.list_speakers()
    print(f"已注册说话人: {speakers}")

    # 测试注册（使用随机数据）
    fake_audio1 = np.random.randn(32000).astype(np.float32)
    fake_audio2 = np.random.randn(32000).astype(np.float32)

    # 注册测试用户
    manager.enroll_speaker("test_user", [fake_audio1, fake_audio2], overwrite=True)

    # 验证
    verified, name, score = manager.verify_speaker(fake_audio1)
    print(f"验证结果: {verified}, 匹配: {name}, 分数: {score:.4f}")

    # 清理测试数据
    manager.delete_speaker("test_user")
