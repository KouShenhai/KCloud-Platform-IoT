package org.laokou.api.server;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.laokou.common.core.constant.Constant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ApiApplicationTests {

	private final MockMvc mockMvc;

	@Test
	void testGet() throws Exception {
		String appKey = "laokou2023";
		String appSecret = "vb05f6c45d67340zaz95v7fa6d49v99zx";
		long timestamp = System.currentTimeMillis();
		String nonce = UUID.randomUUID().toString();
		mockMvc.perform(get("/get").header(APP_KEY, appKey).header(APP_SECRET, appSecret)
				.header(TIMESTAMP, String.valueOf(timestamp)).header(NONCE, nonce)
				.header(SIGN, sign(appKey, appSecret, nonce, timestamp, ""))).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	private static String sign(String appKey, String appSecret, String nonce, long timestamp, String params) {
		String str = appKey + appSecret + nonce + timestamp + params;
		return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
	}

}
