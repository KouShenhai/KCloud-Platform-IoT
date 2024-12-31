package org.laokou.logstash.common.support;

import org.laokou.common.core.utils.ThreadUtil;
import org.laokou.common.i18n.common.constant.StringConstant;
import org.laokou.common.i18n.utils.DateUtil;

import java.util.concurrent.ExecutorService;

public abstract class AbstractTraceLogStorage implements TraceLogStorage {

	protected static final String TRACE_INDEX = "laokou_trace";

	protected static final ExecutorService EXECUTOR = ThreadUtil.newVirtualTaskExecutor();

	protected String getIndexName() {
		return TRACE_INDEX + StringConstant.UNDER + DateUtil.format(DateUtil.nowDate(), DateUtil.YYYYMMDD);
	}

}
