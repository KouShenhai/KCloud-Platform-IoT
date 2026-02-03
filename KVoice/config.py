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
语音认证系统配置参数
"""

from pathlib import Path

# ============ 路径配置 ============
BASE_DIR = Path(__file__).parent.absolute()
USERS_DIR = BASE_DIR / "users"  # 用户声纹数据存储目录
USERS_FILE = USERS_DIR / "registered_users.json"  # 注册用户信息文件

# 确保目录存在
USERS_DIR.mkdir(exist_ok=True)

# ============ 音频配置 ============
SAMPLE_RATE = 16000  # 采样率 (Hz)
CHANNELS = 1  # 单声道
CHUNK_SIZE = 1024  # 音频块大小
RECORD_SECONDS = 3  # 默认录音时长 (秒)
FORMAT_BITS = 16  # 位深度

# ============ 唤醒词配置 ============
WAKE_WORD = "你好小寇"  # 唤醒词
WAKE_WORD_VARIANTS = [
    "你好小寇",
    "你好 小寇",
    "你好，小寇",
    "你好,小寇",
    "nihao xiaokou",
    "ni hao xiao kou",
]

# ============ 声纹识别配置 ============
VOICEPRINT_SIMILARITY_THRESHOLD = 0.70  # 声纹相似度阈值 (0-1)
VOICEPRINT_EMBEDDING_DIM = 256  # 声纹嵌入向量维度
REGISTRATION_SAMPLES = 3  # 注册时录制的语音样本数

# ============ Whisper ASR 配置 ============
WHISPER_MODEL = "base"  # Whisper 模型大小: tiny, base, small, medium, large
WHISPER_LANGUAGE = "zh"  # 语言: 中文

# ============ VAD (语音活动检测) 配置 ============
VAD_MODE = 3  # WebRTC VAD 模式 (0-3, 3 最激进)
VAD_FRAME_DURATION = 30  # VAD 帧时长 (毫秒)
MIN_SPEECH_DURATION = 0.5  # 最小语音持续时间 (秒)
MAX_SILENCE_DURATION = 1.0  # 最大静音持续时间 (秒)

# ============ 测试配置 ============
ENABLE_DEBUG = True  # 是否启用调试输出
