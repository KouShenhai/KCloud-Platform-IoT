/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
 *
 */

package org.laokou.common.mybatisplus.dsl;

import org.laokou.common.i18n.dto.DSL;

/**
 * @author laokou
 */
public class Column extends DSL {

    private final String name;
    private final String alias;

    public Column(Builder builder) {
        this.name = builder.name;
        this.alias = builder.alias;
    }

    public String name() {
        return name;
    }

    public String alias() {
        return alias;
    }

    public static class Builder {
        private String name;
        private String alias;
        private Sort sort;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public Column build() {
            return new Column(this);
        }

    }

}
