package io.laokou.admin.infrastructure.component.action;
import io.laokou.admin.infrastructure.component.pipeline.BusinessProcess;
import io.laokou.admin.interfaces.dto.MessageDTO;
import io.laokou.common.exception.CustomException;
import io.laokou.common.utils.ValidatorUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
@Service
public class PreParamCheckAction implements BusinessProcess<MessageDTO> {

    @Override
    public void process(MessageDTO dto) {
        ValidatorUtil.validateEntity(dto);
        if (CollectionUtils.isEmpty(dto.getReceiver())) {
            throw new CustomException("请选择接收用户");
        }
    }

}
