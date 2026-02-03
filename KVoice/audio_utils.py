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
音频处理工具模块
提供录音、音频加载、预处理等功能
"""

import wave
from pathlib import Path
from typing import Optional, Tuple, Union

import numpy as np

try:
    import pyaudio
    PYAUDIO_AVAILABLE = True
except ImportError:
    PYAUDIO_AVAILABLE = False
    print("Warning: PyAudio not installed. Real-time recording disabled.")

try:
    import webrtcvad
    VAD_AVAILABLE = True
except ImportError:
    VAD_AVAILABLE = False
    print("Warning: webrtcvad not installed. VAD features disabled.")

from config import (
    SAMPLE_RATE, CHANNELS, CHUNK_SIZE, RECORD_SECONDS,
	VAD_MODE, VAD_FRAME_DURATION,
    MIN_SPEECH_DURATION, MAX_SILENCE_DURATION
)


class AudioRecorder:
    """实时音频录制器"""

    def __init__(self):
        if not PYAUDIO_AVAILABLE:
            raise RuntimeError("PyAudio is required for recording. Install with: pip install pyaudio")
        self.audio = pyaudio.PyAudio()
        self.stream = None

    def __del__(self):
        self.close()

    def close(self):
        """关闭音频资源"""
        if self.stream:
            self.stream.stop_stream()
            self.stream.close()
            self.stream = None
        if hasattr(self, 'audio') and self.audio:
            self.audio.terminate()

    def record(self, duration: float = RECORD_SECONDS,
               show_progress: bool = True) -> np.ndarray:
        """
        录制指定时长的音频

        Args:
            duration: 录音时长(秒)
            show_progress: 是否显示进度

        Returns:
            音频数据 (numpy array, float32, normalized)
        """
        self.stream = self.audio.open(
            format=pyaudio.paInt16,
            channels=CHANNELS,
            rate=SAMPLE_RATE,
            input=True,
            frames_per_buffer=CHUNK_SIZE
        )

        frames = []
        num_chunks = int(SAMPLE_RATE / CHUNK_SIZE * duration)

        if show_progress:
            print(f"🎤 开始录音 ({duration}秒)...")

        for i in range(num_chunks):
            data = self.stream.read(CHUNK_SIZE, exception_on_overflow=False)
            frames.append(data)

            if show_progress and (i + 1) % int(num_chunks / 10) == 0:
                progress = (i + 1) / num_chunks * 100
                print(f"   录音进度: {progress:.0f}%", end='\r')

        if show_progress:
            print("\n✅ 录音完成!")

        self.stream.stop_stream()
        self.stream.close()
        self.stream = None

        # 转换为 numpy array
        audio_data = b''.join(frames)
        audio_array = np.frombuffer(audio_data, dtype=np.int16)

        # 归一化到 [-1, 1]
        audio_array = audio_array.astype(np.float32) / 32768.0

        return audio_array

    def record_with_vad(self, max_duration: float = 10.0,
                        min_duration: float = MIN_SPEECH_DURATION,
                        silence_timeout: float = MAX_SILENCE_DURATION) -> Optional[np.ndarray]:
        """
        使用 VAD (语音活动检测) 录制音频
        当检测到静音超过阈值时自动停止

        Args:
            max_duration: 最大录音时长
            min_duration: 最小语音持续时间
            silence_timeout: 静音超时时间

        Returns:
            音频数据，如果未检测到语音则返回 None
        """
        if not VAD_AVAILABLE:
            print("Warning: VAD not available, using fixed duration recording")
            return self.record(duration=max_duration)

        vad = webrtcvad.Vad(VAD_MODE)

        self.stream = self.audio.open(
            format=pyaudio.paInt16,
            channels=CHANNELS,
            rate=SAMPLE_RATE,
            input=True,
            frames_per_buffer=CHUNK_SIZE
        )

        frames = []
        speech_frames = 0
        silence_frames = 0
        frame_duration = CHUNK_SIZE / SAMPLE_RATE
        min_speech_frames = int(min_duration / frame_duration)
        max_silence_frames = int(silence_timeout / frame_duration)
        max_frames = int(max_duration / frame_duration)

        print("🎤 开始录音 (检测到静音后自动停止)...")

        for i in range(max_frames):
            data = self.stream.read(CHUNK_SIZE, exception_on_overflow=False)
            frames.append(data)

            # VAD 检测需要 16-bit PCM 数据
            is_speech = self._check_voice_activity(vad, data)

            if is_speech:
                speech_frames += 1
                silence_frames = 0
            else:
                if speech_frames >= min_speech_frames:
                    silence_frames += 1
                    if silence_frames >= max_silence_frames:
                        print("\n✅ 检测到静音，录音结束")
                        break

        self.stream.stop_stream()
        self.stream.close()
        self.stream = None

        if speech_frames < min_speech_frames:
            print("⚠️ 未检测到足够的语音")
            return None

        # 转换为 numpy array
        audio_data = b''.join(frames)
        audio_array = np.frombuffer(audio_data, dtype=np.int16)
        audio_array = audio_array.astype(np.float32) / 32768.0

        return audio_array

    def _check_voice_activity(self, vad, audio_chunk: bytes) -> bool:
        """检查音频块是否包含语音"""
        try:
            # webrtcvad 需要特定帧长度的数据
            frame_size = int(SAMPLE_RATE * VAD_FRAME_DURATION / 1000) * 2
            if len(audio_chunk) >= frame_size:
                return vad.is_speech(audio_chunk[:frame_size], SAMPLE_RATE)
        except Exception:
            pass
        return False


def load_audio(file_path: Union[str, Path]) -> Tuple[np.ndarray, int]:
    """
    加载音频文件

    Args:
        file_path: 音频文件路径

    Returns:
        (音频数据, 采样率)
    """
    file_path = Path(file_path)

    if file_path.suffix.lower() == '.wav':
        return load_wav(file_path)
    else:
        # 使用 soundfile 支持更多格式
        try:
            import soundfile as sf
            audio, sr = sf.read(file_path)
            if len(audio.shape) > 1:
                audio = audio.mean(axis=1)  # 转换为单声道
            return audio.astype(np.float32), sr
        except ImportError:
            raise ValueError(f"Unsupported audio format: {file_path.suffix}")


def load_wav(file_path: Union[str, Path]) -> Tuple[np.ndarray, int]:
    """加载 WAV 文件"""
    with wave.open(str(file_path), 'rb') as wf:
        n_channels = wf.getnchannels()
        sample_width = wf.getsampwidth()
        sample_rate = wf.getframerate()
        n_frames = wf.getnframes()

        audio_data = wf.readframes(n_frames)

    # 根据位深度解析数据
    if sample_width == 2:
        audio = np.frombuffer(audio_data, dtype=np.int16)
        audio = audio.astype(np.float32) / 32768.0
    elif sample_width == 4:
        audio = np.frombuffer(audio_data, dtype=np.int32)
        audio = audio.astype(np.float32) / 2147483648.0
    else:
        raise ValueError(f"Unsupported sample width: {sample_width}")

    # 转换为单声道
    if n_channels > 1:
        audio = audio.reshape(-1, n_channels).mean(axis=1)

    return audio, sample_rate


def save_wav(audio: np.ndarray, file_path: Union[str, Path],
             sample_rate: int = SAMPLE_RATE):
    """
    保存音频为 WAV 文件

    Args:
        audio: 音频数据 (float32, -1 to 1)
        file_path: 保存路径
        sample_rate: 采样率
    """
    file_path = Path(file_path)
    file_path.parent.mkdir(parents=True, exist_ok=True)

    # 转换为 16-bit PCM
    audio_int16 = (audio * 32767).clip(-32768, 32767).astype(np.int16)

    with wave.open(str(file_path), 'wb') as wf:
        wf.setnchannels(CHANNELS)
        wf.setsampwidth(2)
        wf.setframerate(sample_rate)
        wf.writeframes(audio_int16.tobytes())


def preprocess_audio(audio: np.ndarray, target_sr: int = SAMPLE_RATE) -> np.ndarray:
    """
    音频预处理

    Args:
        audio: 输入音频
        target_sr: 目标采样率

    Returns:
        预处理后的音频
    """
    # 归一化
    if audio.max() > 1.0 or audio.min() < -1.0:
        audio = audio / max(abs(audio.max()), abs(audio.min()))

    # 去除直流偏移
    audio = audio - np.mean(audio)

    # 简单的预加重
    audio = np.append(audio[0], audio[1:] - 0.97 * audio[:-1])

    return audio.astype(np.float32)


def trim_silence(audio: np.ndarray, threshold: float = 0.01,
                 min_silence_len: int = 500) -> np.ndarray:
    """
    去除音频首尾静音

    Args:
        audio: 输入音频
        threshold: 静音阈值
        min_silence_len: 最小静音长度 (采样点)

    Returns:
        去除静音后的音频
    """
    # 计算能量
    energy = np.abs(audio)

    # 找到非静音区域
    mask = energy > threshold

    # 找到起始和结束位置
    nonzero = np.nonzero(mask)[0]
    if len(nonzero) == 0:
        return audio

    start = max(0, nonzero[0] - min_silence_len)
    end = min(len(audio), nonzero[-1] + min_silence_len)

    return audio[start:end]


def get_audio_duration(audio: np.ndarray, sample_rate: int = SAMPLE_RATE) -> float:
    """获取音频时长(秒)"""
    return len(audio) / sample_rate


if __name__ == "__main__":
    # 测试录音功能
    print("=== 音频工具测试 ===")

    recorder = AudioRecorder()

    print("\n1. 测试固定时长录音 (3秒)")
    audio = recorder.record(duration=3)
    print(f"   录制音频长度: {len(audio)} 采样点, {get_audio_duration(audio):.2f} 秒")

    # 保存测试
    test_file = Path(__file__).parent / "test_recording.wav"
    save_wav(audio, test_file)
    print(f"   已保存到: {test_file}")

    # 加载测试
    loaded_audio, sr = load_audio(test_file)
    print(f"   重新加载: {len(loaded_audio)} 采样点, 采样率 {sr}")

    recorder.close()
    print("\n✅ 测试完成!")
