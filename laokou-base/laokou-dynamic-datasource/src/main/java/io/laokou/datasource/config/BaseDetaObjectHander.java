/**
 * Copyright 2020-2022 Kou Shenhai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.laokou.datasource.config;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.laokou.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Objects;
/**
 * @author Kou Shenhai
 */
@Component
@Slf4j
public class BaseDetaObjectHander implements MetaObjectHandler {

    private static final String CREATE_DATE = "createDate";
    private static final String UPDATE_DATE = "updateDate";
    private static final String DEL_FLAG = "delFlag";

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("come to insert fill .........");
        if (Objects.isNull(getFieldValByName(CREATE_DATE,metaObject))) {
            this.setFieldValByName(CREATE_DATE,new Date(),metaObject);
        }
        if (Objects.isNull(getFieldValByName(UPDATE_DATE,metaObject))) {
            this.setFieldValByName(UPDATE_DATE,new Date(),metaObject);
        }
        if (Objects.isNull(getFieldValByName(DEL_FLAG,metaObject))) {
            this.setFieldValByName(DEL_FLAG, Constant.NO, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("come to update fill .......");
        this.setFieldValByName(UPDATE_DATE,new Date(),metaObject);
        this.setFieldValByName(DEL_FLAG, Constant.NO, metaObject);
    }

}
