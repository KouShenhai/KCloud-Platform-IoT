package com.yousen.report.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yousen.report.entity.DeviceDO;
import com.yousen.report.vo.SensorStatisticVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.yousen.report.entity.SensorDayTimeRT;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DeviceMapper extends BaseMapper<DeviceDO> {

    /**
     * 根据租户获取设备编号
     * @param tenantId
     * @return
     */
    List<String> getDeviceIds(@Param("tenantId")Integer tenantId);

    /**
     * 获取设备传感器统计
     * @param list
     * @param dateTime
     * @param maxWight
     * @param minWight
     * @return
     */
    List<SensorStatisticVO> getSensorStatisticList(@Param("list")List<String> list, @Param("dateTime")String dateTime, @Param("maxWight")Integer maxWight, @Param("minWight") Integer minWight);

    /**
     * 查询传感器每天第一次和最后一次计数
     * @param dateTime
     * @param list
     * @return
     */
    List<SensorDayTimeRT> getSensorDayTimeRTList(@Param("list")List<String> list, @Param("dateTime")String dateTime);

    /**
     * 获取获取设备传感器表
     * @param list
     * @return
     */
    List<String> getSensorStatisticTables(@Param("list")List<String> list);
}
