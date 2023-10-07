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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.laokou.common.i18n.common.Constant.EMPTY;

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

	private final List<Where> wheres;

	private final GroupBy groupBy;

	public SelectDSL(Builder builder) {
		this.from = builder.from;
		this.columns = builder.columns;
		this.limit = builder.limit;
		this.offset = builder.offset;
		this.orderBy = builder.orderBy;
		this.joins = builder.joins;
		this.connector = builder.connector;
		this.alias = builder.alias;
		this.wheres = builder.wheres;
		this.groupBy = builder.groupBy;
	}

	public List<Where> wheres() {
		return wheres;
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

	public GroupBy groupBy() {
		return groupBy;
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

		public String sort() {
			return sort;
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

		private final List<Where> wheres;

		private final List<On> ons;

		private final GroupBy groupBy;

		public Join(Builder builder) {
			this.alias = builder.alias;
			this.type = builder.type;
			this.connector = builder.connector;
			this.from = builder.from;
			this.limit = builder.limit;
			this.offset = builder.offset;
			this.orderBy = builder.orderBy;
			this.columns = builder.columns;
			this.wheres = builder.wheres;
			this.ons = builder.ons;
			this.groupBy = builder.groupBy;
		}

		public GroupBy groupBy() {
			return groupBy;
		}

		public List<On> ons() {
			return ons;
		}

		public List<Where> wheres() {
			return wheres;
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

			private List<Where> wheres;

			private List<On> ons;

			private GroupBy groupBy;

			public Builder withGroupBy(GroupBy groupBy) {
				this.groupBy = groupBy;
				return this;
			}

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

			public Builder withColumns(Column... columns) {
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

			public Builder withOns(On... ons) {
				this.ons = Arrays.asList(ons);
				return this;
			}

			public Builder withWheres(List<Where> wheres) {
				this.wheres = wheres;
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

		private final String compare1;

		private final String compare2;

		private final String concat;

		public Where(Builder builder) {
			this.val1 = builder.val1;
			this.val2 = builder.val2;
			this.column = builder.column;
			this.concat = builder.concat;
			this.compare1 = builder.compare1;
			this.compare2 = builder.compare2;
		}

		public String compare1() {
			return compare1;
		}

		public String compare2() {
			return compare2;
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

		public static class Builder {

			private Object val1 = EMPTY;

			private Object val2 = EMPTY;

			private String column = EMPTY;

			private String concat = EMPTY;

			private String compare1 = EMPTY;

			private String compare2 = EMPTY;

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

			public Builder withConcat(String concat) {
				this.concat = concat;
				return this;
			}

			public Builder withCompare1(String compare1) {
				this.compare1 = compare1;
				return this;
			}

			public Builder withCompare2(String compare2) {
				this.compare2 = compare2;
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

		String INNER_JOIN = "inner join";

		String LEFT_JOIN = "left join";

		String RIGHT_JOIN = "right join";

		String UNION = "union";

		String UNION_ALL = "union all";

		String ORDER_BY = "order by";

		String GROUP_BY = "group by";

		String BETWEEN = "between";

		String LT = "<";

		String LE = "<=";

		String EQ = "=";

		String NE = "<>";

		String GE = ">=";

		String GT = ">";

		String IN = "in";

		String ON = "on";

		String AS = "as";

	}

	public static class On {

		private final String fromAlias;

		private final String fromColumn;

		private final String joinAlias;

		private final String joinColumn;

		public On(Builder builder) {
			this.fromAlias = builder.fromAlias;
			this.fromColumn = builder.fromColumn;
			this.joinAlias = builder.joinAlias;
			this.joinColumn = builder.joinColumn;
		}

		public String fromAlias() {
			return fromAlias;
		}

		public String fromColumn() {
			return fromColumn;
		}

		public String joinAlias() {
			return joinAlias;
		}

		public String joinColumn() {
			return joinColumn;
		}

		public static class Builder {

			private String fromAlias;

			private String fromColumn;

			private String joinAlias;

			private String joinColumn;

			public Builder withFromAlias(String fromAlias) {
				this.fromAlias = fromAlias;
				return this;
			}

			public Builder withFromColumn(String fromColumn) {
				this.fromColumn = fromColumn;
				return this;
			}

			public Builder withJoinAlias(String joinAlias) {
				this.joinAlias = joinAlias;
				return this;
			}

			public Builder withJoinColumn(String joinColumn) {
				this.joinColumn = joinColumn;
				return this;
			}

			public On build() {
				return new On(this);
			}

		}

	}

	public static class GroupBy {

		private final List<Column> COLUMNS = new ArrayList<>();

		public GroupBy(Collection<? extends Column> columns) {
			this.COLUMNS.addAll(columns);
		}

		public List<Column> columns() {
			return COLUMNS;
		}

		public static GroupBy of(Collection<? extends Column> columns) {
			return new GroupBy(columns);
		}

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

		private List<Where> wheres;

		private GroupBy groupBy;

		public Builder withLimit(Long limit) {
			this.limit = limit;
			return this;
		}

		public Builder withOffset(Long offset) {
			this.offset = offset;
			return this;
		}

		public Builder withColumns(Column... columns) {
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

		public Builder withJoin(Join... joins) {
			this.joins = Arrays.asList(joins);
			return this;
		}

		public Builder withAlias(String alias) {
			this.alias = alias;
			return this;
		}

		public Builder withGroupBy(GroupBy groupBy) {
			this.groupBy = groupBy;
			return this;
		}

		public Builder withWhere(List<Where> wheres) {
			this.wheres = wheres;
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
