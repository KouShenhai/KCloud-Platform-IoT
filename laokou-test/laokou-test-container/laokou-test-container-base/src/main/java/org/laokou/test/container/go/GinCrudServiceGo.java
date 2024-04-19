/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.test.container.go;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.TemplateUtil;

import java.util.Map;

@Slf4j
public class GinCrudServiceGo implements Crud {

	public static void main(String[] args) {
		String inst = "pro";
		String upper = "Protocol";
		String lower = "protocol";
		Map<String, Object> params = Map.of("inst", inst, "upper", upper, "lower", lower);
		Crud crud = new GinCrudServiceGo();
		StringBuilder s = new StringBuilder();
		s.append("\n").append(crud.imp(params));
		s.append("\n").append(crud.type(params));
		s.append("\n").append(crud.create(params));
		s.append("\n").append(crud.remove(params));
		s.append("\n").append(crud.modify(params));
		s.append("\n").append(crud.findList(params));
		s.append("\n").append(crud.findById(params));
		s.append("\n").append(crud.findOptionList(params));
		log.info("\n{}", s);
	}

	@Override
	@SneakyThrows
	public String create(Map<String, Object> params) {
		String str = """
				func (service *${upper}Service) Create${upper}(${inst} system.AtSys${upper}) error {
					${inst}.CreateDate = time.Now()
					${inst}.UpdateDate = time.Now()
					return global.Db.Create(&${inst}).Error
				}
				""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String remove(Map<String, Object> params) {
		String str = """
				func (service *${upper}Service) Remove${upper}(id int) (err error) {
					var ${inst} system.AtSys${upper}
					return global.Db.Model(&${inst}).Where("id = ?", id).Update("del_flag", "1").Error
				}
				""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String modify(Map<String, Object> params) {
		String str = """
				func (service *${upper}Service) Modify${upper}(${inst} system.AtSys${upper}) (err error) {
					${lower}Map := map[string]interface{}{}
					return global.Db.Updates(${lower}Map).Error
				}
				""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String imp(Map<String, Object> params) {
		String str = """
				import (
					"lc-base-frame/global"
					"lc-base-frame/model/common/request"
					"lc-base-frame/model/system"
					"time"
				)
				""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String findList(Map<String, Object> params) {
		String str = """
				func (service *${upper}Service) Find${upper}List(info request.PageInfo) (list interface{}, total int64, err error) {
					limit := info.PageSize
					offset := info.PageSize * (info.Page - 1)
					db := global.Db.Model(&system.AtSys${upper}{})
					var ${lower}List []system.AtSys${upper}
					err = db.Count(&total).Error
					if err != nil {
						return
					}
					err = db.Limit(limit).Offset(offset).Find(&${lower}List).Error
					return ${lower}List, total, err
				}
				""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String findById(Map<String, Object> params) {
		String str = """
				func (service *${upper}Service) Find${upper}ById(id int64) (${inst} system.AtSys${upper}, err error) {
					err = global.Db.Where("id = ?", id).First(&${inst}).Error
					return
				}
				""";
		return TemplateUtil.getContent(str, params);
	}

	@SneakyThrows
	@Override
	public String findOptionList(Map<String, Object> params) {
		String str = """
				func (service *${upper}Service) Find${upper}OptionList(search request.Search) (list interface{}, err error) {
					keyword := search.Keyword
					var ${lower}List []system.${upper}Option
					sql := " del_flag = 0 "
					if keyword != "" {
						sql = " name like '%" + keyword + "%'"
					}
					err = global.Db.Model(&system.AtSys${upper}{}).Select([]string{"name", "id"}).Where(sql).Find(&${lower}List).Error
					return ${lower}List, err
				}
							""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String type(Map<String, Object> params) {
		String str = """
				type ${upper}Service struct{}
				""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	public String router(Map<String, Object> params) {
		return null;
	}

}
