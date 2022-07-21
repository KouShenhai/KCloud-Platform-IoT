package io.laokou.admin.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.domain.sys.entity.SysMessageDO;
import io.laokou.admin.interfaces.qo.MessageQO;
import io.laokou.admin.interfaces.vo.MessageDetailVO;
import io.laokou.admin.interfaces.vo.MessageVO;

public interface SysMessageService extends IService<SysMessageDO> {

    IPage<MessageVO> getMessageList(IPage<MessageVO> page, MessageQO qo);

    MessageDetailVO getMessageByDetailId(Long id);

    IPage<MessageVO> getUnReadList(IPage<MessageVO> page,Long userId);

    Boolean readMessage(Long id);

    MessageDetailVO getMessageById(Long id);

}
