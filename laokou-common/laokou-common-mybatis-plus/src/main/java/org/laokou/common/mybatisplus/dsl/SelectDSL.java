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

import java.util.Arrays;
import java.util.List;

/**
 * @author laokou
 */
public class SelectDSL extends DSL {

    private final Long limit;
    private final Long offset;
    private final OrderBy orderBy;
    private final List<Column> columns;

    public SelectDSL(Builder builder) {
        this.columns = builder.columns;
        this.limit = builder.limit;
        this.offset = builder.offset;
        this.orderBy = builder.orderBy;
    }

    public Long limit() {
        return limit;
    }

    public Long offset() {
        return offset;
    }

    public List<Column> columns() {
        return columns;
    }

    public OrderBy orderBy() {
        return orderBy;
    }

    public static class Builder {
        private Long limit;
        private Long offset;
        private List<Column> columns;
        private OrderBy orderBy;

        public Builder withLimit(Long limit) {
            this.limit = limit;
            return this;
        }

        public Builder withOffset(Long offset) {
            this.offset = offset;
            return this;
        }

        public Builder withColumns(Column...columns) {
            this.columns = Arrays.asList(columns);
            return this;
        }

        public Builder withOrderBy(OrderBy orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        public SelectDSL build() {
            return new SelectDSL(this);
        }
    }

}
