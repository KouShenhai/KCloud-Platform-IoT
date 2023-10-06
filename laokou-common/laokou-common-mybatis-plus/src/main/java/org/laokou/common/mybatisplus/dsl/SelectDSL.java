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

import java.util.*;

/**
 * @author laokou
 */
public class SelectDSL extends DSL {

    private final String connector;
    private final String alias;
    private final String from;
    private final Long limit;
    private final Long offset;
    private final OrderBy orderBy;
    private final List<Column> columns;
    private final List<Join> joins;

    public SelectDSL(Builder builder) {
        this.from = builder.from;
        this.columns = builder.columns;
        this.limit = builder.limit;
        this.offset = builder.offset;
        this.orderBy = builder.orderBy;
        this.joins = builder.joins;
        this.connector = builder.connector;
        this.alias = builder.alias;
    }

    public String alias() {
        return alias;
    }

    public String from() {
        return from;
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

    public List<Join> joins() {
        return joins;
    }

    public String connector() {
        return connector;
    }

    public static class Column {
        private final String name;
        private final String alias;
        private final String sort;

        public Column(Builder builder) {
            this.name = builder.name;
            this.alias = builder.alias;
            this.sort = builder.sort;
        }

        public boolean orderByDesc() {
            return Objects.equals(sort, Constant.DESC);
        }

        public boolean orderByAsc() {
            return Objects.equals(sort, Constant.ASC);
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
            private String sort;

            public Builder withName(String name) {
                this.name = name;
                return this;
            }

            public Builder withAlias(String alias) {
                this.alias = alias;
                return this;
            }

            public Builder withSort(String sort) {
                this.sort = sort;
                return this;
            }

            public Column build() {
                return new Column(this);
            }

        }
    }

    public static class OrderBy {
        private final List<Column> COLUMNS = new ArrayList<>();

        public OrderBy(Collection<? extends Column> columns) {
            this.COLUMNS.addAll(columns);
        }

        public List<Column> columns() {
            return COLUMNS;
        }

        public static OrderBy of(Collection<? extends Column> columns) {
            return new OrderBy(columns);
        }
    }

    public static class Join {

        private final String alias;
        private final String type;
        private final String connector;
        private final String from;
        private final Long limit;
        private final Long offset;
        private final OrderBy orderBy;
        private final List<Column> columns;

        public Join(Builder builder) {
            this.alias = builder.alias;
            this.type = builder.type;
            this.connector = builder.connector;
            this.from = builder.from;
            this.limit = builder.limit;
            this.offset = builder.offset;
            this.orderBy = builder.orderBy;
            this.columns = builder.columns;
        }

        public String type() {
            return type;
        }

        public String alias() {
            return alias;
        }

        public String from() {
            return from;
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

        public String connector() {
            return connector;
        }

        public static class Builder {
            private String alias;
            private String type;
            private String connector;
            private String from;
            private Long limit;
            private Long offset;
            private List<Column> columns;
            private OrderBy orderBy;

            public Builder withAlias(String alias) {
                this.alias = alias;
                return this;
            }

            public Builder withType(String type) {
                this.type = type;
                return this;
            }

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

            public Builder withFrom(String from) {
                this.from = from;
                return this;
            }

            public Builder withConnector(String connector) {
                this.connector = connector;
                return this;
            }

            public Join build() {
                return new Join(this);
            }
        }

    }

    public static class Where {
        private final Object val1;
        private final Object val2;
        private final String column;
        private final String condition;
        private final String concat;

        public Where(Builder builder) {
            this.val1 = builder.val1;
            this.val2 = builder.val2;
            this.column = builder.column;
            this.condition = builder.condition;
            this.concat = builder.concat;
        }

        public Object val1() {
            return val1;
        }

        public String concat() {
            return concat;
        }

        public Object val2() {
            return val2;
        }

        public String column() {
            return column;
        }

        public String condition() {
            return condition;
        }

        public static class Builder {
            private Object val1;
            private Object val2;
            private String column;
            private String condition;
            private String concat;

            public Builder withVal1(Object val1) {
                this.val1 = val1;
                return this;
            }

            public Builder withVal2(Object val2) {
                this.val2 = val2;
                return this;
            }

            public Builder withColumn(String column) {
                this.column = column;
                return this;
            }

            public Builder withCondition(String condition) {
                this.condition = condition;
                return this;
            }

            public Builder withConcat(String concat) {
                this.concat = concat;
                return this;
            }

            public Where build() {
                return new Where(this);
            }

        }

    }

    public interface Constant {
        String ASC = "asc";
        String DESC = "desc";
        String SELECT = "select";
        String FROM = "from";
        String LIMIT = "limit";
        String OFFSET = "offset";
        String WHERE = "where";
        String AND = "and";
        String OR = "or";
        String INNER = "join";
        String LEFT = "left join";
        String RIGHT = "right join";
        String UNION = "union";
        String UNION_ALL = "union all";
        String ORDER_BY = "order by";
        String BETWEEN = "between";
        String LT = "<";
        String LE = "<=";
        String EQ = "=";
        String NE = "<>";
        String GE = ">=";
        String GT = ">";

    }

    public static class Builder {
        private String connector;
        private String from;
        private Long limit;
        private Long offset;
        private List<Column> columns;
        private OrderBy orderBy;
        private List<Join> joins;
        private String alias;

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

        public Builder withFrom(String from) {
            this.from = from;
            return this;
        }

        public Builder withJoin(Join...joins) {
            this.joins = Arrays.asList(joins);
            return this;
        }

        public Builder withAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder withConnector(String connector) {
            this.connector = connector;
            return this;
        }

        public SelectDSL build() {
            return new SelectDSL(this);
        }
    }

}
