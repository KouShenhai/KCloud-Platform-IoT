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
语音唤醒主程序

双重验证：
1. 唤醒词匹配 (DTW 模板匹配)
2. 声纹验证 (Resemblyzer)

只有说对唤醒词 + 是注册用户 才能唤醒
"""

import time
import argparse
import numpy as np
import pyaudio
from queue import Queue
from datetime import datetime

from wake_word_matcher import WakeWordMatcher
from speaker_manager import SpeakerManager


# 音频参数
SAMPLE_RATE = 16000
CHANNELS = 1
FORMAT = pyaudio.paFloat32
CHUNK_DURATION = 2.0  # 检测窗口（秒）


class VoiceWakeDetector:
    """
    语音唤醒检测器
    
    双重验证：唤醒词 + 声纹
    """
    
    def __init__(self,
                 wake_threshold: float = None,
                 voice_threshold: float = 0.80,
                 chunk_duration: float = 2.0,
                 overlap: float = 0.5):
        """
        初始化检测器
        
        Args:
            wake_threshold: 唤醒词阈值 (None=使用保存的值)
            voice_threshold: 声纹阈值
            chunk_duration: 检测窗口时长（秒）
            overlap: 窗口重叠比例
        """
        self.chunk_duration = chunk_duration
        self.overlap = overlap
        self.voice_threshold = voice_threshold
        
        # 计算采样点数
        self.chunk_samples = int(SAMPLE_RATE * chunk_duration)
        self.step_samples = int(self.chunk_samples * (1 - overlap))
        
        # 初始化唤醒词匹配器
        print("Loading wake word matcher...")
        self.wake_matcher = WakeWordMatcher()
        
        if wake_threshold is not None:
            self.wake_matcher.set_threshold(wake_threshold)
        
        wake_info = self.wake_matcher.get_info()
        if wake_info['num_templates'] == 0:
            print("[WARN] No wake word templates! Run 'python enroll.py' first.")
        else:
            print(f"  Templates: {wake_info['num_templates']}")
            print(f"  Threshold: {wake_info['threshold']:.2f}")
        
        # 初始化声纹管理器
        print("\nLoading speaker manager...")
        self.speaker_manager = SpeakerManager(threshold=voice_threshold)
        
        speakers = self.speaker_manager.list_speakers()
        if len(speakers) == 0:
            print("[WARN] No voiceprints! Run 'python enroll.py' first.")
        else:
            print(f"  Speakers: {', '.join(speakers)}")
        
        # PyAudio
        self.audio = pyaudio.PyAudio()
        self.stream = None
        
        # 音频缓冲
        self.audio_buffer = np.array([], dtype=np.float32)
        self.audio_queue = Queue()
        
        # 状态
        self.is_running = False
        self.last_wake_time = 0
        self.cooldown = 2.0  # 唤醒冷却时间（秒）
        
        # 统计
        self.stats = {
            'wake_attempts': 0,
            'wake_success': 0,
            'voice_verified': 0,
            'voice_rejected': 0
        }
    
    def start(self):
        """开始监听"""
        self.stream = self.audio.open(
            format=FORMAT,
            channels=CHANNELS,
            rate=SAMPLE_RATE,
            input=True,
            frames_per_buffer=1024,
            stream_callback=self._audio_callback
        )
        self.is_running = True
        self.stream.start_stream()
        print("\n[OK] Listening started...")
    
    def stop(self):
        """停止监听"""
        self.is_running = False
        if self.stream:
            self.stream.stop_stream()
            self.stream.close()
        self.audio.terminate()
    
    def _audio_callback(self, in_data, frame_count, time_info, status):
        """音频回调"""
        if self.is_running:
            audio_data = np.frombuffer(in_data, dtype=np.float32)
            self.audio_queue.put(audio_data)
        return (None, pyaudio.paContinue)
    
    def process(self) -> dict:
        """
        处理音频并检测唤醒
        
        Returns:
            检测结果字典，或 None
        """
        # 收集音频
        while not self.audio_queue.empty():
            chunk = self.audio_queue.get()
            self.audio_buffer = np.concatenate([self.audio_buffer, chunk])
        
        # 缓冲区足够长时进行检测
        if len(self.audio_buffer) >= self.chunk_samples:
            audio_chunk = self.audio_buffer[:self.chunk_samples].copy()
            self.audio_buffer = self.audio_buffer[self.step_samples:]
            
            result = {
                'wake_match': False,
                'wake_distance': float('inf'),
                'voice_verified': False,
                'voice_similarity': 0.0,
                'speaker_name': '',
                'success': False
            }
            
            # 第一步：唤醒词匹配
            wake_match, wake_distance = self.wake_matcher.match(audio_chunk)
            result['wake_match'] = wake_match
            result['wake_distance'] = wake_distance
            
            if not wake_match:
                return result
            
            self.stats['wake_attempts'] += 1
            
            # 第二步：声纹验证
            verified, speaker, similarity = self.speaker_manager.verify_speaker(audio_chunk)
            result['voice_verified'] = verified
            result['voice_similarity'] = similarity
            result['speaker_name'] = speaker
            
            if verified:
                self.stats['voice_verified'] += 1
            else:
                self.stats['voice_rejected'] += 1
            
            # 双重验证通过
            result['success'] = wake_match and verified
            
            if result['success']:
                self.stats['wake_success'] += 1
            
            return result
        
        return None
    
    def run_continuous(self):
        """运行连续检测"""
        print("\n" + "=" * 60)
        print("CONTINUOUS DETECTION MODE")
        print("=" * 60)
        print("Say your wake word. Press Ctrl+C to stop.")
        print("-" * 60)
        
        try:
            while self.is_running:
                result = self.process()
                
                if result and result['wake_match']:
                    current_time = time.time()
                    
                    # 冷却检查
                    if current_time - self.last_wake_time < self.cooldown:
                        continue
                    
                    timestamp = datetime.now().strftime("%H:%M:%S")
                    
                    if result['success']:
                        self.last_wake_time = current_time
                        print(f"[{timestamp}] WAKE SUCCESS!")
                        print(f"           User: {result['speaker_name']}")
                        print(f"           Wake distance: {result['wake_distance']:.2f}")
                        print(f"           Voice similarity: {result['voice_similarity']:.1%}")
                        print()
                    else:
                        if result['voice_verified']:
                            # 声纹对但唤醒词不对（理论上不会到这里）
                            pass
                        else:
                            # 唤醒词对但声纹不对
                            print(f"[{timestamp}] REJECTED - Voice not verified")
                            print(f"           Voice similarity: {result['voice_similarity']:.1%}")
                            print()
                
                time.sleep(0.05)
        
        except KeyboardInterrupt:
            print("\n\nStopping...")
            self._print_stats()
    
    def _print_stats(self):
        """打印统计"""
        print("\n" + "=" * 60)
        print("STATISTICS")
        print("=" * 60)
        print(f"  Wake word matches: {self.stats['wake_attempts']}")
        print(f"  Voice verified:    {self.stats['voice_verified']}")
        print(f"  Voice rejected:    {self.stats['voice_rejected']}")
        print(f"  Successful wakes:  {self.stats['wake_success']}")
        print("=" * 60)


def main():
    parser = argparse.ArgumentParser(description="语音唤醒检测")
    parser.add_argument("--wake-threshold", "-w", type=float, default=None,
                        help="唤醒词阈值 (越小越严格, 推荐 1.5-3.0)")
    parser.add_argument("--voice-threshold", "-v", type=float, default=0.80,
                        help="声纹阈值 (0.0-1.0, 默认 0.80)")
    parser.add_argument("--duration", "-d", type=float, default=2.0,
                        help="检测窗口时长 (秒)")

    args = parser.parse_args()

    print("=" * 60)
    print("       KVoice - Voice Wake Detection")
    print("=" * 60)

    detector = VoiceWakeDetector(
        wake_threshold=args.wake_threshold,
        voice_threshold=args.voice_threshold,
        chunk_duration=args.duration
    )

    try:
        detector.start()
        detector.run_continuous()
    finally:
        detector.stop()
        print("\n[OK] Detector stopped.")


if __name__ == "__main__":
    main()
