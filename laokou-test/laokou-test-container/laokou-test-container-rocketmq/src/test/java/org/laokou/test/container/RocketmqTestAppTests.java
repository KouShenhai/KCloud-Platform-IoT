package org.laokou.test.container;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RocketmqTestAppTests {

	private final RocketMqTemplate rocketMqTemplate;

	@Test
	void testRocketProducer() {
		rocketMqTemplate.sendSyncMessage("laokou_test","333");
	}

}
