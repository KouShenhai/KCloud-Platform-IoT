package io.laokou.log.listener;
import io.laokou.common.dto.LoginLogDTO;
import io.laokou.common.dto.OperateLogDTO;
import io.laokou.log.event.LoginLogEvent;
import io.laokou.log.event.OperateLogEvent;
import io.laokou.log.feign.admin.LogApiFeignClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@EnableAsync
@AllArgsConstructor
@ConditionalOnWebApplication
public class LogListener {

    private final LogApiFeignClient logApiFeignClient;

    @Async
    @Order
    @EventListener(OperateLogEvent.class)
    public void listenOperateLog(OperateLogEvent event) {
        OperateLogDTO dto = (OperateLogDTO) event.getSource();
        logApiFeignClient.insertOperateLog(dto);
    }

    @Async
    @Order
    @EventListener(LoginLogEvent.class)
    public void listenLoginLog(LoginLogEvent event) {
        LoginLogDTO dto = (LoginLogDTO) event.getSource();
        logApiFeignClient.insertLoginLog(dto);
    }

}
