/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.test.service.impl;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.test.dto.ReportDTO;
import org.laokou.test.entity.AdReportPO;
import org.laokou.test.entity.DecideExportRate;
import org.laokou.test.entity.DeviceExportPO;
import org.laokou.test.entity.DevicePO;
import org.laokou.test.enums.AdStatusEnum;
import org.laokou.test.enums.DecideStatusEnum;
import org.laokou.test.mapper.DeviceMapper;
import org.laokou.test.service.DeviceExportService;
import org.laokou.test.service.DeviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private static final String DEVICE_PREFIX = "dw_iot_ad_data_%s";

    private final DeviceMapper deviceMapper;
    private final DeviceExportService deviceExportService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void DeviceInfoReport(ReportDTO dto) {
        Integer tenantId = dto.getTenantId();
        String dateTime = dto.getDateTime();
        Date date = DateUtils.parseDate(dateTime, "yyyy-MM-dd");
        // 注意：每天只有一条数据
        Integer count = deviceExportService.getCount(dateTime);
        if (count > 0) {
            throw new CustomException(String.format("%s的数据已生成，请选择其他的日期",dateTime));
        }
        // 1.根据租户id获取所有的设备编号
        List<String> deviceIds = deviceMapper.getDeviceIdsByTenantId(tenantId);
        if (CollectionUtils.isEmpty(deviceIds)) {
            throw new CustomException("暂无数据");
        }
        // 2.根据设备编号拼接设备传感器收集表
        List<String> adDataTables = new ArrayList<>(deviceIds.size());
        for (String deviceId : deviceIds) {
            adDataTables.add(String.format(DEVICE_PREFIX,deviceId.toLowerCase()));
        }
        // 3.根据时间查询所有的设备传感器所收集的数据
        List<AdReportPO> adDataReport = deviceMapper.getAdDataReport(adDataTables,dateTime);
        List<DecideExportRate> decideExportRateList = deviceMapper.getDecideExportRateList(dateTime);
        if (CollectionUtils.isEmpty(decideExportRateList)) {
            throw new CustomException("暂无数据");
        }
        // 4.数据处理
        if (CollectionUtils.isEmpty(adDataReport)) {
            throw new CustomException("暂无数据");
        }
        // 根据租户id查询设备
        List<DevicePO> deviceList = deviceMapper.selectList(Wrappers.lambdaQuery(DevicePO.class).eq(DevicePO::getTenantId, tenantId));
        if (CollectionUtils.isEmpty(deviceList)) {
            throw new CustomException("该租户下未绑定设备，请联系管理员");
        }
        Map<String, List<DevicePO>> deviceMap = deviceList.stream().collect(Collectors.groupingBy(DevicePO::getDevId));
        Map<String, List<AdReportPO>> deviceReportMap = adDataReport.stream().collect(Collectors.groupingBy(AdReportPO::getDevId));
        List<DeviceExportPO> exportList = new ArrayList<>(deviceMap.size());
        deviceReportMap.forEach((k,v) -> {
            List<DevicePO> devicePOList = deviceMap.get(k);
            DeviceExportPO exportPO;
            if (CollectionUtils.isNotEmpty(devicePOList)) {
                exportPO = ConvertUtil.sourceToTarget(devicePOList.stream().findFirst().get(), DeviceExportPO.class);
            } else {
                exportPO = new DeviceExportPO();
            }
            exportPO.setDevId(k);
            // 获取传感器1状态
            exportPO.setSensor1Status(getAdStatus(v,1));
            // 获取传感器2状态
            exportPO.setSensor2Status(getAdStatus(v,2));
            // 获取传感器3状态
            exportPO.setSensor3Status(getAdStatus(v,3));
            // 获取传感器4状态
            exportPO.setSensor4Status(getAdStatus(v,4));
            // 获取传感器5状态
            exportPO.setSensor5Status(getAdStatus(v,5));
            // 获取传感器6状态
            exportPO.setSensor6Status(getAdStatus(v,6));
            // 设备状态
            exportPO.setDevStatus(getDecideStatus(exportPO));
            // 时间设置
            exportPO.setCreateTime(new Date());
            exportPO.setUpdateTime(new Date());
            exportPO.setReportDate(date);
            exportPO.setId(IdUtil.getSnowflakeNextId() + "");
            // 处理变动幅度异常

            exportList.add(exportPO);
        });
        // 5.写入数据库
        deviceExportService.saveBatch(exportList);
    }

    private void handleChangeError(DeviceExportPO po) {
        return;
    }

    private int getAdStatus(List<AdReportPO> valueList,int status) {
        List<AdReportPO> adList = valueList.stream().filter(s -> s.getAdId().equals(status)).collect(Collectors.toList());
        AdReportPO ad = adList.size() > 0 ? adList.get(0) : null;
        return getAdStatus(ad);
    }

    /**
     * 0（正常）
     * 1（未上线：无任何数据）
     * 2（传感器异常：配置的传感器只要存在不正常的状态就进行标记）
     * @param po
     * @return
     */
    private int getDecideStatus(DeviceExportPO po) {
        int s1 = po.getSensor1Status();
        int s2 = po.getSensor2Status();
        int s3 = po.getSensor3Status();
        int s4 = po.getSensor4Status();
        int s5 = po.getSensor5Status();
        int s6 = po.getSensor6Status();
        if (s1 == AdStatusEnum.NO_DATA.getStatus()
                && s2 == AdStatusEnum.NO_DATA.getStatus()
                && s3 == AdStatusEnum.NO_DATA.getStatus()
                && s4 == AdStatusEnum.NO_DATA.getStatus()
                && s5 == AdStatusEnum.NO_DATA.getStatus()
                && s6 == AdStatusEnum.NO_DATA.getStatus()) {
            return DecideStatusEnum.NO_DATA.getStatus();
        }
        if (s1 == AdStatusEnum.NORMAL.getStatus()
                && s2 == AdStatusEnum.NORMAL.getStatus()
                && s3 == AdStatusEnum.NORMAL.getStatus()
                && s4 == AdStatusEnum.NORMAL.getStatus()
                && s5 == AdStatusEnum.NORMAL.getStatus()
                && s6 == AdStatusEnum.NORMAL.getStatus()) {
            return DecideStatusEnum.NORMAL.getStatus();
        } else {
            return DecideStatusEnum.ERROR.getStatus();
        }
    }

    /**
     *  0（正常）
     *  1（无传感器）
     * -1（无数据上传：当日此传感器无数据上传）
     * -2（数据异常：未知的数据异常）
     * -3（数据不变异常：如配置了2个或2个以上传感器，只要有一个传感器数据有变动，而当前传感器数据未变动则进行标记）
     * -4（变动幅度异常：如配置了2个或2个以上传感器，对比同样时间段的N个数据变动的幅度比例是否与其他传感器变动幅度比例一致（上下幅度10%误差范围），如变动幅度）
     * @param po
     * @return
     */
    private int getAdStatus(AdReportPO po) {
        if (po == null) {
            return AdStatusEnum.NO.getStatus();
        }
        if (po.getMinRT() == null || po.getMaxRT() == null) {
            return AdStatusEnum.NO_DATA.getStatus();
        }
        int maxRT = po.getMaxRT();
        int minRT = po.getMinRT();
        if (maxRT == minRT) {
            return AdStatusEnum.DATA_NOT_CHANGE.getStatus();
        }
        return AdStatusEnum.NORMAL.getStatus();
    }

}
