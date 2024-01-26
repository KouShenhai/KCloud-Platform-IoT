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

package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author laokou
 */
@Schema(name = "Convertor", description = "对象转换器")
public interface Convertor<C, E, D> {

	/**
	 * ClientObject 转 Entity.
	 * @param c ClientObject
	 * @return Entity
	 */
	E toEntity(C c);

	/**
	 * Entity 转 DataObject.
	 * @param e Entity
	 * @return DataObject
	 */
	D toDataObject(E e);

	/**
	 * ClientObject 转 DataObject.
	 * @param c ClientObject
	 * @return DataObject
	 */
	D toDataObj(C c);

	/**
	 * DataObject 转 Entity.
	 * @param d DataObject
	 * @return Entity
	 */
	E convertEntity(D d);

	/**
	 * DataObject List 转 Entity List.
	 * @param list DataObject List
	 * @return Entity List
	 */
	List<E> convertEntityList(List<D> list);

	/**
	 * DataObject 转 ClientObject.
	 * @param d DataObject
	 * @return ClientObject
	 */
	C convertClientObj(D d);

	/**
	 * DataObject List 转 ClientObject List.
	 * @param list DataObject List
	 * @return ClientObject List
	 */
	List<C> convertClientObjList(List<D> list);

	/**
	 * Entity 转 ClientObject.
	 * @param e Entity
	 * @return ClientObject
	 */
	C convertClientObject(E e);

	/**
	 * Entity List 转 ClientObject List.
	 * @param list Entity List
	 * @return ClientObject List
	 */
	List<C> convertClientObjectList(List<E> list);

}
