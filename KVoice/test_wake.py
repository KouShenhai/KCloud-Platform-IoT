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
唤醒词匹配测试 (带校准)

流程：
1. 录制 5 遍唤醒词作为模板
2. 录制 3 遍非唤醒词用于校准阈值
3. 交互式测试
"""

import time
import numpy as np
import pyaudio
from wake_word_matcher import WakeWordMatcher

# 录音参数
SAMPLE_RATE = 16000
FORMAT = pyaudio.paFloat32
DURATION = 2.0


def record_audio(duration: float = DURATION, prompt: str = "") -> np.ndarray:
    """录制音频"""
    audio = pyaudio.PyAudio()
    stream = audio.open(
        format=FORMAT,
        channels=1,
        rate=SAMPLE_RATE,
        input=True,
        frames_per_buffer=1024
    )

    if prompt:
        print(f"  {prompt}")
    
    # 倒计时
    for j in range(3, 0, -1):
        print(f"    {j}...", end=" ", flush=True)
        time.sleep(0.5)
    print("[Recording]", end=" ", flush=True)
    
    frames = []
    for _ in range(int(SAMPLE_RATE / 1024 * duration)):
        data = stream.read(1024, exception_on_overflow=False)
        frames.append(np.frombuffer(data, dtype=np.float32))
    print("Done!")

    stream.stop_stream()
    stream.close()
    audio.terminate()
    return np.concatenate(frames)


def main():
    print("=" * 60)
    print("       唤醒词测试 (带校准)")
    print("=" * 60)
    print('  唤醒词: "你好小云"')
    print("=" * 60)

    matcher = WakeWordMatcher()
    
    # 检查是否需要重新注册
    info = matcher.get_info()
    if info['num_templates'] > 0:
        print(f"\n已有 {info['num_templates']} 个模板，阈值 {info['threshold']:.2f}")
        choice = input("重新注册? (y/n, 默认 n): ").strip().lower()
        if choice == 'y':
            matcher.clear_templates()
        else:
            goto_test = True
    else:
        goto_test = False
    
    if info['num_templates'] == 0 or (choice if 'choice' in dir() else '') == 'y':
        goto_test = False
        
        # ===== 第一步：录制唤醒词模板 =====
        print("\n" + "=" * 60)
        print("[Step 1] 录制唤醒词模板")
        print("=" * 60)
        print('请说 5 遍 "你好小云"')
        input("\n按 Enter 开始...")

        wake_audios = []
        for i in range(5):
            print(f"\n  第 {i+1}/5 遍:")
            audio = record_audio(DURATION, '说 "你好小云"')
            matcher.enroll_template(audio, clear_existing=(i == 0))
            wake_audios.append(audio)
            
            if i < 4:
                input("  按 Enter 继续...")

        # ===== 第二步：录制非唤醒词用于校准 =====
        print("\n" + "=" * 60)
        print("[Step 2] 录制非唤醒词 (用于校准)")
        print("=" * 60)
        print('请说 3 个不同的词，如 "你好"、"小云"、"今天天气"')
        input("\n按 Enter 开始...")

        neg_audios = []
        neg_words = ["你好", "小云", "今天天气"]
        for i, word in enumerate(neg_words):
            print(f"\n  第 {i+1}/3 个:")
            audio = record_audio(DURATION, f'说 "{word}"')
            neg_audios.append(audio)
            
            if i < 2:
                input("  按 Enter 继续...")

        # ===== 第三步：校准阈值 =====
        print("\n" + "=" * 60)
        print("[Step 3] 校准阈值")
        print("=" * 60)
        
        matcher.calibrate_threshold(wake_audios, neg_audios)
    
    # ===== 测试模式 =====
    print("\n" + "=" * 60)
    print("[Test] 开始测试")
    print("=" * 60)
    print('说 "你好小云" 应该匹配成功')
    print('说其他词应该匹配失败')
    print("-" * 60)

    while True:
        cmd = input("\n按 Enter 录音测试 (输入 q 退出, t 调整阈值): ").strip().lower()
        
        if cmd == 'q':
            break
        elif cmd == 't':
            try:
                new_th = float(input(f"  当前阈值: {matcher.threshold:.2f}, 输入新阈值: "))
                matcher.set_threshold(new_th)
            except:
                pass
            continue

        print("\n请说话:")
        audio = record_audio(DURATION)

        # 匹配
        is_match, distance = matcher.match(audio)

        print(f"\n  距离: {distance:.3f}")
        print(f"  阈值: {matcher.threshold:.3f}")
        
        if is_match:
            print(f"  结果: [OK] 匹配成功! (距离 < 阈值)")
        else:
            print(f"  结果: [X] 匹配失败 (距离 >= 阈值)")

    print("\n测试结束!")


if __name__ == "__main__":
    main()
