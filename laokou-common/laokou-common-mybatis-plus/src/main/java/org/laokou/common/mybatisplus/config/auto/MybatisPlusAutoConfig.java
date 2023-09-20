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
package org.laokou.common.mybatisplus.config.auto;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.SneakyThrows;
import org.laokou.common.mybatisplus.config.DataFilterInterceptor;
import org.laokou.common.mybatisplus.config.DynamicTableNameHandler;
import org.laokou.common.mybatisplus.config.MybatisPlusSqlInjector;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.net.InetAddress;

/**
 * mybatis-plus配置
 *
 * @author laokou
 */
@AutoConfiguration
@ConditionalOnClass({ DataSource.class })
public class MybatisPlusAutoConfig {

	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		// 数据权限
		mybatisPlusInterceptor.addInnerInterceptor(new DataFilterInterceptor());
		// 分页插件
		mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor());
		// 乐观锁
		mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		// 防止全表更新与删除
		mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
		// 动态表名
		DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
		dynamicTableNameInnerInterceptor.setTableNameHandler(new DynamicTableNameHandler());
		mybatisPlusInterceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
		return mybatisPlusInterceptor;
	}

	/**
	 * <a href="https://baomidou.com/pages/568eb2/#spring-boot">...</a>
	 * 生成雪花算法就是使用的这个，请查看{@link IdWorker}
	 * 为什么要修改这个配置，集群环境可能会重复，网上的解决方案是加网卡信息（用zookeeper生成分布式ID也是可以哦，重写生成雪花算法的逻辑，mp官网支持自定义雪花算法生成）
	 */
	@Bean
	@SneakyThrows
	public IdentifierGenerator identifierGenerator() {
		// 查看 DefaultIdentifierGenerator 可以自定义网卡信息哦，话不多说，就是怼源码
		return new DefaultIdentifierGenerator(InetAddress.getLocalHost());
	}

	@Bean(name = "transactionTemplate")
	@ConditionalOnMissingBean(TransactionOperations.class)
	public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		// 只读事务
		transactionTemplate.setReadOnly(false);
		// 新建事务
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 默认数据库隔离级别
		transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
		// 事务超时时间,单位s
		transactionTemplate.setTimeout(120);
		// 事务名称
		transactionTemplate.setName("laokou-transaction-template");
		return transactionTemplate;
	}

	/**
	 * 自定义sql注入器
	 */
	@Bean
	public MybatisPlusSqlInjector mybatisPlusSqlInjector() {
		return new MybatisPlusSqlInjector();
	}

	/**
	 * 解除每页500条限制
	 */
	private PaginationInnerInterceptor paginationInnerInterceptor() {
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		// -1 不受限制
		paginationInnerInterceptor.setMaxLimit(-1L);
		// 溢出总页数后是进行处理，查看源码就知道是干啥的
		paginationInnerInterceptor.setOverflow(true);
		return paginationInnerInterceptor;
	}

}
