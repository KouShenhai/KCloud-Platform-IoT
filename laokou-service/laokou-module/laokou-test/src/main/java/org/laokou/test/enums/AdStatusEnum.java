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

package org.laokou.test.enums;

/**
 * @author laokou
 */
public enum AdStatusEnum {

    NORMAL(0,"正常"),
    NO(1,"无传感器"),
    NO_DATA(-1,"无数据上传"),
    DATA_ERROR(-2,"未知的数据异常"),
    DATA_NOT_CHANGE(-3,"数据不变异常"),
    DATA_CHANGE_RANGE_ERROR(-4,"变动幅度异常");

    private final int status;
    private final String desc;

    AdStatusEnum(int status,String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }
}
