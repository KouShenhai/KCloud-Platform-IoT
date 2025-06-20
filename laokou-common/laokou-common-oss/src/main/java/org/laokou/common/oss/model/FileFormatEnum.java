/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.common.oss.model;

import lombok.Getter;

import java.util.List;

@Getter
public enum FileFormatEnum {

	VIDEO("video", "视频") {
		@Override
		public List<String> getExtNames() {
			return List.of(".mp4", ".avi", ".mkv", ".wmv", ".mov", ".flv", ".m3u8");
		}
	},

	AUDIO("audio", "音频") {
		@Override
		public List<String> getExtNames() {
			return List.of(".mp3", ".wav", ".aac", ".flac", ".ogg");
		}
	},

	IMAGE("image", "图片") {
		@Override
		public List<String> getExtNames() {
			return List.of(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp");
		}
	};

	private final String code;

	private final String desc;

	FileFormatEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract List<String> getExtNames();

}
