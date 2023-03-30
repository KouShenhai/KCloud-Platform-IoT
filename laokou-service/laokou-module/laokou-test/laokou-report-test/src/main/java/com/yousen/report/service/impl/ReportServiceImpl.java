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

package com.yousen.report.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yousen.report.entity.DeviceDayReportDO;
import com.yousen.report.enums.AdStatusEnum;
import com.yousen.report.enums.DecideStatusEnum;
import com.yousen.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import com.yousen.report.dto.BaseReportDTO;
import com.yousen.report.entity.SensorDayTimeRT;
import com.yousen.report.entity.DeviceDO;
import com.yousen.report.handler.CustomException;
import com.yousen.report.service.DeviceDayReportService;
import com.yousen.report.service.DeviceService;
import com.yousen.report.utils.ConvertUtil;
import com.yousen.report.vo.SensorStatisticVO;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final Environment environment;
    private static final String AD_DATA_TABLE_PREFIX = "dw_iot_ad_data_%s";
    private final DeviceService deviceService;
    private final DeviceDayReportService deviceDayReportService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public Boolean sensorReport(BaseReportDTO dto) {
        // 租户id
        Integer tenantId = dto.getTenantId();
        // 报表时间
        String dateTime = dto.getDateTime();
        // 最大阈值
        Integer minWight = environment.getProperty("youSen.weight.max",Integer.class);
        // 最小阈值
        Integer maxWight = environment.getProperty("youSen.weight.min",Integer.class);
        // 转换时间
        Date date = DateUtils.parseDate(dateTime, "yyyy-MM-dd");
        // 一个设备每天只有一条数据（按时间查询）
        Integer count = deviceDayReportService.getDeviceDayReportCount(dateTime);
        if (count > 0) {
            throw new CustomException(500,String.format("%s的数据已生成，请选择其他的时间",dateTime));
        }
        // 1.根据租户id获取所有的设备
        List<String> deviceIds = deviceService.getDeviceIds(tenantId);
        // 2.根据设备编号拼接设备传感器收集表
        List<String> sensorAdDataTables = new ArrayList<>(deviceIds.size());
        for (String deviceId : deviceIds) {
            sensorAdDataTables.add(String.format(AD_DATA_TABLE_PREFIX,deviceId.toLowerCase()));
        }
        // 过滤表掉不存在的表
        sensorAdDataTables = deviceService.getSensorStatisticTables(sensorAdDataTables);
        // 3.根据时间查询所有的设备传感器所收集的数据
        List<SensorStatisticVO> sensorStatisticList = deviceService.getSensorStatisticList(sensorAdDataTables,dateTime,maxWight,minWight);
        if (CollectionUtils.isEmpty(sensorStatisticList)) {
            throw new CustomException(500,"该设备的传感器未工作，请检查设备");
        }
        // 获取设备传感器一天中第一次和最后一次采集数据
        List<SensorDayTimeRT> sensorDayTimeRTList = deviceService.getSensorDayTimeRTList(sensorAdDataTables,dateTime);
        Map<String,List<SensorDayTimeRT>> sensorDayTimeRTMap;
        if (!CollectionUtils.isEmpty(sensorDayTimeRTList)) {
            sensorDayTimeRTMap = sensorDayTimeRTList.stream().collect(Collectors.groupingBy(SensorDayTimeRT::getDevId));
        } else {
            sensorDayTimeRTMap = new HashMap<>(0);
        }
        List<DeviceDO> deviceList = deviceService.list(Wrappers.lambdaQuery(DeviceDO.class)
                .eq(DeviceDO::getDevOnlineStatus,1)
                .isNotNull(DeviceDO::getDevId)
                .eq(DeviceDO::getTenantId, tenantId)
                .select(DeviceDO::getDevId
                        , DeviceDO::getSysOrgCode
                        , DeviceDO::getDevName
                        , DeviceDO::getTenantId
                        , DeviceDO::getDevId
                        , DeviceDO::getPlate));
        if (CollectionUtils.isEmpty(deviceList)) {
            throw new CustomException(500,"该租户下未绑定相关设备");
        }
        // 设备
        Map<String, List<DeviceDO>> deviceMap = deviceList.stream().collect(Collectors.groupingBy(DeviceDO::getDevId));
        //
        Map<String, List<SensorStatisticVO>> sensorStatisticMap = sensorStatisticList.stream().collect(Collectors.groupingBy(SensorStatisticVO::getDevId));
        List<DeviceDayReportDO> deviceDayReportList = new ArrayList<>(deviceMap.size());
        sensorStatisticMap.forEach((deviceId,SensorStatisticList) -> {
            List<DeviceDO> DeviceList = deviceMap.get(deviceId);
            // 设备信息不存在
            if (CollectionUtils.isEmpty(DeviceList)) {
                return;
            }
            DeviceDayReportDO deviceDayReportDO = ConvertUtil.sourceToTarget(DeviceList.stream().findFirst().get(), DeviceDayReportDO.class);
            deviceDayReportDO.setDevId(deviceId);
            // 获取传感器1状态
            deviceDayReportDO.setSensor1Status(getSensorStatus(SensorStatisticList,1));
            // 获取传感器2状态
            deviceDayReportDO.setSensor2Status(getSensorStatus(SensorStatisticList,2));
            // 获取传感器3状态
            deviceDayReportDO.setSensor3Status(getSensorStatus(SensorStatisticList,3));
            // 获取传感器4状态
            deviceDayReportDO.setSensor4Status(getSensorStatus(SensorStatisticList,4));
            // 获取传感器5状态
            deviceDayReportDO.setSensor5Status(getSensorStatus(SensorStatisticList,5));
            // 获取传感器6状态
            deviceDayReportDO.setSensor6Status(getSensorStatus(SensorStatisticList,6));
            // 处理变动幅度异常
            deviceDayReportDO = setSensorStatusResult(sensorDayTimeRTMap,deviceId,deviceDayReportDO);
            // 设备状态
            deviceDayReportDO.setDevStatus(getDeviceStatus(deviceDayReportDO));
            // 时间设置
            deviceDayReportDO.setCreateTime(new Date());
            deviceDayReportDO.setUpdateTime(new Date());
            deviceDayReportDO.setReportDate(date);
            deviceDayReportDO.setId(IdUtil.getSnowflakeNextId() + "");
            // 插入创建人和修改人
            deviceDayReportDO.setCreateBy("yousen");
            deviceDayReportDO.setUpdateBy("yousen");
            deviceDayReportList.add(deviceDayReportDO);
        });
        // 5.写入数据库
        return deviceDayReportService.saveBatch(deviceDayReportList);
    }

    @Override
    @SneakyThrows
    public Boolean initYMReport(BaseReportDTO dto) {
        int firstDay = 1;
        int lastDay = 31;
        List<CompletableFuture> list = Collections.synchronizedList(new ArrayList<>(lastDay));
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(9, 16, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(256), new ThreadPoolExecutor.AbortPolicy());
        for (int i = firstDay; i <= lastDay; i++) {
            BaseReportDTO newDTO = new BaseReportDTO();
            newDTO.setTenantId(dto.getTenantId());
            newDTO.setDateTime(dto.getYm() + "-" + (i > 9 ? i : "0" + i));
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> sensorReport(newDTO));
            list.add(future);
        }
        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();
        poolExecutor.shutdown();
        return true;
    }

    /**
     * 对传感器显示正常，判断是否有幅度异常
     * @param sensorDayTimeRTMap
     * @param deviceId
     * @param deviceDayReportDO
     * @return
     */
    private DeviceDayReportDO setSensorStatusResult(Map<String,List<SensorDayTimeRT>> sensorDayTimeRTMap, String deviceId, DeviceDayReportDO deviceDayReportDO) {
        if (checkChangRangeError(sensorDayTimeRTMap,deviceId)) {
            // 将正常改为变动幅度异常
            Integer sensor1Status = deviceDayReportDO.getSensor1Status();
            Integer sensor2Status = deviceDayReportDO.getSensor2Status();
            Integer sensor3Status = deviceDayReportDO.getSensor3Status();
            Integer sensor4Status = deviceDayReportDO.getSensor4Status();
            Integer sensor5Status = deviceDayReportDO.getSensor5Status();
            Integer sensor6Status = deviceDayReportDO.getSensor6Status();
            deviceDayReportDO.setSensor1Status(sensor1Status.equals(AdStatusEnum.NORMAL.getStatus()) ? AdStatusEnum.DATA_CHANGE_RANGE_ERROR.getStatus() : deviceDayReportDO.getSensor1Status());
            deviceDayReportDO.setSensor2Status(sensor2Status.equals(AdStatusEnum.NORMAL.getStatus()) ? AdStatusEnum.DATA_CHANGE_RANGE_ERROR.getStatus() : deviceDayReportDO.getSensor2Status());
            deviceDayReportDO.setSensor3Status(sensor3Status.equals(AdStatusEnum.NORMAL.getStatus()) ? AdStatusEnum.DATA_CHANGE_RANGE_ERROR.getStatus() : deviceDayReportDO.getSensor3Status());
            deviceDayReportDO.setSensor4Status(sensor4Status.equals(AdStatusEnum.NORMAL.getStatus()) ? AdStatusEnum.DATA_CHANGE_RANGE_ERROR.getStatus() : deviceDayReportDO.getSensor4Status());
            deviceDayReportDO.setSensor5Status(sensor5Status.equals(AdStatusEnum.NORMAL.getStatus()) ? AdStatusEnum.DATA_CHANGE_RANGE_ERROR.getStatus() : deviceDayReportDO.getSensor5Status());
            deviceDayReportDO.setSensor6Status(sensor6Status.equals(AdStatusEnum.NORMAL.getStatus()) ? AdStatusEnum.DATA_CHANGE_RANGE_ERROR.getStatus() : deviceDayReportDO.getSensor6Status());
        }
        return deviceDayReportDO;
    }

    /**
     * 检查变动幅度
     * @param sensorDayTimeRTMap
     * @param deviceId
     * @return
     */
    private boolean checkChangRangeError(Map<String,List<SensorDayTimeRT>> sensorDayTimeRTMap, String deviceId) {
        List<SensorDayTimeRT> sensorDayTimeRTS = sensorDayTimeRTMap.get(deviceId);
        if (CollectionUtils.isEmpty(sensorDayTimeRTS)) {
            return false;
        }
        // 判断是否存在两种传感器
        long count = sensorDayTimeRTS.stream().map(k -> k.getAcId()).distinct().count();
        if (count < 2) {
            return false;
        }
        // 传感器1
        SensorDayTimeRT sensorDayTimeRT1 = buildSensorDayTimeRT(sensorDayTimeRTS,1);
        // 传感器2
        SensorDayTimeRT sensorDayTimeRT2 = buildSensorDayTimeRT(sensorDayTimeRTS,2);
        // 传感器3
        SensorDayTimeRT sensorDayTimeRT3 = buildSensorDayTimeRT(sensorDayTimeRTS,3);
        // 传感器4
        SensorDayTimeRT sensorDayTimeRT4 = buildSensorDayTimeRT(sensorDayTimeRTS,4);
        // 传感器5
        SensorDayTimeRT sensorDayTimeRT5 = buildSensorDayTimeRT(sensorDayTimeRTS,5);
        // 传感器6
        SensorDayTimeRT sensorDayTimeRT6 = buildSensorDayTimeRT(sensorDayTimeRTS,6);
        BigDecimal rate1 = rate(Math.abs(sensorDayTimeRT1.getMinTimeRT() - sensorDayTimeRT1.getMaxTimeRT()), sensorDayTimeRT1.getMinTimeRT());
        BigDecimal rate2 = rate(Math.abs(sensorDayTimeRT2.getMinTimeRT() - sensorDayTimeRT2.getMaxTimeRT()), sensorDayTimeRT2.getMinTimeRT());
        BigDecimal rate3 = rate(Math.abs(sensorDayTimeRT3.getMinTimeRT() - sensorDayTimeRT3.getMaxTimeRT()), sensorDayTimeRT3.getMinTimeRT());
        BigDecimal rate4 = rate(Math.abs(sensorDayTimeRT4.getMinTimeRT() - sensorDayTimeRT4.getMaxTimeRT()), sensorDayTimeRT4.getMinTimeRT());
        BigDecimal rate5 = rate(Math.abs(sensorDayTimeRT5.getMinTimeRT() - sensorDayTimeRT5.getMaxTimeRT()), sensorDayTimeRT5.getMinTimeRT());
        BigDecimal rate6 = rate(Math.abs(sensorDayTimeRT6.getMinTimeRT() - sensorDayTimeRT6.getMaxTimeRT()), sensorDayTimeRT6.getMinTimeRT());
        // 过滤
        List<BigDecimal> bigDecimals = new ArrayList<>(6);
        bigDecimals.add(rate1);
        bigDecimals.add(rate2);
        bigDecimals.add(rate3);
        bigDecimals.add(rate4);
        bigDecimals.add(rate5);
        bigDecimals.add(rate6);
        bigDecimals = bigDecimals.stream().filter(r -> r.doubleValue() != 0 && r.doubleValue() != 100).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bigDecimals)) {
            return false;
        }
        DoubleSummaryStatistics doubleSummaryStatistics = bigDecimals.parallelStream().mapToDouble(i -> i.doubleValue()).summaryStatistics();
        double max = doubleSummaryStatistics.getMax();
        double min = doubleSummaryStatistics.getMin();
        // 允许误差在10%
        return max > 0.1 + min;
    }

    /**
     * 构建对象
     * @param sensorDayTimeRTS
     * @param type
     * @return
     */
    private SensorDayTimeRT buildSensorDayTimeRT(List<SensorDayTimeRT> sensorDayTimeRTS, Integer type) {
        List<SensorDayTimeRT> sensorDayTimeRTList = sensorDayTimeRTS.stream().filter(k -> k.getAcId().equals(type)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(sensorDayTimeRTList)) {
            return sensorDayTimeRTList.get(0);
        }
        return new SensorDayTimeRT();
    }

    /**
     * 计算百分比 保留两位小数
     * 被除数/除数*100% 保留两位小数
     * @param dividend 被除数
     * @param divisor  除数 不为零
     */
    private BigDecimal rate(int dividend, int divisor) {
        if (divisor == 0) {
            return new BigDecimal(100);
        }
        return BigDecimal.valueOf(Math.abs(dividend)).divide(BigDecimal.valueOf(divisor), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 获取传感器状态
     * @param valueList
     * @param type
     * @return
     */
    private int getSensorStatus(List<SensorStatisticVO> valueList, int type) {
        List<SensorStatisticVO> adList = valueList.stream().filter(s -> s.getAdId().equals(type)).collect(Collectors.toList());        SensorStatisticVO ad = adList.size() > 0 ? adList.get(0) : null;
        return getSensorStatus(ad);
    }

    /**
     * 获取设备状态
     * 0（正常）
     * 1（未上线：无任何数据）
     * 2（传感器异常：配置的传感器只要存在不正常的状态就进行标记）
     * @param deviceDayReportDO
     * @return
     */
    private int getDeviceStatus(DeviceDayReportDO deviceDayReportDO) {
        int s1 = deviceDayReportDO.getSensor1Status();
        int s2 = deviceDayReportDO.getSensor2Status();
        int s3 = deviceDayReportDO.getSensor3Status();
        int s4 = deviceDayReportDO.getSensor4Status();
        int s5 = deviceDayReportDO.getSensor5Status();
        int s6 = deviceDayReportDO.getSensor6Status();
        if (s1 == AdStatusEnum.NO_DATA.getStatus()
                && s2 == AdStatusEnum.NO_DATA.getStatus()
                && s3 == AdStatusEnum.NO_DATA.getStatus()
                && s4 == AdStatusEnum.NO_DATA.getStatus()
                && s5 == AdStatusEnum.NO_DATA.getStatus()
                && s6 == AdStatusEnum.NO_DATA.getStatus()) {
            return DecideStatusEnum.NO_DATA.getStatus();
        }
        if (s1 < AdStatusEnum.NORMAL.getStatus()
                || s2 < AdStatusEnum.NORMAL.getStatus()
                || s3 < AdStatusEnum.NORMAL.getStatus()
                || s4 < AdStatusEnum.NORMAL.getStatus()
                || s5 < AdStatusEnum.NORMAL.getStatus()
                || s6 < AdStatusEnum.NORMAL.getStatus()) {
            return DecideStatusEnum.ERROR.getStatus();
        }
        return DecideStatusEnum.NORMAL.getStatus();
    }

    /**
     * 获取传感器状态
     *  0（正常）
     *  1（无传感器）
     * -1（无数据上传：当日此传感器无数据上传）
     * -2（数据异常：未知的数据异常）
     * -3（数据不变异常：如配置了2个或2个以上传感器，只要有一个传感器数据有变动，而当前传感器数据未变动则进行标记）
     * -4（变动幅度异常：如配置了2个或2个以上传感器，对比同样时间段的N个数据变动的幅度比例是否与其他传感器变动幅度比例一致（上下幅度10%误差范围），如变动幅度）
     * -5（超出最大阈值）
     * -6（超出最低阈值）
     * @param vo
     * @return
     */
    private int getSensorStatus(SensorStatisticVO vo) {
        if (vo == null) {
            return AdStatusEnum.NO.getStatus();
        }
        if (vo.getMinRT() == null || vo.getMaxRT() == null) {
            return AdStatusEnum.NO_DATA.getStatus();
        }
        int maxRT = vo.getMaxRT();
        int minRT = vo.getMinRT();
        if (maxRT == minRT) {
            return AdStatusEnum.DATA_NOT_CHANGE.getStatus();
        }
//        if (vo.getMaxWeightCount() > 0) {
//            return AdStatusEnum.DATA_MAX_THRESHOLD_ERROR.getStatus();
//        }
//        if (vo.getMinWeightCount() > 0) {
//            return AdStatusEnum.DATA_MIN_THRESHOLD_ERROR.getStatus();
//        }
        return AdStatusEnum.NORMAL.getStatus();
    }


}
