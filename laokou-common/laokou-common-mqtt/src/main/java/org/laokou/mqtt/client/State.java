package org.laokou.mqtt.client;

import lombok.Getter;

/**
 * @author laokou
 */
@Getter
	enum State {

		WAIT_PUBACK(0, "QoS1：等待 PUBACK"),

		WAIT_PUBREC(1, "QoS2：已发 PUBLISH，等待 PUBREC"),

		WAIT_PUBCOMP(2, "QoS2：已发 PUBREL，等待 PUBCOMP");

		private final int code;

		private final String desc;

		State(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

	}
