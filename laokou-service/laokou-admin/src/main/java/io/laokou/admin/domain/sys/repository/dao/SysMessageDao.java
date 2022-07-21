package io.laokou.admin.domain.sys.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.domain.sys.entity.SysMessageDO;
import io.laokou.admin.interfaces.qo.MessageQO;
import io.laokou.admin.interfaces.vo.MessageDetailVO;
import io.laokou.admin.interfaces.vo.MessageVO;
import io.laokou.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SysMessageDao extends BaseDao<SysMessageDO> {

    IPage<MessageVO> getMessageList(IPage<MessageVO> page, @Param("qo") MessageQO qo);

    MessageDetailVO getMessageByDetailId(@Param("id") Long id);

    IPage<MessageVO> getUnReadList(IPage<MessageVO> page, @Param("userId") Long userId);

    void readMessage(Long id);

    MessageDetailVO getMessageById(@Param("id")Long id);
}
