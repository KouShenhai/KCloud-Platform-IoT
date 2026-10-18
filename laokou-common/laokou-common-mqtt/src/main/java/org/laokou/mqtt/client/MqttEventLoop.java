package org.laokou.mqtt.client;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.uring.IoUring;
import io.netty.channel.uring.IoUringIoHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.i18n.util.SystemUtils;

import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
@Slf4j
@Getter
final class MqttEventLoop implements AutoCloseable {

	private final ExecutorService executor;

	private final EventLoopGroup workerGroup;

	MqttEventLoop(MqttClientProperties properties) {
		this.executor = ThreadUtils.newVirtualTaskExecutor();
		this.workerGroup = new MultiThreadIoEventLoopGroup(properties.getIoThreads(), executor, getIoHandlerFactory());
	}

	@Override
	public void close() {
		workerGroup.shutdownGracefully().addListener(_ -> ThreadUtils.shutdown(executor, 30));
	}

	private IoHandlerFactory getIoHandlerFactory() {
		if (isIoUringAvailable()) {
			log.info("Use IoUringIoHandler factory");
			return IoUringIoHandler.newFactory();
		}
		log.info("Use NioIoHandler factory");
		return NioIoHandler.newFactory();
	}

	private boolean isIoUringAvailable() {
		if (SystemUtils.isLinux()) {
			return IoUring.isAvailable();
		}
		return false;
	}

}
