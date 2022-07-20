package io.laokou.admin.infrastructure.component.action;
import io.laokou.admin.infrastructure.component.event.SendMessageEvent;
import io.laokou.admin.infrastructure.component.pipeline.BusinessProcess;
import io.laokou.admin.interfaces.dto.MessageDTO;
import io.laokou.common.utils.SpringContextUtil;
import org.springframework.stereotype.Service;

@Service
public class SendMessageAction implements BusinessProcess<MessageDTO> {
    @Override
    public void process(MessageDTO dto) {
        SpringContextUtil.publishEvent(new SendMessageEvent(dto));
    }
}
