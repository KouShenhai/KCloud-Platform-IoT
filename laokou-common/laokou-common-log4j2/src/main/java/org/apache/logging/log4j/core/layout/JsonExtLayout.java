/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.logging.log4j.core.layout;

import lombok.Getter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Note: The JsonLayout should be considered to be deprecated. Please use
 * JsonTemplateLayout instead.
 * <p>
 * Appends a series of JSON events as strings serialized as bytes.
 *
 * <h2>Complete well-formed JSON vs. fragment JSON</h2>
 * <p>
 * If you configure {@code complete="true"}, the appender outputs a well-formed JSON
 * document. By default, with {@code complete="false"}, you should include the output as
 * an <em>external file</em> in a separate file to form a well-formed JSON document.
 * </p>
 * <p>
 * If {@code complete="false"}, the appender does not write the JSON open array character
 * "[" at the start of the document, "]" and the end, nor comma "," between records.
 * </p>
 * <h2>Encoding</h2>
 * <p>
 * Appenders using this layout should have their {@code charset} set to {@code UTF-8} or
 * {@code UTF-16}, otherwise events containing non ASCII characters could result in
 * corrupted log files.
 * </p>
 * <h2>Pretty vs. compact JSON</h2>
 * <p>
 * By default, the JSON layout is not compact (a.k.a. "pretty") with
 * {@code compact="false"}, which means the appender uses end-of-line characters and
 * indents lines to format the text. If {@code compact="true"}, then no end-of-line or
 * indentation is used. Message content may contain, of course, escaped end-of-lines.
 * </p>
 * <h2>Additional Fields</h2>
 * <p>
 * This property allows addition of custom fields into generated JSON.
 * {@code <JsonLayout><KeyValuePair key="foo" value="bar"/></JsonLayout>} inserts
 * {@code "foo":"bar"} directly into JSON output. Supports Lookup expressions.
 * </p>
 */
@Plugin(name = "JsonExtLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public final class JsonExtLayout extends AbstractJacksonLayout {

	static final String CONTENT_TYPE = "application/json";

	private static final String DEFAULT_FOOTER = "";

	private static final String DEFAULT_HEADER = "[";

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	public static class Builder<B extends Builder<B>> extends AbstractJacksonLayout.Builder<B>
			implements org.apache.logging.log4j.core.util.Builder<JsonExtLayout> {

		@Getter
		@PluginBuilderAttribute
		private boolean propertiesAsList;

		@Getter
		@PluginBuilderAttribute
		private boolean objectMessageAsJsonObject;

		@Getter
		@PluginElement("AdditionalField")
		private KeyValuePair[] additionalFields;

		public Builder() {
			setCharset(StandardCharsets.UTF_8);
		}

		@Override
		public JsonExtLayout build() {
			final boolean encodeThreadContextAsList = isProperties() && propertiesAsList;
			final String headerPattern = toStringOrNull(super.getHeader());
			final String footerPattern = toStringOrNull(super.getFooter());
			return new JsonExtLayout(getConfiguration(), isLocationInfo(), isProperties(), encodeThreadContextAsList,
					isComplete(), isCompact(), getEventEol(), getEndOfLine(), headerPattern, footerPattern,
					getCharset(), isIncludeStacktrace(), isStacktraceAsString(), isIncludeNullDelimiter(),
					isIncludeTimeMillis(), getAdditionalFields(), isObjectMessageAsJsonObject());
		}

		public B setPropertiesAsList(final boolean propertiesAsList) {
			this.propertiesAsList = propertiesAsList;
			return asBuilder();
		}

		public B setObjectMessageAsJsonObject(final boolean objectMessageAsJsonObject) {
			this.objectMessageAsJsonObject = objectMessageAsJsonObject;
			return asBuilder();
		}

		@Override
		public B setAdditionalFields(final KeyValuePair[] additionalFields) {
			this.additionalFields = additionalFields;
			return asBuilder();
		}

	}

	private JsonExtLayout(final Configuration config, final boolean locationInfo, final boolean properties,
			final boolean encodeThreadContextAsList, final boolean complete, final boolean compact,
			final boolean eventEol, final String endOfLine, final String headerPattern, final String footerPattern,
			final Charset charset, final boolean includeStacktrace, final boolean stacktraceAsString,
			final boolean includeNullDelimiter, final boolean includeTimeMillis, final KeyValuePair[] additionalFields,
			final boolean objectMessageAsJsonObject) {
		super(config,
				new JacksonFactory.JSON(encodeThreadContextAsList, includeStacktrace, stacktraceAsString,
						objectMessageAsJsonObject)
					.newWriter(locationInfo, properties, compact, includeTimeMillis),
				charset, compact, complete, eventEol, endOfLine,
				PatternLayout.newSerializerBuilder()
					.setConfiguration(config)
					.setPattern(headerPattern)
					.setDefaultPattern(DEFAULT_HEADER)
					.build(),
				PatternLayout.newSerializerBuilder()
					.setConfiguration(config)
					.setPattern(footerPattern)
					.setDefaultPattern(DEFAULT_FOOTER)
					.build(),
				includeNullDelimiter, additionalFields);
	}

	@PluginBuilderFactory
	public static <B extends Builder<B>> B newBuilder() {
		return new Builder<B>().asBuilder();
	}

	/**
	 * Returns appropriate JSON header.
	 * @return a byte array containing the header, opening the JSON array.
	 */
	@Override
	public byte[] getHeader() {
		if (!this.complete) {
			return null;
		}
		final StringBuilder buf = new StringBuilder();
		final String str = serializeToString(getHeaderSerializer());
		if (str != null) {
			buf.append(str);
		}
		buf.append(this.eol);
		return getBytes(buf.toString());
	}

	/**
	 * Returns appropriate JSON footer.
	 * @return a byte array containing the footer, closing the JSON array.
	 */
	@Override
	public byte[] getFooter() {
		if (!this.complete) {
			return null;
		}
		final StringBuilder buf = new StringBuilder();
		buf.append(this.eol);
		final String str = serializeToString(getFooterSerializer());
		if (str != null) {
			buf.append(str);
		}
		buf.append(this.eol);
		return getBytes(buf.toString());
	}

	@Override
	public Map<String, String> getContentFormat() {
		final Map<String, String> result = HashMap.newHashMap(1);
		result.put("version", "4.0.0");
		return result;
	}

	/**
	 * @return The content type.
	 */
	@Override
	public String getContentType() {
		return CONTENT_TYPE + "; charset=" + this.getCharset();
	}

	@Override
	public void toSerializable(final LogEvent event, final Writer writer) throws IOException {
		// 判断是否定义<KeyValuePair>
		if (additionalFields.length > 0) {
			objectWriter.writeValue(writer, getFieldsMap(event));
			if (complete) {
				writer.append(",");
			}
			writer.write(eol);
			if (includeNullDelimiter) {
				writer.write('\0');
			}
		}
		else {
			if (complete && eventCount > 0) {
				writer.append(", ");
			}
			super.toSerializable(event, writer);
		}
	}

	// @formatter:off
	private Map<String, String> getFieldsMap(LogEvent event) {
		LogEvent evt = convertMutableToLog4jEvent(event);
		long timeMillis = evt.getTimeMillis();
		Map<String, String> additionalFieldsMap = resolveAdditionalFields(evt);
		additionalFieldsMap.putAll(evt.getContextData().toMap());
		additionalFieldsMap.putAll(Map.of(
			"address", System.getProperty("address", ""),
			"dateTime", FORMATTER.format(getLocalDateTimeOfTimestamp(timeMillis)),
			"level", evt.getLevel().name(),
			"threadName", evt.getThreadName(),
			"packageName", evt.getLoggerName(),
			"message", evt.getMessage().getFormattedMessage(),
			"stacktrace", getStackTraceAsString(evt.getThrown()))
		);
		return additionalFieldsMap;
	}
	// @formatter:on

	private String getStackTraceAsString(Throwable throwable) {
		if (ObjectUtils.isEmpty(throwable)) {
			return "";
		}
		StringWriter stringWriter = new StringWriter();
		try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
			throwable.printStackTrace(printWriter);
			return stringWriter.toString();
		}
	}

	private LogEvent convertMutableToLog4jEvent(final LogEvent event) {
		return event instanceof Log4jLogEvent ? event : Log4jLogEvent.createMemento(event);
	}

	private Map<String, String> resolveAdditionalFields(final LogEvent logEvent) {
		Map<String, String> additionalFieldsMap = new LinkedHashMap<>(this.additionalFields.length);
		StrSubstitutor strSubstitutor = this.configuration.getStrSubstitutor();
		for (ResolvableKeyValuePair pair : this.additionalFields) {
			if (pair.valueNeedsLookup) {
				additionalFieldsMap.put(pair.key, strSubstitutor.replace(logEvent, pair.value));
			}
			else {
				additionalFieldsMap.put(pair.key, pair.value);
			}
		}
		return additionalFieldsMap;
	}

	private LocalDateTime getLocalDateTimeOfTimestamp(long timestamp) {
		return LocalDateTime.ofInstant(getInstantOfTimestamp(timestamp), ZoneId.systemDefault());
	}

	private Instant getInstantOfTimestamp(long timestamp) {
		return Instant.ofEpochMilli(timestamp);
	}

}
