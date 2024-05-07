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

package org.laokou.common.i18n.dto;

import java.util.List;

/**
 * 对象转换器.
 *
 * @author laokou
 */
public interface Convertor<CO, Entity, DO> {

	/**
	 * Entity 转 DataObject.
	 * @param e Entity
	 * @return DataObject
	 */
	DO toDataObject(Entity e);

	/**
	 * DataObject 转 Entity.
	 * @param d DataObject
	 * @return Entity
	 */
	Entity convertEntity(DO d);

	/**
	 * DataObject List 转 Entity List.
	 * @param list DataObject List
	 * @return Entity List
	 */
	List<Entity> convertEntityList(List<DO> list);

	/**
	 * ClientObject 转 Entity.
	 * @param c ClientObject
	 * @return Entity
	 */
	Entity toEntity(CO c);

	/**
	 * ClientObject 转 DataObject.
	 * @param c ClientObject
	 * @return DataObject
	 */
	DO toDataObj(CO c);

	/**
	 * DataObject 转 ClientObject.
	 * @param d DataObject
	 * @return ClientObject
	 */
	CO convertClientObj(DO d);

	/**
	 * DataObject List 转 ClientObject List.
	 * @param list DataObject List
	 * @return ClientObject List
	 */
	List<CO> convertClientObjList(List<DO> list);

	/**
	 * Entity 转 ClientObject.
	 * @param e Entity
	 * @return ClientObject
	 */
	CO convertClientObject(Entity e);

	/**
	 * Entity List 转 ClientObject List.
	 * @param list Entity List
	 * @return ClientObject List
	 */
	List<CO> convertClientObjectList(List<Entity> list);

}
