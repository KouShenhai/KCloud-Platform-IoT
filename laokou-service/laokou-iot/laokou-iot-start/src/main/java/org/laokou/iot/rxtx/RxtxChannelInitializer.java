package org.laokou.iot.rxtx;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;

public final class RxtxChannelInitializer extends ChannelInitializer<RxtxChannel> {
	@Override
	protected void initChannel(RxtxChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast(new LineBasedFrameDecoder(60000));
		pipeline.addLast(new StringEncoder(StandardCharsets.UTF_8));
		pipeline.addLast(new StringDecoder(StandardCharsets.UTF_8));
		pipeline.addLast();
	}
}
