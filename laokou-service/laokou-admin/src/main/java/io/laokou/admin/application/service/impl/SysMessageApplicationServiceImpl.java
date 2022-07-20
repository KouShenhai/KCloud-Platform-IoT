package io.laokou.admin.application.service.impl;

import io.laokou.admin.application.service.SysMessageApplicationService;
import io.laokou.admin.interfaces.dto.MessageDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysMessageApplicationServiceImpl implements SysMessageApplicationService {



    @Override
    public Boolean pushMessage(MessageDTO dto) {
        return true;
    }

    @Override
    public void consumeMessage(MessageDTO dto) {
        //1.插入日志
        //2.推送消息

    }

    @Override
    public Boolean insertMessage(MessageDTO dto) {
        return true;
    }

}
