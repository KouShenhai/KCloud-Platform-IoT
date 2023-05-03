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

package org.laokou.common.log.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author laokou
 */
@Data
public class SysOperateLogExcel implements Serializable {

    @Serial
    private static final long serialVersionUID = -7345114440915452670L;
    /**
     * 模块名称，如：系统菜单
     */
    @ExcelProperty(index = 0,value = "模块名称")
    @ColumnWidth(value = 20)
    private String module;

    /**
     * 操作名称
     */
    @ExcelProperty(index = 1,value = "操作名称")
    @ColumnWidth(value = 20)
    private String operation;

    /**
     * 请求URI
     */
    @ExcelProperty(index = 2,value = "请求URI")
    @ColumnWidth(value = 40)
    private String requestUri;

    /**
     * 请求方式
     */
    @ExcelProperty(index = 3,value = "请求方式")
    @ColumnWidth(value = 20)
    private String requestMethod;

    /**
     * 请求参数
     */
    @ExcelProperty(index = 4,value = "请求参数")
    @ColumnWidth(value = 40)
    private String requestParams;

    /**
     * 浏览器版本
     */
    @ExcelProperty(index = 5,value = "浏览器版本")
    @ColumnWidth(value = 40)
    private String userAgent;

    /**
     * IP地址
     */
    @ExcelProperty(index = 6,value = "IP地址")
    @ColumnWidth(value = 20)
    private String requestIp;

    /**
     * 归属地
     */
    @ExcelProperty(index = 7,value = "归属地")
    @ColumnWidth(value = 20)
    private String requestAddress;

    /**
     * 状态  0：成功   1：失败
     */
    @ExcelProperty(index = 8,value = "状态")
    @ColumnWidth(value = 20)
    private String requestStatusMsg;

    @ExcelIgnore
    private Integer requestStatus;

    /**
     * 操作人
     */
    @ExcelProperty(index = 9,value = "操作人")
    @ColumnWidth(value = 20)
    private String operator;

    /**
     * 错误信息
     */
    @ExcelProperty(index = 10,value = "错误信息")
    @ColumnWidth(value = 40)
    private String errorMsg;

    /**
     * 方法名称
     */
    @ExcelProperty(index = 11,value = "方法名称")
    @ColumnWidth(value = 40)
    private String methodName;

    /**
     * 操作时间
     */
    @ExcelProperty(index = 12,value = "操作时间")
    @ColumnWidth(value = 20)
    private Date createDate;

    /**
     * 耗时（毫秒）
     */
    @ExcelProperty(index = 13,value = "耗时（毫秒）")
    @ColumnWidth(value = 20)
    private Long takeTime;

    public void setRequestStatusMsg(Integer requestStatus) {
        requestStatusMsg = requestStatus == 0 ? "成功" : "失败";
    }

    public void setRequestStatus(Integer requestStatus) {
        setRequestStatusMsg(requestStatus);
        this.requestStatus = requestStatus;
    }
}
