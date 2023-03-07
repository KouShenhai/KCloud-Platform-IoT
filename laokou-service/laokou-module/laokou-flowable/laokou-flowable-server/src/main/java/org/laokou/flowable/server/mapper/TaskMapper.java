package org.laokou.flowable.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface TaskMapper {

    /**
     * 获取审批人
     * @param instanceId
     * @return
     */
    @Select("SELECT a.assignee_ FROM act_hi_actinst a JOIN act_ru_task b ON b.id_ = a.task_id_\n" +
            "WHERE act_type_ = 'userTask' AND b.proc_inst_id_ = #{instanceId}")
    String getAssignee(@Param("instanceId")String instanceId);

}
