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
唤醒词检测测试工具

实时监听麦克风输入，检测唤醒词并统计准确率
支持声纹认证：只有注册的用户才能唤醒
"""

import os
import sys
import time
import argparse
import numpy as np
import pyaudio
from queue import Queue
from datetime import datetime
from tensorflow import keras

from audio_utils import preprocess_audio
from feature_extractor import extract_features
from speaker_manager import SpeakerManager


class WakeWordTester:
    """唤醒词检测测试器（支持声纹验证）"""

    def __init__(self,
                 model_path: str = "models/wake_word_model.keras",
                 sample_rate: int = 16000,
                 chunk_duration: float = 2.0,
                 wake_threshold: float = 0.7,
                 voice_threshold: float = 0.70,
                 use_voiceprint: bool = False,
                 overlap: float = 0.5):
        """
        初始化测试器

        Args:
            model_path: 唤醒词模型路径
            sample_rate: 采样率
            chunk_duration: 每个音频块的时长（秒）
            wake_threshold: 唤醒词检测阈值
            voice_threshold: 声纹验证阈值
            use_voiceprint: 是否启用声纹验证
            overlap: 音频块重叠比例
        """
        self.sample_rate = sample_rate
        self.chunk_duration = chunk_duration
        self.wake_threshold = wake_threshold
        self.voice_threshold = voice_threshold
        self.use_voiceprint = use_voiceprint
        self.overlap = overlap

        # 计算采样点数
        self.chunk_samples = int(sample_rate * chunk_duration)
        self.step_samples = int(self.chunk_samples * (1 - overlap))

        # 加载唤醒词模型
        print(f"加载唤醒词模型: {model_path}")
        self.model = keras.models.load_model(model_path)
        print("唤醒词模型加载成功！")

        # 初始化声纹管理器
        if use_voiceprint:
            print("初始化声纹验证...")
            self.speaker_manager = SpeakerManager(threshold=voice_threshold)
            speakers = self.speaker_manager.list_speakers()
            if len(speakers) == 0:
                print("⚠️  警告: 没有注册任何声纹！请先使用 enroll_speaker.py 注册")
            else:
                print(f"已加载 {len(speakers)} 个声纹: {', '.join(speakers)}")
        else:
            self.speaker_manager = None

        # PyAudio设置
        self.audio = pyaudio.PyAudio()
        self.stream = None

        # 音频缓冲区
        self.audio_buffer = np.array([], dtype=np.float32)
        self.audio_queue = Queue()

        # 统计信息
        self.stats = {
            'total_tests': 0,
            'wake_word_detected': 0,
            'voice_verified': 0,
            'voice_rejected': 0,
            'true_positives': 0,
            'false_positives': 0,
            'true_negatives': 0,
            'false_negatives': 0,
            'predictions': [],
            'labels': []
        }

        # 控制标志
        self.is_running = False

    def start_listening(self):
        """开始监听麦克风"""
        self.stream = self.audio.open(
            format=pyaudio.paFloat32,
            channels=1,
            rate=self.sample_rate,
            input=True,
            frames_per_buffer=1024,
            stream_callback=self._audio_callback
        )
        self.is_running = True
        self.stream.start_stream()

    def stop_listening(self):
        """停止监听"""
        self.is_running = False
        if self.stream:
            self.stream.stop_stream()
            self.stream.close()
        self.audio.terminate()

    def _audio_callback(self, in_data, frame_count, time_info, status):
        """音频回调函数"""
        if self.is_running:
            audio_data = np.frombuffer(in_data, dtype=np.float32)
            self.audio_queue.put(audio_data)
        return (None, pyaudio.paContinue)

    def process_audio(self) -> dict:
        """
        处理音频并进行预测

        Returns:
            结果字典 或 None
        """
        # 从队列收集音频数据
        while not self.audio_queue.empty():
            chunk = self.audio_queue.get()
            self.audio_buffer = np.concatenate([self.audio_buffer, chunk])

        # 如果缓冲区足够长，进行预测
        if len(self.audio_buffer) >= self.chunk_samples:
            # 提取音频块
            audio_chunk = self.audio_buffer[:self.chunk_samples].copy()

            # 移动缓冲区
            self.audio_buffer = self.audio_buffer[self.step_samples:]

            # 预处理
            processed = preprocess_audio(audio_chunk)

            # 提取特征
            features = extract_features(processed)

            # 添加batch维度
            features = np.expand_dims(features, axis=0)

            # 唤醒词预测
            prediction = self.model.predict(features, verbose=0)[0]
            wake_word_prob = prediction[1]
            is_wake_word = wake_word_prob >= self.wake_threshold

            result = {
                'is_wake_word': is_wake_word,
                'wake_confidence': wake_word_prob,
                'voice_verified': False,
                'voice_similarity': 0.0,
                'speaker_name': '',
                'final_result': False
            }

            # 声纹验证
            if is_wake_word and self.use_voiceprint and self.speaker_manager:
                verified, speaker, similarity = self.speaker_manager.verify_speaker(audio_chunk)
                result['voice_verified'] = verified
                result['voice_similarity'] = similarity
                result['speaker_name'] = speaker
                result['final_result'] = verified  # 双重验证：唤醒词+声纹
            else:
                result['final_result'] = is_wake_word and not self.use_voiceprint

            return result

        return None

    def record_and_test(self, label: int, is_authorized: bool = True, duration: float = 2.0):
        """
        录制音频并测试

        Args:
            label: 真实标签 (1=唤醒词, 0=非唤醒词)
            is_authorized: 是否为授权用户
            duration: 录制时长
        """
        print(f"\n{'='*50}")
        if label == 1:
            if is_authorized:
                print("请说出唤醒词（授权用户）...")
            else:
                print("请说出唤醒词（模拟非授权用户）...")
        else:
            print("请说任意其他词语或保持安静...")
        print(f"{'='*50}")

        # 清空缓冲区
        self.audio_buffer = np.array([], dtype=np.float32)
        while not self.audio_queue.empty():
            self.audio_queue.get()

        # 等待录音
        time.sleep(duration + 0.5)

        # 处理音频
        result = self.process_audio()

        if result:
            self.stats['total_tests'] += 1

            is_wake = result['is_wake_word']
            final = result['final_result']

            # 更新统计
            if is_wake:
                self.stats['wake_word_detected'] += 1

            if self.use_voiceprint:
                if result['voice_verified']:
                    self.stats['voice_verified'] += 1
                elif is_wake:
                    self.stats['voice_rejected'] += 1

            # 对于声纹验证模式，期望结果是：授权用户说唤醒词才能通过
            expected = (label == 1 and is_authorized) if self.use_voiceprint else (label == 1)

            if final == expected:
                if final:
                    self.stats['true_positives'] += 1
                else:
                    self.stats['true_negatives'] += 1
            else:
                if final:
                    self.stats['false_positives'] += 1
                else:
                    self.stats['false_negatives'] += 1

            self.stats['predictions'].append(1 if final else 0)
            self.stats['labels'].append(1 if expected else 0)

            # 显示结果
            print(f"\n唤醒词检测: {'✓ 通过' if is_wake else '✗ 未通过'} (置信度: {result['wake_confidence']:.2%})")

            if self.use_voiceprint and is_wake:
                print(f"声纹验证:   {'✓ 通过' if result['voice_verified'] else '✗ 未通过'} (相似度: {result['voice_similarity']:.2%})")
                if result['speaker_name']:
                    print(f"匹配用户:   {result['speaker_name']}")

            correct = final == expected
            color = "\033[92m" if correct else "\033[91m"
            reset = "\033[0m"
            print(f"\n最终结果: {'🔔 唤醒成功' if final else '🔇 未唤醒'}")
            print(f"判定: {color}{'正确' if correct else '错误'}{reset}")
        else:
            print("未能获取足够的音频数据")

    def continuous_test(self):
        """连续测试模式"""
        print("\n" + "="*60)
        print("连续测试模式" + (" (声纹验证已启用)" if self.use_voiceprint else ""))
        print("="*60)
        print("实时监测唤醒词，按 Ctrl+C 停止")
        print("-"*60)

        last_detection_time = 0
        detection_cooldown = 2.0

        try:
            while self.is_running:
                result = self.process_audio()

                if result:
                    current_time = time.time()

                    if result['is_wake_word'] and (current_time - last_detection_time) > detection_cooldown:
                        last_detection_time = current_time
                        timestamp = datetime.now().strftime("%H:%M:%S")

                        if self.use_voiceprint:
                            if result['final_result']:
                                print(f"[{timestamp}] 🔔 唤醒成功! 用户: {result['speaker_name']} "
                                      f"(唤醒词: {result['wake_confidence']:.0%}, 声纹: {result['voice_similarity']:.0%})")
                                self.stats['voice_verified'] += 1
                            else:
                                print(f"[{timestamp}] 🔇 声纹验证失败 "
                                      f"(唤醒词: {result['wake_confidence']:.0%}, 声纹: {result['voice_similarity']:.0%})")
                                self.stats['voice_rejected'] += 1
                        else:
                            print(f"[{timestamp}] 🔔 检测到唤醒词！(置信度: {result['wake_confidence']:.0%})")

                        self.stats['wake_word_detected'] += 1

                time.sleep(0.05)

        except KeyboardInterrupt:
            print("\n停止连续测试...")
            self.print_statistics()

    def interactive_test(self, num_positive: int = 10, num_negative: int = 10):
        """交互式测试"""
        print("\n" + "="*60)
        print("交互式唤醒词检测测试" + (" (声纹验证已启用)" if self.use_voiceprint else ""))
        print("="*60)
        print(f"将进行 {num_positive} 次唤醒词测试和 {num_negative} 次非唤醒词测试")
        print("-"*60)

        input("按 Enter 开始测试...")

        # 测试唤醒词（正样本）
        print(f"\n{'#'*50}")
        print("第一阶段：唤醒词测试（授权用户）")
        print(f"{'#'*50}")

        for i in range(num_positive):
            print(f"\n[{i+1}/{num_positive}] ", end="")
            input("准备好后按 Enter...")
            self.record_and_test(label=1, is_authorized=True)

        # 测试非唤醒词（负样本）
        print(f"\n{'#'*50}")
        print("第二阶段：非唤醒词测试")
        print(f"{'#'*50}")

        for i in range(num_negative):
            print(f"\n[{i+1}/{num_negative}] ", end="")
            input("准备好后按 Enter...")
            self.record_and_test(label=0)

        self.print_statistics()

    def print_statistics(self):
        """打印统计结果"""
        print("\n" + "="*60)
        print("测试统计结果")
        print("="*60)

        total = self.stats['total_tests']
        if total == 0:
            print("还没有进行任何测试")
            return

        tp = self.stats['true_positives']
        fp = self.stats['false_positives']
        tn = self.stats['true_negatives']
        fn = self.stats['false_negatives']

        accuracy = (tp + tn) / total if total > 0 else 0
        precision = tp / (tp + fp) if (tp + fp) > 0 else 0
        recall = tp / (tp + fn) if (tp + fn) > 0 else 0
        f1 = 2 * precision * recall / (precision + recall) if (precision + recall) > 0 else 0

        fpr = fp / (fp + tn) if (fp + tn) > 0 else 0
        fnr = fn / (fn + tp) if (fn + tp) > 0 else 0

        print(f"\n总测试次数: {total}")
        print(f"唤醒词检测: {self.stats['wake_word_detected']} 次")

        if self.use_voiceprint:
            print(f"声纹验证通过: {self.stats['voice_verified']} 次")
            print(f"声纹验证拒绝: {self.stats['voice_rejected']} 次")

        print(f"\n混淆矩阵:")
        print(f"  {'':12} | 预测:通过 | 预测:拒绝")
        print(f"  {'-'*40}")
        print(f"  {'真实:通过':12} | {tp:^8} | {fn:^8}")
        print(f"  {'真实:拒绝':12} | {fp:^8} | {tn:^8}")

        print(f"\n性能指标:")
        print(f"  准确率 (Accuracy):  {accuracy:.2%}")
        print(f"  精确率 (Precision): {precision:.2%}")
        print(f"  召回率 (Recall):    {recall:.2%}")
        print(f"  F1 分数:            {f1:.2%}")
        print(f"  误报率 (FPR):       {fpr:.2%}")
        print(f"  漏报率 (FNR):       {fnr:.2%}")

        print("\n" + "="*60)

        # 评估
        print("\n📊 评估结论:")
        if accuracy >= 0.9:
            print("  ✅ 系统表现优秀！准确率达到90%以上")
        elif accuracy >= 0.8:
            print("  ⚠️ 系统表现良好，但仍有改进空间")
        elif accuracy >= 0.7:
            print("  ⚠️ 系统表现一般，建议补充更多训练数据")
        else:
            print("  ❌ 系统表现较差，需要重新训练或调整参数")


def main():
    parser = argparse.ArgumentParser(description="唤醒词检测测试工具（支持声纹验证）")
    parser.add_argument("--model", "-m", type=str,
                        default="models/wake_word_model.keras",
                        help="唤醒词模型路径")
    parser.add_argument("--mode", type=str, default="interactive",
                        choices=["interactive", "continuous"],
                        help="测试模式")
    parser.add_argument("--voiceprint", "-v", action="store_true",
                        help="启用声纹验证（需先使用 enroll_speaker.py 注册）")
    parser.add_argument("--positive", "-p", type=int, default=5,
                        help="正样本测试次数")
    parser.add_argument("--negative", "-n", type=int, default=5,
                        help="负样本测试次数")
    parser.add_argument("--wake-threshold", type=float, default=0.7,
                        help="唤醒词检测阈值 (0.0-1.0, 默认 0.7)")
    parser.add_argument("--voice-threshold", type=float, default=0.70,
                        help="声纹验证阈值 (0.0-1.0)")
    parser.add_argument("--duration", "-d", type=float, default=2.0,
                        help="每次录音时长（秒）")

    args = parser.parse_args()

    # 检查模型文件
    if not os.path.exists(args.model):
        print(f"错误: 模型文件不存在: {args.model}")
        print("请先运行 python model_demo.py 训练模型")
        sys.exit(1)

    # 创建测试器
    tester = WakeWordTester(
        model_path=args.model,
        wake_threshold=args.wake_threshold,
        voice_threshold=args.voice_threshold,
        use_voiceprint=args.voiceprint,
        chunk_duration=args.duration
    )

    try:
        tester.start_listening()
        mode_str = "声纹验证已启用" if args.voiceprint else "仅唤醒词检测"
        print(f"\n🎤 麦克风已启动 ({mode_str})")

        if args.mode == "interactive":
            tester.interactive_test(
                num_positive=args.positive,
                num_negative=args.negative
            )
        else:
            tester.continuous_test()

    except Exception as e:
        print(f"错误: {e}")
        import traceback
        traceback.print_exc()
    finally:
        tester.stop_listening()
        print("\n测试完成！")


if __name__ == "__main__":
    main()
