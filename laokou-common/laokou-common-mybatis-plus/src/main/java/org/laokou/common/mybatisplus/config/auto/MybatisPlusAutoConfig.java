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
package org.laokou.common.mybatisplus.config.auto;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.laokou.common.mybatisplus.config.DataFilterInterceptor;
import org.laokou.common.mybatisplus.config.DynamicTableNameHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * mybatis-plus的配置
 *
 * @author laokou
 */
@AutoConfiguration
@ConditionalOnClass({ DataSource.class })
@ComponentScan("org.laokou.common.mybatisplus")
public class MybatisPlusAutoConfig {

	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		// 数据权限
		mybatisPlusInterceptor.addInnerInterceptor(new DataFilterInterceptor());
		// 分页插件
		mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
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

	@Bean(name = "transactionTemplate")
	@ConditionalOnMissingBean(TransactionOperations.class)
	public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.setReadOnly(false);
		// 新建事务
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 事务超时时间,单位s
		transactionTemplate.setTimeout(120);
		transactionTemplate.setName("laokou-transaction-template");
		return transactionTemplate;
	}

}
