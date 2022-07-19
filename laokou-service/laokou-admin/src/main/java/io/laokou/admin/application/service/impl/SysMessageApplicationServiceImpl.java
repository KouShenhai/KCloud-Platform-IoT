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
    public Boolean send(MessageDTO dto) {
        return null;
    }

}
