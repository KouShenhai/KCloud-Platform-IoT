/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

/*
 * Copyright (c) 2011-2024, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.mybatisplus.config;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.baomidou.mybatisplus.extension.toolkit.PropertyMapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlParserUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.transaction.SpringManagedTransaction;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 没有上过生产，请谨慎使用.
 * 没有上过生产，请谨慎使用.
 * 没有上过生产，请谨慎使用.
 *
 * @author hubin
 * @author gitkakafu
 * @author laokou
 * @see PaginationInnerInterceptor 分页拦截器
 * <p>
 * 默认对 left join 进行优化,虽然能优化count,但是加上分页的话如果1对多本身结果条数就是不正确的
 * @since 3.4.0
 */
@Data
@Slf4j
@NoArgsConstructor
@SuppressWarnings({ "rawtypes" })
public class AsyncPaginationInnerInterceptor implements InnerInterceptor {

	/**
	 * 获取jsqlparser中count的SelectItem.
	 */
	protected static final List<SelectItem<?>> COUNT_SELECT_ITEM = Collections
		.singletonList(new SelectItem<>(new Column().withColumnName("COUNT(*)")).withAlias(new Alias("total")));

	protected static final Map<String, MappedStatement> countMsCache = new ConcurrentHashMap<>();

	private static final ThreadLocal<Future<?>> COUNT_LOCAL = new TransmittableThreadLocal<>();

	/**
	 * 溢出总页数后是否进行处理.
	 */
	protected boolean overflow;

	/**
	 * 单页分页条数限制.
	 */
	protected Long maxLimit;

	/**
	 * 生成 countSql 优化掉 join 现在只支持 left join.
	 *
	 * @since 3.4.2
	 */
	protected boolean optimizeJoin = true;

	/**
	 * 数据库类型.
	 * <p>
	 * 查看 {@link #findIDialect(Executor)} 逻辑.
	 */
	private DbType dbType;

	/**
	 * 方言实现类.
	 * <p>
	 * 查看 {@link #findIDialect(Executor)} 逻辑.
	 */
	private IDialect dialect;

	private DataSource dataSource;

	private ExecutorService executorService;

	public AsyncPaginationInnerInterceptor(DbType dbType, DataSource dataSource, ExecutorService executorService) {
		this.dbType = dbType;
		this.dataSource = dataSource;
		this.executorService = executorService;
	}

	public AsyncPaginationInnerInterceptor(IDialect dialect, DataSource dataSource, ExecutorService executorService) {
		this.dialect = dialect;
		this.dataSource = dataSource;
		this.executorService = executorService;
	}

	public AsyncPaginationInnerInterceptor(DataSource dataSource, ExecutorService executorService) {
		this.dataSource = dataSource;
		this.executorService = executorService;
	}

	public static Future<?> get() {
		return COUNT_LOCAL.get();
	}

	public static void remove() {
		COUNT_LOCAL.remove();
	}

	/**
	 * 这里进行count,如果count为0这返回false(就是不再执行sql了).
	 */
	@Override
	public boolean willDoQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
			ResultHandler resultHandler, BoundSql boundSql) {
		IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
		if (page == null || page.getSize() < 0 || !page.searchCount() || resultHandler != Executor.NO_RESULT_HANDLER) {
			return true;
		}

		BoundSql countSql;
		MappedStatement countMs = buildCountMappedStatement(ms, page.countId());
		if (countMs != null) {
			countSql = countMs.getBoundSql(parameter);
		}
		else {
			countMs = buildAutoCountMappedStatement(ms);
			String countSqlStr = autoCountSql(page, boundSql.getSql());
			PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
			countSql = new BoundSql(countMs.getConfiguration(), countSqlStr, mpBoundSql.parameterMappings(), parameter);
			PluginUtils.setAdditionalParameter(countSql, mpBoundSql.additionalParameters());
		}

		SpringManagedTransaction springManagedTransaction = new SpringManagedTransaction(dataSource);
		SimpleExecutor simpleExecutor = new SimpleExecutor(ms.getConfiguration(), springManagedTransaction);
		CacheKey cacheKey = simpleExecutor.createCacheKey(countMs, parameter, rowBounds, countSql);

		final MappedStatement mappedStatement = countMs;

		Future<?> future = executorService.submit(() -> {
			try {
				List<Object> result = executor.query(mappedStatement, parameter, rowBounds, resultHandler, cacheKey,
						countSql);
				long total = 0;
				if (CollectionUtils.isNotEmpty(result)) {
					// 个别数据库 count 没数据不会返回 0
					Object o = result.getFirst();
					if (o != null) {
						total = Long.parseLong(o.toString());
					}
				}
				page.setTotal(total);
			}
			catch (Exception e) {
				log.error("查询失败，错误信息：{}", e.getMessage());
			}
		});
		COUNT_LOCAL.set(future);
		return true;
	}

	@Override
	public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
			ResultHandler resultHandler, BoundSql boundSql) {
		IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
		if (null == page) {
			return;
		}

		// 处理 orderBy 拼接
		boolean addOrdered = false;
		String buildSql = boundSql.getSql();
		List<OrderItem> orders = page.orders();
		if (CollectionUtils.isNotEmpty(orders)) {
			addOrdered = true;
			buildSql = this.concatOrderBy(buildSql, orders);
		}

		// size 小于 0 且不限制返回值则不构造分页sql
		Long _limit = page.maxLimit() != null ? page.maxLimit() : maxLimit;
		if (page.getSize() < 0 && null == _limit) {
			if (addOrdered) {
				PluginUtils.mpBoundSql(boundSql).sql(buildSql);
			}
			return;
		}

		handlerLimit(page, _limit);
		IDialect dialect = findIDialect(executor);

		final Configuration configuration = ms.getConfiguration();
		DialectModel model = dialect.buildPaginationSql(buildSql, page.offset(), page.getSize());
		PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);

		List<ParameterMapping> mappings = mpBoundSql.parameterMappings();
		Map<String, Object> additionalParameter = mpBoundSql.additionalParameters();
		model.consumers(mappings, configuration, additionalParameter);
		mpBoundSql.sql(model.getDialectSql());
		mpBoundSql.parameterMappings(mappings);
	}

	/**
	 * 获取分页方言类的逻辑.
	 * @param executor Executor
	 * @return 分页方言类
	 */
	protected IDialect findIDialect(Executor executor) {
		if (dialect != null) {
			return dialect;
		}
		if (dbType != null) {
			dialect = DialectFactory.getDialect(dbType);
			return dialect;
		}
		return DialectFactory.getDialect(JdbcUtils.getDbType(executor));
	}

	/**
	 * 获取指定的 id 的 MappedStatement.
	 * @param ms MappedStatement
	 * @param countId id
	 * @return MappedStatement
	 */
	protected MappedStatement buildCountMappedStatement(MappedStatement ms, String countId) {
		if (StringUtils.isNotBlank(countId)) {
			final String id = ms.getId();
			if (!countId.contains(StringPool.DOT)) {
				countId = id.substring(0, id.lastIndexOf(StringPool.DOT) + 1) + countId;
			}
			final Configuration configuration = ms.getConfiguration();
			try {
				return CollectionUtils.computeIfAbsent(countMsCache, countId,
						key -> configuration.getMappedStatement(key, false));
			}
			catch (Exception e) {
				log.warn(String.format("can not find this countId: [\"%s\"]", countId));
			}
		}
		return null;
	}

	/**
	 * 构建 mp 自用自动的 MappedStatement.
	 * @param ms MappedStatement
	 * @return MappedStatement
	 */
	protected MappedStatement buildAutoCountMappedStatement(MappedStatement ms) {
		final String countId = ms.getId() + "_mpCount";
		final Configuration configuration = ms.getConfiguration();
		return CollectionUtils.computeIfAbsent(countMsCache, countId, key -> {
			MappedStatement.Builder builder = new MappedStatement.Builder(configuration, key, ms.getSqlSource(),
					ms.getSqlCommandType());
			builder.resource(ms.getResource());
			builder.fetchSize(ms.getFetchSize());
			builder.statementType(ms.getStatementType());
			builder.timeout(ms.getTimeout());
			builder.parameterMap(ms.getParameterMap());
			builder.resultMaps(Collections.singletonList(
					new ResultMap.Builder(configuration, Constants.MYBATIS_PLUS, Long.class, Collections.emptyList())
						.build()));
			builder.resultSetType(ms.getResultSetType());
			builder.cache(ms.getCache());
			builder.flushCacheRequired(ms.isFlushCacheRequired());
			builder.useCache(ms.isUseCache());
			return builder.build();
		});
	}

	/**
	 * 获取自动优化的 countSql.
	 * @param page 参数
	 * @param sql sql
	 * @return countSql
	 */
	public String autoCountSql(IPage<?> page, String sql) {
		if (!page.optimizeCountSql()) {
			return lowLevelCountSql(sql);
		}
		try {
			Select select = (Select) JsqlParserGlobal.parse(sql);
			// https://github.com/baomidou/mybatis-plus/issues/3920 分页增加union语法支持
			if (select instanceof SetOperationList) {
				return lowLevelCountSql(sql);
			}
			PlainSelect plainSelect = (PlainSelect) select;

			// 优化 order by 在非分组情况下
			List<OrderByElement> orderBy = plainSelect.getOrderByElements();
			if (CollectionUtils.isNotEmpty(orderBy)) {
				boolean canClean = true;
				for (OrderByElement order : orderBy) {
					// order by 里带参数,不去除order by
					Expression expression = order.getExpression();
					if (!(expression instanceof Column) && expression.toString().contains(StringPool.QUESTION_MARK)) {
						canClean = false;
						break;
					}
				}
				if (canClean) {
					plainSelect.setOrderByElements(null);
				}
			}
			Distinct distinct = plainSelect.getDistinct();
			GroupByElement groupBy = plainSelect.getGroupBy();
			// 包含 distinct、groupBy 不优化
			if (null != distinct || null != groupBy) {
				return lowLevelCountSql(select.toString());
			}
			// #95 Github, selectItems contains #{} ${}, which will be translated to ?,
			// and it may be in a function: power(#{myInt},2)
			for (SelectItem item : plainSelect.getSelectItems()) {
				if (item.toString().contains(StringPool.QUESTION_MARK)) {
					return lowLevelCountSql(select.toString());
				}
			}

			// 包含 join 连表,进行判断是否移除 join 连表
			if (optimizeJoin && page.optimizeJoinOfCountSql()) {
				List<Join> joins = plainSelect.getJoins();
				if (CollectionUtils.isNotEmpty(joins)) {
					boolean canRemoveJoin = true;
					String whereS = Optional.ofNullable(plainSelect.getWhere())
						.map(Expression::toString)
						.orElse(StringPool.EMPTY);
					// 不区分大小写
					whereS = whereS.toLowerCase();
					for (Join join : joins) {
						if (!join.isLeft()) {
							canRemoveJoin = false;
							break;
						}
						FromItem rightItem = join.getRightItem();
						String str = "";
						if (rightItem instanceof Table table) {
							str = Optional.ofNullable(table.getAlias()).map(Alias::getName).orElse(table.getName())
									+ StringPool.DOT;
						}
						else if (rightItem instanceof ParenthesedSelect subSelect) {
							/*
							 * 如果 left join 是子查询，并且子查询里包含 ?(代表有入参) 或者 where 条件里包含使用 join
							 * 的表的字段作条件,就不移除 join
							 */
							if (subSelect.toString().contains(StringPool.QUESTION_MARK)) {
								canRemoveJoin = false;
								break;
							}
							str = subSelect.getAlias().getName() + StringPool.DOT;
						}
						// 不区分大小写
						str = str.toLowerCase();

						if (whereS.contains(str)) {
							/* 如果 where 条件里包含使用 join 的表的字段作条件,就不移除 join */
							canRemoveJoin = false;
							break;
						}

						for (Expression expression : join.getOnExpressions()) {
							if (expression.toString().contains(StringPool.QUESTION_MARK)) {
								/* 如果 join 里包含 ?(代表有入参) 就不移除 join */
								canRemoveJoin = false;
								break;
							}
						}
					}

					if (canRemoveJoin) {
						plainSelect.setJoins(null);
					}
				}
			}

			// 优化 SQL
			plainSelect.setSelectItems(COUNT_SELECT_ITEM);
			return select.toString();
		}
		catch (JSQLParserException e) {
			// 无法优化使用原 SQL
			log.warn("optimize this sql to a count sql has exception, sql:\"{}\", exception:\n{}", sql, e.getCause());
		}
		catch (Exception e) {
			log.warn("optimize this sql to a count sql has error, sql:\"{}\", exception:\n{}", sql, e);
		}
		return lowLevelCountSql(sql);
	}

	/**
	 * 无法进行count优化时,降级使用此方法.
	 * @param originalSql 原始sql
	 * @return countSql
	 */
	protected String lowLevelCountSql(String originalSql) {
		return SqlParserUtils.getOriginalCountSql(originalSql);
	}

	/**
	 * 查询SQL拼接Order By.
	 * @param originalSql 需要拼接的SQL
	 * @return ignore
	 */
	public String concatOrderBy(String originalSql, List<OrderItem> orderList) {
		try {
			Select selectBody = (Select) JsqlParserGlobal.parse(originalSql);
			switch (selectBody) {
				case PlainSelect plainSelect -> {
					List<OrderByElement> orderByElements = plainSelect.getOrderByElements();
					List<OrderByElement> orderByElementsReturn = addOrderByElements(orderList, orderByElements);
					plainSelect.setOrderByElements(orderByElementsReturn);
					return plainSelect.toString();
				}
				case SetOperationList setOperationList -> {
					List<OrderByElement> orderByElements = setOperationList.getOrderByElements();
					List<OrderByElement> orderByElementsReturn = addOrderByElements(orderList, orderByElements);
					setOperationList.setOrderByElements(orderByElementsReturn);
					return setOperationList.toString();
				}
				case null, default -> {
					return originalSql;
				}
			}
		}
		catch (JSQLParserException e) {
			log.error("failed to concat orderBy from IPage, exception:\n", e.getCause());
		}
		catch (Exception e) {
			log.error("failed to concat orderBy from IPage, exception:\n", e);
		}
		return originalSql;
	}

	protected List<OrderByElement> addOrderByElements(List<OrderItem> orderList, List<OrderByElement> orderByElements) {
		List<OrderByElement> additionalOrderBy = orderList.stream()
			.filter(item -> StringUtils.isNotBlank(item.getColumn()))
			.map(item -> {
				OrderByElement element = new OrderByElement();
				element.setExpression(new Column(item.getColumn()));
				element.setAsc(item.isAsc());
				element.setAscDescPresent(true);
				return element;
			})
			.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(orderByElements)) {
			return additionalOrderBy;
		}
		// github pull/3550 优化排序，比如：默认 order by id 前端传了name排序，设置为 order by name,id
		additionalOrderBy.addAll(orderByElements);
		return additionalOrderBy;
	}

	/**
	 * 处理超出分页条数限制,默认归为限制数.
	 * @param page IPage
	 */
	protected void handlerLimit(IPage<?> page, Long limit) {
		final long size = page.getSize();
		if (limit != null && limit > 0 && (size > limit || size < 0)) {
			page.setSize(limit);
		}
	}

	@Override
	public void setProperties(Properties properties) {
		PropertyMapper.newInstance(properties)
			.whenNotBlank("overflow", Boolean::parseBoolean, this::setOverflow)
			.whenNotBlank("dbType", DbType::getDbType, this::setDbType)
			.whenNotBlank("dialect", ClassUtils::newInstance, this::setDialect)
			.whenNotBlank("maxLimit", Long::parseLong, this::setMaxLimit)
			.whenNotBlank("optimizeJoin", Boolean::parseBoolean, this::setOptimizeJoin);
	}

}
