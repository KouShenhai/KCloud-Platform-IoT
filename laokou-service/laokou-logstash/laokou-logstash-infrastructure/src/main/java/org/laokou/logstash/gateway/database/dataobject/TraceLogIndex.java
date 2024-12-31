package org.laokou.logstash.gateway.database.dataobject;

import lombok.Data;
import org.laokou.common.elasticsearch.annotation.Field;
import org.laokou.common.elasticsearch.annotation.Index;
import org.laokou.common.elasticsearch.annotation.Setting;
import org.laokou.common.elasticsearch.annotation.Type;
import org.laokou.common.i18n.utils.DateUtil;

import java.io.Serializable;

@Data
@Index(setting = @Setting(refreshInterval = "1"))
public final class TraceLogIndex implements Serializable {

	@Field(type = Type.LONG)
	private String id;

	@Field(type = Type.KEYWORD)
	private String serviceId;

	@Field(type = Type.KEYWORD)
	private String profile;

	@Field(type = Type.DATE, format = DateUtil.Constant.YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS_SSS)
	private String dateTime;

	@Field(type = Type.KEYWORD, index = true)
	private String traceId;

	@Field(type = Type.KEYWORD, index = true)
	private String spanId;

	@Field(type = Type.KEYWORD)
	private String address;

	@Field(type = Type.KEYWORD)
	private String level;

	@Field(type = Type.KEYWORD)
	private String threadName;

	@Field(type = Type.KEYWORD)
	private String packageName;

	@Field(type = Type.KEYWORD)
	private String message;

	@Field(type = Type.KEYWORD)
	private String stacktrace;

}
