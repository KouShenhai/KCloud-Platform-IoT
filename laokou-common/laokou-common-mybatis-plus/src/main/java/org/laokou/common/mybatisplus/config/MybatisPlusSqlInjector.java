/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteBatchByIds;

import java.util.List;

/**
 * @author laokou
 */
public class MybatisPlusSqlInjector extends DefaultSqlInjector {

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
		// https://baomidou.com/pages/49cc81/#mapper-%E5%B1%82-%E9%80%89%E8%A3%85%E4%BB%B6
		List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
		// 去掉 update 填充的字段
		methodList.add(new InsertBatchSomeColumn(insert -> insert.getFieldFill() != FieldFill.UPDATE));
		methodList.add(new AlwaysUpdateSomeColumnById());
		methodList.add(new LogicDeleteBatchByIds());
		return methodList;
	}

}
