package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysMessageDO;
import io.laokou.admin.domain.sys.repository.mapper.SysMessageMapper;
import io.laokou.admin.domain.sys.repository.service.SysMessageService;
import io.laokou.admin.interfaces.qo.MessageQO;
import io.laokou.admin.interfaces.vo.MessageDetailVO;
import io.laokou.admin.interfaces.vo.MessageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessageDO> implements SysMessageService {
    @Override
    public IPage<MessageVO> getMessageList(IPage<MessageVO> page, MessageQO qo) {
        return this.baseMapper.getMessageList(page,qo);
    }

    @Override
    public MessageDetailVO getMessageByDetailId(Long id) {
        return this.baseMapper.getMessageByDetailId(id);
    }

    @Override
    public IPage<MessageVO> getUnReadList(IPage<MessageVO> page, Long userId) {
        return this.baseMapper.getUnReadList(page,userId);
    }

    @Override
    public Boolean readMessage(Long id) {
        this.baseMapper.readMessage(id);
        return true;
    }

    @Override
    public MessageDetailVO getMessageById(Long id) {
        return this.baseMapper.getMessageById(id);
    }
}
