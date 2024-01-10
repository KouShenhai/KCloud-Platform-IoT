package org.laokou.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.PropertiesConstants.WEBSOCKET_PREFIX;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = WEBSOCKET_PREFIX)
public class WebsocketProperties {

	private int port;

	private String appName;

	private String poolName;

}
