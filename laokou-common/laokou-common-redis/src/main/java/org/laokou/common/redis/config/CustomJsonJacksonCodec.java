/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.laokou.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.netty.buffer.*;
import org.laokou.common.i18n.utils.DateUtil;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author laokou
 */
public class CustomJsonJacksonCodec extends BaseCodec {

	public static final CustomJsonJacksonCodec INSTANCE = new CustomJsonJacksonCodec();

	private volatile ObjectMapper mapObjectMapper;

	public CustomJsonJacksonCodec() {
		this.mapObjectMapper = CustomJsonJacksonCodec.getObjectMapper();
	}

	private final Encoder encoder = in -> {
		ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
		try {
			ByteBufOutputStream os = new ByteBufOutputStream(out);
			mapObjectMapper.writeValue((OutputStream) os, in);
			return os.buffer();
		}
		catch (IOException e) {
			out.release();
			throw e;
		}
		catch (Exception e) {
			out.release();
			throw new IOException(e);
		}
	};

	private final Decoder<Object> DECODER = (buf, state) -> mapObjectMapper
		.readValue((InputStream) new ByteBufInputStream(buf), Object.class);

	@Override
	public Decoder<Object> getValueDecoder() {
		return DECODER;
	}

	@Override
	public Encoder getValueEncoder() {
		return encoder;
	}

	public static ObjectMapper getObjectMapper() {
		// 解决查询缓存转换异常的问题
		ObjectMapper objectMapper = new ObjectMapper();
		DateTimeFormatter dateTimeFormatter = DateUtil.getDateTimeFormatter(DateUtil.YYYY_MM_DD_HH_MM_SS);
		// Long类型转String类型
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
		javaTimeModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		// LocalDateTime
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
		objectMapper.registerModule(javaTimeModule);
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.PROPERTY);
		return objectMapper;
	}

}
