/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.api.server;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.laokou.api.client.ParamDTO;
import org.laokou.api.server.service.OpenApiService;
import org.laokou.common.core.utils.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */

@SpringBootTest(classes = {ApiApplication.class})
@RunWith(SpringRunner.class)
@Slf4j
public class ApiServerTests {

    @Autowired
    private OpenApiService openApiService;

    @Test
    public void testPost() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("cmd","tts");
        params.set("text", URLEncoder.encode("只有热爱才是最好的11111老师", StandardCharsets.UTF_8));
        params.set("voice","xiaoyun");
        params.set("volume","100");
        params.set("speech","0");
        params.set("pitch","0");
        ParamDTO dto = new ParamDTO();
        dto.setUuid("laokou-api-001");
        dto.setParams(params);
        dto.setUri("http://www.yayapeiyin.com/api.php");
        Mono<String> stringMono = openApiService.doPost(dto);
        log.info("返回结果:{}", JacksonUtil.readTree(stringMono.block()).get("data").get("uri").asText());
    }

    @Test
    public void testGet() {
        String stringBlock = openApiService.doGet(String.format("https://tts.baidu.com/text2audio.mp3?cuid=baike&lan=ZH&ctp=2&pdt=301&vol=100&rate=32&per=3&spd=4&pit=5.mp3&tex=%s", URLEncoder.encode("哈哈哈哈哈",StandardCharsets.UTF_8))).block();
        log.info("返回结果：{}",stringBlock);
    }

}
