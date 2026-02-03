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
唤醒词训练工具 v3 - 精准版

支持区分完整唤醒词和部分唤醒词（如区分"小云小云"和"小云"）
"""

import os
import time
import argparse
import numpy as np
import pyaudio
import soundfile as sf

# 录音参数
SAMPLE_RATE = 16000
CHANNELS = 1
FORMAT = pyaudio.paFloat32
DURATION = 2.0


def record_audio(duration: float = DURATION) -> np.ndarray:
    """录制一段音频"""
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


def augment_audio(audio: np.ndarray, num_augments: int = 10) -> list:
    """数据增强（保守版）"""
    augmented = [audio.copy()]

    for i in range(num_augments):
        aug = audio.copy()
        aug_type = i % 3

        if aug_type == 0:
            # 音量变化
            scale = np.random.uniform(0.8, 1.2)
            aug = aug * scale

        elif aug_type == 1:
            # 添加轻微噪声
            noise = np.random.randn(len(aug)).astype(np.float32) * 0.008
            aug = aug + noise

        else:
            # 轻微时移
            shift = np.random.randint(-400, 400)
            aug = np.roll(aug, shift)

        aug = np.clip(aug.astype(np.float32), -1.0, 1.0)
        augmented.append(aug)

    return augmented


def main():
    parser = argparse.ArgumentParser(description="唤醒词训练 v3 - 精准版")
    parser.add_argument("--wake-samples", "-w", type=int, default=5,
                        help="完整唤醒词录制次数 (默认 5)")
    parser.add_argument("--partial-samples", "-p", type=int, default=5,
                        help="部分唤醒词录制次数 (默认 5)")
    parser.add_argument("--other-samples", "-o", type=int, default=5,
                        help="其他词语录制次数 (默认 5)")
    parser.add_argument("--duration", "-d", type=float, default=2.0,
                        help="每次录制时长 (秒)")

    args = parser.parse_args()

    print("=" * 60)
    print("       KVoice 唤醒词训练 v3 (精准版)")
    print("=" * 60)
    print(f"  完整唤醒词: {args.wake_samples} 次 (如: 小云小云)")
    print(f"  部分唤醒词: {args.partial_samples} 次 (如: 小云) [作为负样本]")
    print(f"  其他词语:   {args.other_samples} 次 (如: 你好)")
    print("=" * 60)

    # 创建目录
    os.makedirs("voice", exist_ok=True)
    os.makedirs("negative", exist_ok=True)
    os.makedirs("models", exist_ok=True)

    # ========== 第一步：录制完整唤醒词 ==========
    print("\n" + "=" * 60)
    print("[Step 1/4] Recording FULL wake word")
    print("=" * 60)
    print('Say the COMPLETE wake word, like "小云小云"')
    print("[IMPORTANT] Say it COMPLETELY every time!")

    wake_samples = []
    input("\nPress Enter to start...")

    for i in range(args.wake_samples):
        print(f"\n  [{i+1}/{args.wake_samples}] Say FULL wake word (e.g., 小云小云):")
        for j in range(3, 0, -1):
            print(f"    {j}...", end=" ", flush=True)
            time.sleep(0.5)
        print()

        audio = record_audio(args.duration)
        wake_samples.append(audio)
        sf.write(f"voice/wake_full_{i+1:02d}.wav", audio, SAMPLE_RATE)

        if i < args.wake_samples - 1:
            input("  Press Enter for next...")

    print(f"\n  [OK] Full wake word: {len(wake_samples)} samples")

    # ========== 第二步：录制部分唤醒词（负样本！） ==========
    print("\n" + "=" * 60)
    print("[Step 2/4] Recording PARTIAL wake word (as NEGATIVE!)")
    print("=" * 60)
    print('Say only PART of the wake word, like just "小云"')
    print("[IMPORTANT] This trains the model to REJECT partial matches!")

    partial_samples = []
    input("\nPress Enter to start...")

    for i in range(args.partial_samples):
        print(f"\n  [{i+1}/{args.partial_samples}] Say PARTIAL wake word (e.g., just 小云):")
        for j in range(3, 0, -1):
            print(f"    {j}...", end=" ", flush=True)
            time.sleep(0.5)
        print()

        audio = record_audio(args.duration)
        partial_samples.append(audio)
        sf.write(f"negative/partial_{i+1:02d}.wav", audio, SAMPLE_RATE)

        if i < args.partial_samples - 1:
            input("  Press Enter for next...")

    print(f"\n  [OK] Partial wake word (negative): {len(partial_samples)} samples")

    # ========== 第三步：录制其他词语（负样本） ==========
    print("\n" + "=" * 60)
    print("[Step 3/4] Recording OTHER words (as NEGATIVE)")
    print("=" * 60)
    print('Say various other words: "你好", "今天", "打开", etc.')

    other_samples = []
    input("\nPress Enter to start...")

    for i in range(args.other_samples):
        print(f"\n  [{i+1}/{args.other_samples}] Say any OTHER word:")
        for j in range(3, 0, -1):
            print(f"    {j}...", end=" ", flush=True)
            time.sleep(0.5)
        print()

        audio = record_audio(args.duration)
        other_samples.append(audio)
        sf.write(f"negative/other_{i+1:02d}.wav", audio, SAMPLE_RATE)

        if i < args.other_samples - 1:
            input("  Press Enter for next...")

    # 添加静音
    print("\n  Adding silence samples...")
    silence_samples = []
    for i in range(3):
        silence = np.zeros(int(SAMPLE_RATE * args.duration), dtype=np.float32)
        silence += np.random.randn(len(silence)).astype(np.float32) * 0.003
        silence_samples.append(silence)
        sf.write(f"negative/silence_{i+1:02d}.wav", silence, SAMPLE_RATE)

    all_negative = partial_samples + other_samples + silence_samples
    print(f"\n  [OK] Total negative samples: {len(all_negative)}")

    # ========== 第四步：训练模型 ==========
    print("\n" + "=" * 60)
    print("[Step 4/4] Training Model")
    print("=" * 60)

    from audio_utils import preprocess_audio
    from feature_extractor import extract_features
    from model import create_wake_word_model, compile_model
    from sklearn.model_selection import train_test_split
    from tensorflow import keras

    # 数据增强
    print("  Augmenting data...")
    
    # 正样本增强
    all_positive = []
    for sample in wake_samples:
        augmented = augment_audio(sample, 12)
        all_positive.extend(augmented)
    print(f"    Positive: {len(wake_samples)} -> {len(all_positive)}")

    # 负样本增强（部分唤醒词多增强一些）
    all_negative_aug = []
    for sample in partial_samples:
        augmented = augment_audio(sample, 15)  # 部分唤醒词增强更多
        all_negative_aug.extend(augmented)
    for sample in other_samples + silence_samples:
        augmented = augment_audio(sample, 8)
        all_negative_aug.extend(augmented)
    print(f"    Negative: {len(all_negative)} -> {len(all_negative_aug)}")

    # 准备数据
    X = []
    y = []

    print("  Extracting features...")
    for audio in all_positive:
        processed = preprocess_audio(audio)
        features = extract_features(processed)
        X.append(features)
        y.append(1)

    for audio in all_negative_aug:
        processed = preprocess_audio(audio)
        features = extract_features(processed)
        X.append(features)
        y.append(0)

    X = np.array(X)
    y = np.array(y)

    print(f"\n  Dataset Summary:")
    print(f"    Total:    {len(y)}")
    print(f"    Positive: {sum(y)} (full wake word)")
    print(f"    Negative: {len(y) - sum(y)} (partial + others)")

    # 划分数据集
    X_train, X_val, y_train, y_val = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y
    )

    # 创建和训练模型
    model = create_wake_word_model(input_shape=X[0].shape)
    model = compile_model(model, learning_rate=0.0003)

    print("\n  Training...")
    callbacks = [
        keras.callbacks.EarlyStopping(
            monitor='val_loss',
            patience=20,
            restore_best_weights=True
        ),
        keras.callbacks.ReduceLROnPlateau(
            monitor='val_loss',
            factor=0.5,
            patience=8,
            min_lr=1e-6
        )
    ]

    history = model.fit(
        X_train, y_train,
        validation_data=(X_val, y_val),
        epochs=150,
        batch_size=16,
        callbacks=callbacks,
        verbose=1
    )

    # 评估
    loss, accuracy = model.evaluate(X_val, y_val, verbose=0)
    print(f"\n  Validation Accuracy: {accuracy:.1%}")

    # 保存模型
    model_path = "models/wake_word_model.keras"
    model.save(model_path)
    print(f"  Model saved: {model_path}")

    # 完成
    print("\n" + "=" * 60)
    print("  TRAINING COMPLETE!")
    print("=" * 60)
    print(f"\n  Accuracy: {accuracy:.1%}")
    print(f"\n  Test command:")
    print(f"    python wake_word_test.py --wake-threshold 0.8 --mode continuous")
    print("\n  [TIP] If '小云' still triggers, try threshold 0.85 or 0.9")
    print("=" * 60)


if __name__ == "__main__":
    main()
