/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.swagger.utils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.swagger.exception.ErrorCode;
import org.laokou.common.core.utils.MessageUtil;
/**
 * 统一返回结果实体类
 * @author laokou
 */
@Data
@Schema(name = "HttpResult",description = "统一返回结果实体类")
public class HttpResult<T> {
    /**
     * 编码：200标识成功，其他值表示失败
     */
    @Schema(name = "code",description = "编码",example = "200")
    private int code = 200;

    /**
     * 响应描述
     */
    @Schema(name = "msg",description = "响应描述",example = "success")
    private String msg = "success";


    /**
     * 响应结果
     */
    @Schema(name = "data",description = "响应结果",example = "true")
    private T data;

    public boolean success(){
        return code == 200;
    }

    public HttpResult<T> error(){
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = MessageUtil.getMessage(this.code);
        return this;
    }

    public HttpResult<T> error(int code){
        this.code = code;
        this.msg = MessageUtil.getMessage(this.code);
        return this;
    }

    public HttpResult<T> ok(T data){
        this.setData(data);
        return this;
    }

    public HttpResult<T> error(int code, String msg){
        this.code = code;
        this.msg = msg;
        return this;
    }

    public HttpResult<T> error(String msg){
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
        return this;
    }

}
