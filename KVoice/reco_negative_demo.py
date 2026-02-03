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

import os
import record
import soundfile as sf
import time
from audio_utils import normalize_audio
from datetime import datetime
if __name__ == '__main__':
	out_dir = 'negative'
	os.makedirs(out_dir, exist_ok=True)
	_recorder = record.AudioRecorder()
	_audios = []
	for i in range(30):
		print(f"录制第 {i+1} 个音频【非唤醒词】")
		_audio = normalize_audio(_recorder.record())
		timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
		file_name = f"negative_{timestamp}_{i:03d}.wav"
		file_path = os.path.join(out_dir, file_name)
		sf.write(file_path, _audio, _recorder.sample_rate)
		_audios.append(_audio)
		time.sleep(1)
	for _audio in _audios:
		_recorder.play(_audio)

