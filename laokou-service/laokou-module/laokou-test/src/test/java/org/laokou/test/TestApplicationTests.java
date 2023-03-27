package org.laokou.test;

import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.rocketmq.dto.RocketmqDTO;
import org.laokou.common.rocketmq.support.RocketTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApplication.class)
class TestApplicationTests {

    @Autowired
    private RocketTemplate rocketTemplate;

    @Test
    void contextLoads() {
        rocketTemplate.sendAsyncMessage("test",new RocketmqDTO());
        rocketTemplate.sendTransactionMessage("test",new RocketmqDTO(),String.valueOf(IdGenerator.defaultSnowflakeId()));
    }

}
