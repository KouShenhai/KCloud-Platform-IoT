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
声纹注册工具

交互式录制音频样本并注册说话人声纹
"""

import os
import sys
import time
import argparse
import numpy as np
import pyaudio
from speaker_manager import SpeakerManager


# 录音参数
SAMPLE_RATE = 16000
CHANNELS = 1
FORMAT = pyaudio.paFloat32


def record_audio(duration: float = 2.0) -> np.ndarray:
    """
    录制音频

    Args:
        duration: 录制时长（秒）

    Returns:
        音频数据
    """
    audio = pyaudio.PyAudio()
    frames = []

    stream = audio.open(
        format=FORMAT,
        channels=CHANNELS,
        rate=SAMPLE_RATE,
        input=True,
        frames_per_buffer=1024
    )

    print("🎤 录制中...", end=" ", flush=True)

    num_chunks = int(SAMPLE_RATE / 1024 * duration)
    for _ in range(num_chunks):
        data = stream.read(1024, exception_on_overflow=False)
        frames.append(np.frombuffer(data, dtype=np.float32))

    print("完成!")

    stream.stop_stream()
    stream.close()
    audio.terminate()

    return np.concatenate(frames)


def show_progress_bar(current: int, total: int, width: int = 30):
    """显示进度条"""
    progress = current / total
    filled = int(width * progress)
    bar = "█" * filled + "░" * (width - filled)
    print(f"\r进度: [{bar}] {current}/{total}", end="")


def main():
    parser = argparse.ArgumentParser(description="声纹注册工具")
    parser.add_argument("--name", "-n", type=str, required=True,
                        help="说话人名称（必填）")
    parser.add_argument("--samples", "-s", type=int, default=5,
                        help="录制样本数量（默认5个）")
    parser.add_argument("--duration", "-d", type=float, default=2.0,
                        help="每个样本录制时长（秒）")
    parser.add_argument("--overwrite", "-o", action="store_true",
                        help="覆盖已存在的声纹")
    parser.add_argument("--list", "-l", action="store_true",
                        help="列出已注册的说话人")
    parser.add_argument("--delete", type=str, default=None,
                        help="删除指定说话人")

    args = parser.parse_args()

    # 初始化管理器
    manager = SpeakerManager()

    # 列出已注册说话人
    if args.list:
        speakers = manager.list_speakers()
        if speakers:
            print("已注册的说话人:")
            for i, name in enumerate(speakers, 1):
                info = manager.get_speaker_info(name)
                enrolled_at = info.get('enrolled_at', '未知')
                num_samples = info.get('num_samples', '未知')
                print(f"  {i}. {name} (样本数: {num_samples}, 注册时间: {enrolled_at})")
        else:
            print("还没有注册任何说话人")
        return

    # 删除说话人
    if args.delete:
        manager.delete_speaker(args.delete)
        return

    # 检查是否已存在
    if args.name in manager.list_speakers() and not args.overwrite:
        print(f"说话人 '{args.name}' 已存在!")
        print("使用 --overwrite 参数覆盖，或选择其他名称")
        sys.exit(1)

    print("=" * 60)
    print("声纹注册")
    print("=" * 60)
    print(f"说话人: {args.name}")
    print(f"样本数: {args.samples}")
    print(f"每个样本时长: {args.duration}秒")
    print("-" * 60)
    print("提示: 请在安静的环境中清晰地说出唤醒词")
    print("      每次录制后会有提示，请按Enter继续")
    print("=" * 60)

    input("\n准备好后按 Enter 开始录制...")

    audio_samples = []

    for i in range(args.samples):
        print(f"\n[{i+1}/{args.samples}] 请说出唤醒词:")

        # 倒计时
        for j in range(3, 0, -1):
            print(f"  {j}...", end=" ", flush=True)
            time.sleep(0.5)
        print()

        # 录制
        audio = record_audio(args.duration)
        audio_samples.append(audio)

        # 显示进度
        show_progress_bar(i + 1, args.samples)

        if i < args.samples - 1:
            input("\n按 Enter 继续下一个...")

    print("\n\n" + "=" * 60)
    print("正在处理声纹...")

    # 注册声纹
    success = manager.enroll_speaker(
        name=args.name,
        audio_samples=audio_samples,
        overwrite=args.overwrite
    )

    if success:
        print("=" * 60)
        print(f"✅ 声纹注册成功!")
        print(f"   说话人: {args.name}")
        print(f"   样本数: {len(audio_samples)}")
        print("=" * 60)
        print("\n提示: 现在可以使用 wake_word_test.py --voiceprint 进行测试")
    else:
        print("❌ 声纹注册失败!")
        sys.exit(1)


if __name__ == "__main__":
    main()
