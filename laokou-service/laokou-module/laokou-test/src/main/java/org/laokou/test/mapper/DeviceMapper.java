package org.laokou.test.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.test.entity.AdReportPO;
import org.laokou.test.entity.DecideExportRate;
import org.laokou.test.entity.DevicePO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DeviceMapper extends BaseMapper<DevicePO> {

    /**
     * 根据租户id获取所有设备编号
     * @param tenantId
     * @return
     */
    List<String> getDeviceIdsByTenantId(int tenantId);

    /**
     * 获取设备传感器统计
     * @param list
     * @param dateTime
     * @return
     */
    List<AdReportPO> getAdDataReport(@Param("list")List<String> list,@Param("dateTime")String dateTime);

    /**
     * 查询
     * @param dateTime
     * @return
     */
    List<DecideExportRate> getDecideExportRateList(@Param("dateTime")String dateTime);

}
