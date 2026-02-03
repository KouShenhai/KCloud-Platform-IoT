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
一键注册工具

注册唤醒词模板 + 声纹
只需录几遍唤醒词，同时完成两项注册
"""

import os
import time
import argparse
import numpy as np
import pyaudio
import soundfile as sf
from wake_word_matcher import WakeWordMatcher
from speaker_manager import SpeakerManager

# 录音参数
SAMPLE_RATE = 16000
CHANNELS = 1
FORMAT = pyaudio.paFloat32

def record_audio(duration: float) -> np.ndarray:
    """录制音频"""
    audio = pyaudio.PyAudio()

    stream = audio.open(
        format=FORMAT,
        channels=CHANNELS,
        rate=SAMPLE_RATE,
        input=True,
        frames_per_buffer=1024
    )

    print("    [Recording]", end=" ", flush=True)
    frames = []
    num_chunks = int(SAMPLE_RATE / 1024 * duration)

    for _ in range(num_chunks):
        data = stream.read(1024, exception_on_overflow=False)
        frames.append(np.frombuffer(data, dtype=np.float32))

    print("Done!")

    stream.stop_stream()
    stream.close()
    audio.terminate()

    return np.concatenate(frames)


def main():
    parser = argparse.ArgumentParser(
        description="一键注册唤醒词 + 声纹",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Example:
  python enroll.py --name zhangsan --samples 5
  python enroll.py --name zhangsan --samples 3 --duration 2
        """
    )
    parser.add_argument("--name", "-n", type=str, required=True,
                        help="用户名称 (必填)")
    parser.add_argument("--samples", "-s", type=int, default=5,
                        help="录制次数 (默认 5)")
    parser.add_argument("--duration", "-d", type=float, default=2.0,
                        help="每次录制时长秒 (默认 2)")
    parser.add_argument("--calibrate", "-c", action="store_true",
                        help="录制后进行阈值校准")

    args = parser.parse_args()

    print("=" * 60)
    print("       KVoice 一键注册")
    print("=" * 60)
    print(f"  用户: {args.name}")
    print(f"  录制次数: {args.samples}")
    print(f"  每次时长: {args.duration}秒")
    print("-" * 60)
    print('  请每次都说完整的唤醒词： "你好小云"')
    print("=" * 60)

    # 初始化
    matcher = WakeWordMatcher()
    speaker_manager = SpeakerManager()

    # 检查是否已存在
    existing_speakers = speaker_manager.list_speakers()
    if args.name in existing_speakers:
        confirm = input(f"\n  User '{args.name}' exists. Overwrite? (y/n): ")
        if confirm.lower() != 'y':
            print("  Cancelled.")
            return

    os.makedirs("recordings", exist_ok=True)

    # ========== 录制唤醒词 ==========
    print("\n" + "=" * 60)
    print("Recording Wake Word")
    print("=" * 60)
    print('Say your wake word: "你好小云"')

    wake_audios = []
    input("\nPress Enter to start...")

    for i in range(args.samples):
        print(f"\n  [{i+1}/{args.samples}] Say wake word:")
        for j in range(3, 0, -1):
            print(f"    {j}...", end=" ", flush=True)
            time.sleep(0.5)
        print()

        audio = record_audio(args.duration)
        wake_audios.append(audio)

        # 保存录音
        sf.write(f"recordings/{args.name}_wake_{i+1:02d}.wav", audio, SAMPLE_RATE)

        if i < args.samples - 1:
            input("  Press Enter for next...")

    print(f"\n  [OK] Recorded {len(wake_audios)} samples")

    # ========== 注册唤醒词模板 ==========
    print("\n" + "=" * 60)
    print("Enrolling Wake Word Templates")
    print("=" * 60)

    # 清除旧模板，注册新模板
    for i, audio in enumerate(wake_audios):
        clear = (i == 0)  # 第一个时清除旧模板
        matcher.enroll_template(audio, clear_existing=clear)

    # ========== 注册声纹 ==========
    print("\n" + "=" * 60)
    print("Enrolling Voiceprint")
    print("=" * 60)

    success = speaker_manager.enroll_speaker(
        name=args.name,
        audio_samples=wake_audios,
        overwrite=True
    )

    if not success:
        print("  [ERROR] Failed to enroll voiceprint!")
        return

    # ========== 阈值校准（可选） ==========
    if args.calibrate:
        print("\n" + "=" * 60)
        print("Threshold Calibration")
        print("=" * 60)
        print("Now say some DIFFERENT words (NOT the wake word)")
        print("This helps set the right threshold")

        negative_audios = []
        input("\nPress Enter to record 3 non-wake words...")

        for i in range(3):
            print(f"\n  [{i+1}/3] Say a DIFFERENT word (not wake word):")
            for j in range(3, 0, -1):
                print(f"    {j}...", end=" ", flush=True)
                time.sleep(0.5)
            print()

            audio = record_audio(args.duration)
            negative_audios.append(audio)

            if i < 2:
                input("  Press Enter for next...")

        # 校准阈值
        print("\n  Calibrating threshold...")
        matcher.calibrate_threshold(wake_audios, negative_audios)

    # ========== 完成 ==========
    print("\n" + "=" * 60)
    print("  ENROLLMENT COMPLETE!")
    print("=" * 60)

    info = matcher.get_info()
    print(f"\n  Wake word templates: {info['num_templates']}")
    print(f"  Threshold: {info['threshold']:.2f}")
    print(f"  Voiceprint: {args.name}")

    print(f"\n  Test command:")
    print(f"    python voice_wake.py")
    print("=" * 60)


if __name__ == "__main__":
    main()
