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
唤醒词检测模块
使用 Whisper ASR 进行语音识别，检测"你好世界"唤醒词
"""

import re
from typing import Tuple

import numpy as np

from config import (
	WAKE_WORD,
	WAKE_WORD_VARIANTS,
	WHISPER_MODEL,
	WHISPER_LANGUAGE,
	SAMPLE_RATE,
	ENABLE_DEBUG
)

# 延迟导入 Whisper (加载较慢)
_whisper_model = None


def get_whisper_model():
    """获取 Whisper 模型 (单例模式)"""
    global _whisper_model
    if _whisper_model is None:
        print(f"🔄 加载 Whisper 模型 ({WHISPER_MODEL})...")
        import whisper
        _whisper_model = whisper.load_model(WHISPER_MODEL)
        print("✅ Whisper 模型加载完成!")
    return _whisper_model


class WakeWordDetector:
    """唤醒词检测器"""

    def __init__(self, wake_word: str = WAKE_WORD):
        """
        初始化唤醒词检测器

        Args:
            wake_word: 唤醒词 (默认 "你好世界")
        """
        self.wake_word = wake_word
        self.model = get_whisper_model()

        # 预编译唤醒词匹配模式
        self.wake_patterns = self._build_patterns()

    def _build_patterns(self) -> list:
        """构建唤醒词匹配模式"""
        patterns = []

        # 添加标准唤醒词变体
        for variant in WAKE_WORD_VARIANTS:
            # 移除空格和标点进行匹配
            normalized = self._normalize_text(variant)
            patterns.append(normalized)

        # 添加自定义唤醒词
        if self.wake_word not in WAKE_WORD_VARIANTS:
            patterns.append(self._normalize_text(self.wake_word))

        return patterns

    def _normalize_text(self, text: str) -> str:
        """规范化文本用于匹配"""
        # 移除空格、标点
        text = re.sub(r'[,，.。!！?？\s]', '', text.lower())
        return text

    def transcribe(self, audio: np.ndarray,
                   sample_rate: int = SAMPLE_RATE) -> str:
        """
        使用 Whisper 转录音频

        Args:
            audio: 音频数据
            sample_rate: 采样率

        Returns:
            转录文本
        """
        # Whisper 需要 float32 数据
        if audio.dtype != np.float32:
            audio = audio.astype(np.float32)

        # 如果采样率不是 16000，需要重采样
        if sample_rate != 16000:
            audio = self._resample(audio, sample_rate, 16000)

        try:
            # 转录
            result = self.model.transcribe(
                audio,
                language=WHISPER_LANGUAGE,
                fp16=False,  # CPU 模式
                verbose=False
            )

            text = result.get('text', '').strip()

            if ENABLE_DEBUG:
                print(f"   📝 Whisper 转录: \"{text}\"")

            return text

        except Exception as e:
            if ENABLE_DEBUG:
                print(f"⚠️ Whisper 转录失败: {e}")
            return ""

    def detect_wake_word(self, audio: np.ndarray,
                         sample_rate: int = SAMPLE_RATE) -> Tuple[bool, str, float]:
        """
        检测音频中是否包含唤醒词

        Args:
            audio: 音频数据
            sample_rate: 采样率

        Returns:
            (是否检测到唤醒词, 识别的文本, 匹配置信度)
        """
        # 转录音频
        text = self.transcribe(audio, sample_rate)

        if not text:
            return False, "", 0.0

        # 检测唤醒词
        is_match, confidence = self.is_wake_word_match(text)

        return is_match, text, confidence

    def is_wake_word_match(self, text: str) -> Tuple[bool, float]:
        """
        检查文本是否匹配唤醒词

        Args:
            text: 输入文本

        Returns:
            (是否匹配, 置信度分数)
        """
        normalized_text = self._normalize_text(text)

        # 精确匹配检查
        for pattern in self.wake_patterns:
            if pattern in normalized_text:
                return True, 1.0

        # 模糊匹配 - 计算编辑距离
        best_similarity = 0.0
        primary_pattern = self._normalize_text(WAKE_WORD)  # 你好世界

        # 使用滑动窗口检查相似度
        window_size = len(primary_pattern)
        for i in range(max(1, len(normalized_text) - window_size + 1)):
            window = normalized_text[i:i + window_size]
            similarity = self._calculate_similarity(window, primary_pattern)
            best_similarity = max(best_similarity, similarity)

        # 如果整体相似度也很高，考虑匹配
        overall_similarity = self._calculate_similarity(normalized_text, primary_pattern)
        best_similarity = max(best_similarity, overall_similarity)

        # 阈值 0.8 认为是匹配
        is_match = best_similarity >= 0.8

        if ENABLE_DEBUG and best_similarity > 0.5:
            print(f"   🔍 唤醒词匹配相似度: {best_similarity:.2%}")

        return is_match, best_similarity

    def _calculate_similarity(self, s1: str, s2: str) -> float:
        """计算两个字符串的相似度 (1 - 归一化编辑距离)"""
        if not s1 or not s2:
            return 0.0

        # 简化的 Levenshtein 距离
        len1, len2 = len(s1), len(s2)

        if len1 == 0:
            return 0.0
        if len2 == 0:
            return 0.0

        # 创建距离矩阵
        dp = [[0] * (len2 + 1) for _ in range(len1 + 1)]

        for i in range(len1 + 1):
            dp[i][0] = i
        for j in range(len2 + 1):
            dp[0][j] = j

        for i in range(1, len1 + 1):
            for j in range(1, len2 + 1):
                cost = 0 if s1[i - 1] == s2[j - 1] else 1
                dp[i][j] = min(
                    dp[i - 1][j] + 1,      # 删除
                    dp[i][j - 1] + 1,      # 插入
                    dp[i - 1][j - 1] + cost  # 替换
                )

        distance = dp[len1][len2]
        max_len = max(len1, len2)

        return 1.0 - (distance / max_len)

    def _resample(self, audio: np.ndarray, orig_sr: int, target_sr: int) -> np.ndarray:
        """简单重采样"""
        if orig_sr == target_sr:
            return audio
        ratio = target_sr / orig_sr
        new_length = int(len(audio) * ratio)
        indices = np.linspace(0, len(audio) - 1, new_length)
        return np.interp(indices, np.arange(len(audio)), audio).astype(np.float32)


def detect_wake_word_simple(text: str) -> bool:
    """
    简单的唤醒词检测 (仅基于文本匹配)
    用于已经完成 ASR 转录的场景

    Args:
        text: 输入文本

    Returns:
        是否包含唤醒词
    """
    # 规范化文本
    normalized = re.sub(r'[,，.。!！?？\s]', '', text.lower())

    # 检查所有唤醒词变体
    for variant in WAKE_WORD_VARIANTS:
        variant_normalized = re.sub(r'[,，.。!！?？\s]', '', variant.lower())
        if variant_normalized in normalized:
            return True

    return False


if __name__ == "__main__":
    # 测试唤醒词检测
    print("=== 唤醒词检测测试 ===\n")

    # 初始化检测器
    detector = WakeWordDetector()

    # 测试文本匹配
    print("1. 测试文本匹配")
    test_cases = [
        "你好世界",
        "你好，世界",
        "世界你好",
        "你好小明",
        "今天天气很好",
        "ni hao xiao yun",
    ]

    for text in test_cases:
        is_match, confidence = detector.is_wake_word_match(text)
        status = "✅" if is_match else "❌"
        print(f"   {status} \"{text}\" -> 匹配: {is_match}, 置信度: {confidence:.2%}")

    # 测试音频转录 (如果有音频设备)
    print("\n2. 测试音频转录")
    try:
        from audio_utils import AudioRecorder

        print("   请说话 (3秒)...")
        recorder = AudioRecorder()
        audio = recorder.record(duration=3)
        recorder.close()

        is_wake, text, conf = detector.detect_wake_word(audio)
        print(f"   识别文本: \"{text}\"")
        print(f"   唤醒词检测: {'✅ 是' if is_wake else '❌ 否'}")
        print(f"   置信度: {conf:.2%}")

    except Exception as e:
        print(f"   ⚠️ 音频测试跳过: {e}")

    print("\n✅ 唤醒词检测测试完成!")
