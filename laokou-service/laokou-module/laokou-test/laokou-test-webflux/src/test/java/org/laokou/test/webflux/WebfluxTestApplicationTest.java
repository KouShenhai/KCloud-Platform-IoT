package org.laokou.test.webflux;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@Slf4j
@SpringBootTest(classes = WebfluxTestApplication.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class WebfluxTestApplicationTest {

    private final RedisUtil redisUtil;

    @Test
    public void test() {

        redisUtil.set("22",);
    }


}
