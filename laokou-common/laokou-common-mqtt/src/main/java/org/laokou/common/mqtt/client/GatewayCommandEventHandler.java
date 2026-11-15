package org.laokou.common.mqtt.client;

import com.lmax.disruptor.EventHandler;

public interface GatewayCommandEventHandler extends EventHandler<GatewayCommandEvent> {}
