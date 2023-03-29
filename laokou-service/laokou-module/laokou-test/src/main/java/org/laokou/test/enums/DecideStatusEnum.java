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
public enum DecideStatusEnum {
    NORMAL(0,"正常"),
    NO_DATA(-1,"未上线，无任何数据"),
    ERROR(-2,"传感器异常");

    private final int status;
    private final String desc;

    DecideStatusEnum(int status,String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }
}
