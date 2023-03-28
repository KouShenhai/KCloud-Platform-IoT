package org.laokou.test;

import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.rocketmq.dto.RocketmqDTO;
import org.laokou.common.rocketmq.support.RocketTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = TestApplication.class)
class TestApplicationTests {

//    @Autowired
//    private RocketTemplate rocketTemplate;

    @Test
    void contextLoads() {
//        RocketmqDTO dto = new RocketmqDTO();
//        List<String> list = new ArrayList<>(100);
//        for (int i = 0; i <= 100; i++) {
//            list.add("" + i);
//        }
//        dto.setBody(JacksonUtil.toJsonStr(list));
//        rocketTemplate.sendAsyncMessage("laokou-test",dto);
        RocketmqDTO rocketmqDTO = new RocketmqDTO();
        rocketmqDTO.setBody("333");
        RocketmqDTO rocketmqDTO1 = new RocketmqDTO();
        rocketmqDTO1.setBody("31111");
//        rocketTemplate.sendAsyncMessage("laokou-test",rocketmqDTO1);
//        rocketTemplate.sendAsyncMessage("laokou-test",rocketmqDTO1);
//        rocketTemplate.sendAsyncMessage("laokou-test",rocketmqDTO1);
//        rocketTemplate.sendSyncMessage("laokou-test",rocketmqDTO);
        //rocketTemplate.convertAndSendMessage("laokou-test",rocketmqDTO);
        //rocketTemplate.sendSyncMessage("laokou-test-2",new RocketmqDTO());
        //rocketTemplate.sendAndReceiveMessage("laokou-test-2",new RocketmqDTO(),RocketmqDTO.class);
        //rocketTemplate.sendTransactionMessage("laokou-test",new RocketmqDTO(),String.valueOf(IdGenerator.defaultSnowflakeId()));
    }

}
