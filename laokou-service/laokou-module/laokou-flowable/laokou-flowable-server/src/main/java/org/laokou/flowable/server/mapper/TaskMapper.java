package org.laokou.flowable.server.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.flowable.client.dto.TaskDTO;
import org.laokou.flowable.client.vo.TaskVO;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface TaskMapper {

    /**
     * 获取审批人或处理人
     * @param instanceId
     * @return
     */
    String getAssignee(@Param("instanceId")String instanceId);

    /**
     * 分页查询任务
     * @param page
     * @param dto
     * @return
     */
    IPage<TaskVO> getTakePage(IPage<TaskVO> page,@Param("dto")TaskDTO dto);

}
