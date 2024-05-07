/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.crypto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.HttpUtil;
import org.laokou.common.core.utils.JacksonUtil;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author laokou
 */
@Slf4j
public class GYSGApiTest {

	public static void main(String[] args) {
		HashMap<String, String> params = new HashMap<>(2);
		String key = "";
		String cx = "";
		params.put("key", key);
		params.put("cx", cx);
		String result = HttpUtil.doGet("https://api.t1qq.com/api/sky/sc/sg", params, Collections.emptyMap(), true);
		Result res = JacksonUtil.toBean(result, Result.class);
		Result.Action action = res.action;
		Result.Adorn adorn = res.adorn;
		Result.Data data = res.data;
		String str = """
				\n亲爱的光崽，你好！
				-----基本信息-----
				当前身高：{}
				最大身高：{}
				最小身高：{}
				体型值：{}
				身高：{}
				-----装扮信息-----
				斗篷：{}
				道具：{}
				脖子：{}
				面具：{}
				耳朵：{}
				头发：{}
				裤子：{}
				-----动作信息-----
				声音：{}
				站姿：{}
				""";
		log.info(str, data.currentHeight, data.maxHeight, data.minHeight, data.scale, data.height, adorn.cloak,
				adorn.prop, adorn.neck, adorn.mask, adorn.horn, adorn.hair, adorn.pants, action.voice, action.attitude);
	}

	@Data
	static class Result {

		private Integer code;

		private String time;

		private Data data;

		private Adorn adorn;

		private Action action;

		@lombok.Data
		static class Data {

			private String scale;

			private String height;

			private String currentHeight;

			private String maxHeight;

			private String minHeight;

		}

		@lombok.Data
		static class Adorn {

			private String cloak;

			private String prop;

			private String neck;

			private String mask;

			private String horn;

			private String hair;

			private String pants;

		}

		@lombok.Data
		static class Action {

			private String voice;

			private String attitude;

		}

	}

}
