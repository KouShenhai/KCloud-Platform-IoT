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

package org.laokou.common.mybatisplus.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;
import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * mybatis-plus配置.
 *
 * @author laokou
 */
@AutoConfiguration
@ConditionalOnClass({ DataSource.class })
@MapperScan("org.laokou.common.mybatisplus.mapper")
public class MybatisPlusAutoConfig {

	static {
		JsqlParserGlobal.setJsqlParseCache(new ForySerialCaffeineJsqlParseCache(
				Caffeine.newBuilder().maximumSize(4096).expireAfterWrite(10, TimeUnit.MINUTES).build()));
	}

	@Bean
	public ConfigurationCustomizer configurationCustomizer(MybatisPlusExtProperties mybatisPlusExtProperties) {
		return configuration -> {
			// SQL监控
			SqlMonitorInterceptor sqlMonitorInterceptor = new SqlMonitorInterceptor(mybatisPlusExtProperties);
			configuration.addInterceptor(sqlMonitorInterceptor);
		};
	}

	// @formatter:off
	/**
	 * 注意: 使用多个功能需要注意顺序关系,建议使用如下顺序
	 * 											- 多租户，动态表名
	 * 											- 分页，乐观锁
	 * 											- sql性能规范，防止全表更新与删除
	 * 总结：对 sql进行单次改造的优先放入,不对 sql 进行改造的最后放入.
	 * @param mybatisPlusExtProperties mybatis配置
	 */
	// @formatter:on
	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor(MybatisPlusExtProperties mybatisPlusExtProperties) {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		// 数据权限插件
		// interceptor.addInnerInterceptor(new DataFilterInterceptor());
		// 多租户插件
		if (mybatisPlusExtProperties.getTenant().isEnabled()) {
			interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(
					new GlobalTenantLineHandler(mybatisPlusExtProperties.getTenant().getIgnoreTables())));
		}
		// 动态表名插件
		interceptor.addInnerInterceptor(new DynamicTableNameInnerInterceptor(new DynamicTableNameHandler()));
		// 分页插件
		interceptor.addInnerInterceptor(paginationInnerInterceptor());
		// 乐观锁插件
		interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		// 防止全表更新与删除插件
		interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
		return interceptor;
	}

	@Bean(name = "transactionTemplate")
	@ConditionalOnMissingBean(TransactionOperations.class)
	public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		// 只读事务
		transactionTemplate.setReadOnly(false);
		// 新建事务
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 数据库隔离级别 => 读已提交
		transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		// 事务超时时间，单位s
		transactionTemplate.setTimeout(180);
		// 事务名称
		transactionTemplate.setName("laokou-transaction-template");
		return transactionTemplate;
	}

	/**
	 * 分页. 解除每页500条限制.
	 */
	private PaginationInnerInterceptor paginationInnerInterceptor() {
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		// -1表示不受限制
		paginationInnerInterceptor.setMaxLimit(-1L);
		// 溢出总页数后是进行处理，查看源码就知道是干啥的
		paginationInnerInterceptor.setOverflow(true);
		return paginationInnerInterceptor;
	}

}
