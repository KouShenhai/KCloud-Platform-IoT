package org.laokou.common.netty.config;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelConfig;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RxtxClient implements Client {

	protected final int port;

	protected final ChannelInitializer<?> channelInitializer;

	@Override
	public void open() {
		// 阻塞式
		try (OioEventLoopGroup group = new OioEventLoopGroup()) {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
				.channelFactory(() -> {
					RxtxChannel rxtxChannel = new RxtxChannel();
					rxtxChannel.config()
						.setBaudrate(port)
						.setDatabits(RxtxChannelConfig.Databits.DATABITS_8)
						.setParitybit(RxtxChannelConfig.Paritybit.NONE)
						.setStopbits(RxtxChannelConfig.Stopbits.STOPBITS_1);
					return rxtxChannel;
				}).handler(channelInitializer);
		}
	}

	@Override
	public void close() {

	}

	@Override
	public void send(String key, String payload) {

	}

}
