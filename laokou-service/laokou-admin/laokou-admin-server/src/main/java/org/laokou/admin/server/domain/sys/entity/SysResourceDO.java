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
package org.laokou.admin.server.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.common.mybatisplus.entity.BaseDO;
import lombok.Data;
/**
 * 系统资源
 * @author laokou
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:51
 */
@Data
@TableName("boot_sys_resource")
@Schema(name = "SysResourceDO",description = "系统资源实体类")
public class SysResourceDO extends BaseDO {

    /**
     * 资源标题
     */
    @Schema(name = "title",description = "资源标题")
    private String title;

    /**
     * 资源URL
     */
    @Schema(name = "url",description = "资源URL")
    private String url;

    /**
     * 资源编码
     */
    @Schema(name = "code",description = "资源编码")
    private String code;

    /**
     * 资源备注
     */
    @Schema(name = "remark",description = "资源备注")
    private String remark;

}
