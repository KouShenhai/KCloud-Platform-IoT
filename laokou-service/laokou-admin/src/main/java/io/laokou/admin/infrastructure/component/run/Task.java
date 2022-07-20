package io.laokou.admin.infrastructure.component.run;

import io.laokou.admin.infrastructure.component.handler.HandleHolder;
import io.laokou.admin.interfaces.dto.MessageDTO;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 发送消息
 */
@Data
@Accessors(chain = true)
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task implements Runnable{

    @Autowired
    private HandleHolder handleHolder;

    private MessageDTO dto;

    @Override
    public void run() {
        //发送消息
        handleHolder.route(dto.getSendChannel()).doHandler(dto);
    }
}
