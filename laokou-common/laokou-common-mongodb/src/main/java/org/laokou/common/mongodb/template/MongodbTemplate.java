/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.mongodb.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.mongodb.clientobject.SearchCO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author laokou
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MongodbTemplate {

	private final MongoTemplate mongoTemplate;

	/**
	 * 保存数据.
	 * @param collectionName 集合名称
	 * @param objData obj对象
	 */
	public void insert(String collectionName, Object objData) {
		mongoTemplate.save(objData, collectionName);
	}

	public Object get(Class<?> clazz, String id) {
		long startTime = IdGenerator.SystemClock.now();
		final Object obj = mongoTemplate.findById(id, clazz);
		log.info("消耗时间：{}ms", (IdGenerator.SystemClock.now() - startTime));
		return obj;
	}

	public Page<Map> query(SearchCO queryCO) {
		final long startTime = IdGenerator.SystemClock.now();
		final Query query = new Query();
		final String collectionName = queryCO.getCollectionName();
		final Criteria criteria = new Criteria();
		final List<SearchCO.Search> likeSearchList = queryCO.getLikeSearchList();
		Integer pageNum = queryCO.getPageNum();
		Integer pageSize = queryCO.getPageSize();
		final int size = likeSearchList.size();
		if (size > 0) {
			Criteria[] likeCriteria = new Criteria[size];
			for (int j = 0; j < size; j++) {
				final SearchCO.Search search = likeSearchList.get(j);
				final String machValue = search.getValue();
				final String machKey = search.getField();
				final Pattern pattern = Pattern.compile("^.*" + machValue + ".*$", Pattern.CASE_INSENSITIVE);
				likeCriteria[j] = Criteria.where(machKey).regex(pattern);
			}
			criteria.andOperator(likeCriteria);
		}
		query.addCriteria(criteria);
		// 分页
		int start = 0;
		int end = 10000;
		if (queryCO.isNeedPage()) {
			start = (pageNum - 1) * pageSize;
			end = pageSize;
		}
		query.skip(start);
		query.limit(end);
		List<Map> result = mongoTemplate.find(query, Map.class, collectionName);
		long total = mongoTemplate.count(query, collectionName);
		// Datas<Map> datas = new Datas<>();
		// datas.setRecords(result);
		// datas.setTotal(total);
		return null;
	}

	public void insertBatch(String collectionName, List<? extends Object> dataList) {
		mongoTemplate.insert(dataList, collectionName);
	}

	public void deleteAll(String collectionName) {
		mongoTemplate.dropCollection(collectionName);
	}

}
