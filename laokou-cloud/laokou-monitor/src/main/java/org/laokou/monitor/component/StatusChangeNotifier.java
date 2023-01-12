/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.monitor.component;
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
 * @author laokou
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
            if (event instanceof InstanceStatusChangedEvent eventStatus) {
                String status = eventStatus.getStatusInfo().getStatus();
                switch (status) {
                    //健康检查没通过
                    case "DOWN" -> log.info("健康检查没通过");

                    //服务离线
                    case "OFFLINE" -> log.info("服务离线");

                    //服务上线
                    case "UP" -> log.info("服务上线");

                    //服务未知异常
                    case "UNKNOWN" -> log.error("服务未知异常");
                    default -> {
                    }
                }
            }
        });
    }
}
