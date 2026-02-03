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
语音认证系统主模块
整合唤醒词检测和声纹识别功能
"""

from dataclasses import dataclass, field
from datetime import datetime
from enum import Enum
from typing import Callable, Optional

import numpy as np

from audio_utils import AudioRecorder
from config import (
	SAMPLE_RATE,
	RECORD_SECONDS,
	ENABLE_DEBUG
)
from user_manager import UserManager
from voiceprint import VoiceprintEncoder
from wake_word import WakeWordDetector


class AuthResult(Enum):
    """认证结果枚举"""
    SUCCESS = "success"                  # 认证成功
    WAKE_WORD_NOT_DETECTED = "no_wake"   # 未检测到唤醒词
    USER_NOT_REGISTERED = "not_registered"  # 用户未注册
    VOICE_MISMATCH = "mismatch"          # 声纹不匹配
    ERROR = "error"                      # 错误


@dataclass
class AuthenticationEvent:
    """认证事件"""
    timestamp: datetime
    result: AuthResult
    transcribed_text: str = ""
    matched_user: Optional[str] = None
    similarity_score: float = 0.0
    wake_word_confidence: float = 0.0


@dataclass
class AuthStats:
    """认证统计"""
    total_attempts: int = 0
    wake_word_detected: int = 0
    wake_word_not_detected: int = 0
    auth_success: int = 0
    auth_failed: int = 0
    events: list = field(default_factory=list)

    @property
    def wake_word_accuracy(self) -> float:
        """唤醒词检测准确率"""
        if self.total_attempts == 0:
            return 0.0
        return self.wake_word_detected / self.total_attempts

    @property
    def auth_accuracy(self) -> float:
        """认证成功率 (基于唤醒成功的尝试)"""
        total = self.auth_success + self.auth_failed
        if total == 0:
            return 0.0
        return self.auth_success / total

    def add_event(self, event: AuthenticationEvent):
        """添加认证事件"""
        self.total_attempts += 1

        if event.result == AuthResult.WAKE_WORD_NOT_DETECTED:
            self.wake_word_not_detected += 1
        else:
            self.wake_word_detected += 1

            if event.result == AuthResult.SUCCESS:
                self.auth_success += 1
            else:
                self.auth_failed += 1

        self.events.append(event)

    def get_summary(self) -> str:
        """获取统计摘要"""
        return f"""
===== 认证统计 =====
总尝试次数: {self.total_attempts}
唤醒词检测:
  - 成功: {self.wake_word_detected}
  - 失败: {self.wake_word_not_detected}
  - 准确率: {self.wake_word_accuracy:.1%}
声纹认证:
  - 成功: {self.auth_success}
  - 失败: {self.auth_failed}
  - 成功率: {self.auth_accuracy:.1%}
========================
"""


class VoiceAuthSystem:
    """语音认证系统"""

    def __init__(self, command_handler: Callable[[str, str], None] = None):
        """
        初始化语音认证系统

        Args:
            command_handler: 命令处理回调函数 (user_id, transcribed_text) -> None
        """
        self.user_manager = UserManager()
        self.wake_detector = WakeWordDetector()
        self.voiceprint_encoder = VoiceprintEncoder()
        self.recorder = None
        self.command_handler = command_handler or self._default_command_handler

        self.stats = AuthStats()
        self._running = False

    def _default_command_handler(self, user_id: str, text: str):
        """默认命令处理器"""
        print(f"🎯 执行命令 - 用户: {user_id}, 内容: \"{text}\"")

    def authenticate(self, audio: np.ndarray,
                     sample_rate: int = SAMPLE_RATE) -> AuthenticationEvent:
        """
        完整认证流程：唤醒词检测 + 声纹识别

        Args:
            audio: 输入音频
            sample_rate: 采样率

        Returns:
            认证事件
        """
        event = AuthenticationEvent(timestamp=datetime.now(), result=AuthResult.ERROR)

        try:
            # Step 1: 唤醒词检测
            if ENABLE_DEBUG:
                print("\n🔍 Step 1: 唤醒词检测...")

            is_wake, text, wake_conf = self.wake_detector.detect_wake_word(audio, sample_rate)
            event.transcribed_text = text
            event.wake_word_confidence = wake_conf

            if not is_wake:
                event.result = AuthResult.WAKE_WORD_NOT_DETECTED
                if ENABLE_DEBUG:
                    print(f"   ❌ 未检测到唤醒词 (识别内容: \"{text}\")")
                self.stats.add_event(event)
                return event

            if ENABLE_DEBUG:
                print(f"   ✅ 唤醒词检测成功 (置信度: {wake_conf:.1%})")

            # Step 2: 声纹识别
            if ENABLE_DEBUG:
                print("\n🔍 Step 2: 声纹识别...")

            if self.user_manager.user_count == 0:
                event.result = AuthResult.USER_NOT_REGISTERED
                if ENABLE_DEBUG:
                    print("   ⚠️ 没有注册用户")
                self.stats.add_event(event)
                return event

            matched_user, similarity = self.user_manager.verify_user(audio, sample_rate)
            event.matched_user = matched_user
            event.similarity_score = similarity

            if matched_user:
                event.result = AuthResult.SUCCESS
                if ENABLE_DEBUG:
                    print(f"   ✅ 声纹匹配成功: {matched_user} (相似度: {similarity:.1%})")
            else:
                event.result = AuthResult.VOICE_MISMATCH
                if ENABLE_DEBUG:
                    print(f"   ❌ 声纹不匹配 (最高相似度: {similarity:.1%})")

        except Exception as e:
            event.result = AuthResult.ERROR
            if ENABLE_DEBUG:
                print(f"   ❌ 认证错误: {e}")

        self.stats.add_event(event)
        return event

    def start_listening(self, continuous: bool = True,
                        record_duration: float = RECORD_SECONDS):
        """
        开始监听语音输入

        Args:
            continuous: 是否持续监听
            record_duration: 每次录音时长
        """
        self.recorder = AudioRecorder()
        self._running = True

        print("\n" + "=" * 50)
        print("🎤 语音认证系统已启动")
        print(f"   唤醒词: 你好世界")
        print(f"   已注册用户: {self.user_manager.user_count} 人")
        print("   按 Ctrl+C 停止")
        print("=" * 50 + "\n")

        try:
            while self._running:
                print("\n📢 请说话 (说\"你好世界\"来唤醒)...")

                try:
                    # 录制音频
                    audio = self.recorder.record(duration=record_duration,
                                                  show_progress=False)

                    # 执行认证
                    event = self.authenticate(audio, SAMPLE_RATE)

                    # 显示结果
                    self._display_result(event)

                    # 如果认证成功，执行命令
                    if event.result == AuthResult.SUCCESS:
                        self.command_handler(event.matched_user, event.transcribed_text)

                except KeyboardInterrupt:
                    break

                if not continuous:
                    break

        except KeyboardInterrupt:
            pass
        finally:
            self.stop_listening()

    def stop_listening(self):
        """停止监听"""
        self._running = False
        if self.recorder:
            self.recorder.close()
            self.recorder = None
        print("\n🛑 语音认证系统已停止")

    def _display_result(self, event: AuthenticationEvent):
        """显示认证结果"""
        print("\n" + "-" * 40)
        print(f"📝 识别内容: \"{event.transcribed_text}\"")

        if event.result == AuthResult.WAKE_WORD_NOT_DETECTED:
            print("❌ 结果: 未检测到唤醒词")
        elif event.result == AuthResult.USER_NOT_REGISTERED:
            print("⚠️ 结果: 没有注册用户，无法验证声纹")
        elif event.result == AuthResult.VOICE_MISMATCH:
            print(f"🚫 结果: 声纹不匹配 (相似度: {event.similarity_score:.1%})")
            print("   该用户未注册，拒绝执行命令")
        elif event.result == AuthResult.SUCCESS:
            user_info = self.user_manager.get_user(event.matched_user)
            user_name = user_info.get('name', event.matched_user) if user_info else event.matched_user
            print(f"✅ 结果: 认证成功!")
            print(f"   用户: {user_name}")
            print(f"   相似度: {event.similarity_score:.1%}")
        else:
            print(f"❓ 结果: {event.result.value}")

        print("-" * 40)

    def single_authenticate(self, record_duration: float = RECORD_SECONDS) -> AuthenticationEvent:
        """
        单次认证 (录音 + 认证)

        Args:
            record_duration: 录音时长

        Returns:
            认证事件
        """
        recorder = AudioRecorder()
        try:
            print("\n📢 请说话...")
            audio = recorder.record(duration=record_duration, show_progress=True)
            event = self.authenticate(audio, SAMPLE_RATE)
            self._display_result(event)
            return event
        finally:
            recorder.close()

    def register_user(self, user_id: str, name: str = None) -> bool:
        """注册新用户"""
        return self.user_manager.register_user(user_id, name)

    def remove_user(self, user_id: str) -> bool:
        """删除用户"""
        return self.user_manager.remove_user(user_id)

    def list_users(self):
        """列出所有用户"""
        return self.user_manager.list_users()

    def get_stats(self) -> AuthStats:
        """获取统计信息"""
        return self.stats

    def print_stats(self):
        """打印统计信息"""
        print(self.stats.get_summary())


if __name__ == "__main__":
    # 快速测试
    print("=== 语音认证系统测试 ===\n")

    system = VoiceAuthSystem()

    print(f"已注册用户: {system.user_manager.user_count}")

    # 单次认证测试
    print("\n进行单次认证测试...")
    event = system.single_authenticate(duration=4)

    print(f"\n认证结果: {event.result.value}")
    system.print_stats()
