package org.laokou.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.laokou.im.config.WebsocketProperties.PREFIX;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = PREFIX)
public class WebsocketProperties {

	public static final String PREFIX = "spring.websocket";

	private int port;

	private String appName;

	private String poolName;

}
