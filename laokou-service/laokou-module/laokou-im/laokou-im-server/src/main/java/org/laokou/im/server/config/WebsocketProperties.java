package org.laokou.im.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = WebsocketProperties.PREFIX)
public class WebsocketProperties {

	public static final String PREFIX = "spring.websocket";

	private int port;

	private String appName;

	private String poolName;

}
