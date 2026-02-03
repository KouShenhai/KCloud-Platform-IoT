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
用户管理模块
管理注册用户的声纹信息
"""

import json
from datetime import datetime
from typing import Dict, List, Optional, Tuple

import numpy as np

from audio_utils import AudioRecorder, save_wav
from config import (
	USERS_DIR,
	USERS_FILE,
	REGISTRATION_SAMPLES,
	SAMPLE_RATE,
	RECORD_SECONDS,
	ENABLE_DEBUG
)
from voiceprint import VoiceprintEncoder, VoiceprintMatcher


class UserManager:
    """用户声纹管理器"""

    def __init__(self):
        """初始化用户管理器"""
        self.encoder = VoiceprintEncoder()
        self.matcher = VoiceprintMatcher()
        self.users_info = {}  # {user_id: {"name": str, "created_at": str, ...}}

        # 加载已注册用户
        self._load_users()

    def _load_users(self):
        """从文件加载已注册用户"""
        if not USERS_FILE.exists():
            return

        try:
            with open(USERS_FILE, 'r', encoding='utf-8') as f:
                data = json.load(f)

            self.users_info = data.get('users', {})

            # 加载声纹嵌入向量
            for user_id, user_info in self.users_info.items():
                embedding_file = USERS_DIR / f"{user_id}_embedding.npy"
                if embedding_file.exists():
                    embedding = np.load(embedding_file)
                    self.matcher.registered_users[user_id] = embedding

            if ENABLE_DEBUG:
                print(f"📁 已加载 {len(self.users_info)} 个注册用户")

        except Exception as e:
            if ENABLE_DEBUG:
                print(f"⚠️ 加载用户数据失败: {e}")

    def _save_users(self):
        """保存用户数据到文件"""
        try:
            # 确保目录存在
            USERS_DIR.mkdir(parents=True, exist_ok=True)

            # 保存用户信息
            data = {'users': self.users_info}
            with open(USERS_FILE, 'w', encoding='utf-8') as f:
                json.dump(data, f, ensure_ascii=False, indent=2)

            # 保存声纹嵌入向量
            for user_id, embedding in self.matcher.registered_users.items():
                embedding_file = USERS_DIR / f"{user_id}_embedding.npy"
                np.save(embedding_file, embedding)

        except Exception as e:
            if ENABLE_DEBUG:
                print(f"⚠️ 保存用户数据失败: {e}")

    def register_user(self, user_id: str, name: str = None,
                      num_samples: int = REGISTRATION_SAMPLES,
                      record_duration: float = RECORD_SECONDS) -> bool:
        """
        注册新用户声纹

        Args:
            user_id: 用户ID (唯一标识)
            name: 用户姓名 (可选)
            num_samples: 需要录制的语音样本数
            record_duration: 每个样本录制时长

        Returns:
            是否注册成功
        """
        if user_id in self.users_info:
            print(f"⚠️ 用户 '{user_id}' 已存在，请先删除后重新注册")
            return False

        name = name or user_id

        print(f"\n{'='*50}")
        print(f"📝 开始注册用户: {name} (ID: {user_id})")
        print(f"   需要录制 {num_samples} 段语音样本")
        print(f"{'='*50}\n")

        recorder = AudioRecorder()
        embeddings = []
        audio_samples = []

        try:
            for i in range(num_samples):
                print(f"\n🎙️ 第 {i + 1}/{num_samples} 段录音")
                print("   请朗读：你好小寇，我是{}".format(name))
                input("   按 Enter 开始录音...")

                # 录制音频
                audio = recorder.record(duration=record_duration, show_progress=True)
                audio_samples.append(audio)

                # 提取声纹
                embedding = self.encoder.extract_embedding(audio, SAMPLE_RATE)
                embeddings.append(embedding)

                print(f"   ✅ 第 {i + 1} 段录音完成")

            # 注册声纹 (取平均)
            avg_embedding = self.matcher.register_user(user_id, embeddings)

            # 保存用户信息
            self.users_info[user_id] = {
                'name': name,
                'created_at': datetime.now().isoformat(),
                'num_samples': num_samples
            }

            # 保存录音文件 (用于调试)
            user_audio_dir = USERS_DIR / user_id
            user_audio_dir.mkdir(parents=True, exist_ok=True)
            for i, audio in enumerate(audio_samples):
                save_wav(audio, user_audio_dir / f"sample_{i + 1}.wav")

            # 持久化
            self._save_users()

            print(f"\n🎉 用户 '{name}' 注册成功!")
            print(f"   声纹特征维度: {avg_embedding.shape}")
            return True

        except Exception as e:
            print(f"\n❌ 注册失败: {e}")
            return False
        finally:
            recorder.close()

    def register_user_from_audio(self, user_id: str, audio_list: List[np.ndarray],
                                  name: str = None) -> bool:
        """
        从已有音频注册用户声纹

        Args:
            user_id: 用户ID
            audio_list: 音频数据列表
            name: 用户姓名

        Returns:
            是否注册成功
        """
        if user_id in self.users_info:
            print(f"⚠️ 用户 '{user_id}' 已存在")
            return False

        name = name or user_id

        # 提取声纹
        embeddings = []
        for audio in audio_list:
            embedding = self.encoder.extract_embedding(audio, SAMPLE_RATE)
            embeddings.append(embedding)

        # 注册
        self.matcher.register_user(user_id, embeddings)

        # 保存用户信息
        self.users_info[user_id] = {
            'name': name,
            'created_at': datetime.now().isoformat(),
            'num_samples': len(audio_list)
        }

        self._save_users()
        return True

    def remove_user(self, user_id: str) -> bool:
        """
        删除用户

        Args:
            user_id: 用户ID

        Returns:
            是否删除成功
        """
        if user_id not in self.users_info:
            print(f"⚠️ 用户 '{user_id}' 不存在")
            return False

        # 删除声纹
        self.matcher.unregister_user(user_id)

        # 删除用户信息
        del self.users_info[user_id]

        # 删除文件
        try:
            embedding_file = USERS_DIR / f"{user_id}_embedding.npy"
            if embedding_file.exists():
                embedding_file.unlink()

            user_audio_dir = USERS_DIR / user_id
            if user_audio_dir.exists():
                import shutil
                shutil.rmtree(user_audio_dir)
        except Exception as e:
            if ENABLE_DEBUG:
                print(f"⚠️ 删除用户文件失败: {e}")

        # 保存
        self._save_users()

        print(f"✅ 用户 '{user_id}' 已删除")
        return True

    def list_users(self) -> List[Dict]:
        """
        获取所有注册用户列表

        Returns:
            用户信息列表
        """
        users = []
        for user_id, info in self.users_info.items():
            users.append({
                'user_id': user_id,
                'name': info.get('name', user_id),
                'created_at': info.get('created_at', 'Unknown')
            })
        return users

    def get_user(self, user_id: str) -> Optional[Dict]:
        """获取用户信息"""
        if user_id not in self.users_info:
            return None
        return {
            'user_id': user_id,
            **self.users_info[user_id]
        }

    def verify_user(self, audio: np.ndarray,
                    sample_rate: int = SAMPLE_RATE) -> Tuple[Optional[str], float]:
        """
        验证音频对应的用户

        Args:
            audio: 输入音频
            sample_rate: 采样率

        Returns:
            (匹配的用户ID, 相似度分数)
        """
        return self.matcher.match(audio, sample_rate)

    def verify_embedding(self, embedding: np.ndarray) -> Tuple[Optional[str], float]:
        """
        使用嵌入向量验证用户

        Args:
            embedding: 声纹嵌入向量

        Returns:
            (匹配的用户ID, 相似度分数)
        """
        return self.matcher.match_embedding(embedding)

    @property
    def user_count(self) -> int:
        """注册用户数量"""
        return len(self.users_info)


if __name__ == "__main__":
    # 测试用户管理
    print("=== 用户管理模块测试 ===\n")

    manager = UserManager()

    print(f"当前注册用户数: {manager.user_count}")

    # 列出用户
    users = manager.list_users()
    if users:
        print("\n已注册用户:")
        for user in users:
            print(f"  - {user['name']} (ID: {user['user_id']})")
    else:
        print("\n暂无注册用户")

    print("\n✅ 用户管理模块测试完成!")
