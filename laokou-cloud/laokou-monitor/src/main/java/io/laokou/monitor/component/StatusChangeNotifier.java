package io.laokou.monitor.component;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 通知配置
 */
@Slf4j
@Component
public class StatusChangeNotifier extends AbstractStatusChangeNotifier {
    public StatusChangeNotifier(InstanceRepository repositpry) {
        super(repositpry);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                String status = ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus();
                switch (status) {
                    //健康检查没通过
                    case "DOWN":  log.info("健康检查没通过"); break;
                    //服务离线
                    case "OFFLINE": log.info("服务离线"); break;
                    //服务上线
                    case "UP": log.info("服务上线"); break;
                    //服务未知异常
                    case "UNKNOWN": log.error("服务未知异常"); break;
                    default:break;
                }
            }
        });
    }
}
