package io.laokou.log.event;

import io.laokou.common.dto.OperateLogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * 操作日志事件
 */
public class OperateLogEvent extends ApplicationEvent {
    public OperateLogEvent(OperateLogDTO source) {
        super(source);
    }
}
