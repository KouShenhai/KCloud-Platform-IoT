/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
	 * ClientObject 转 Domain.
	 * @param c ClientObject
	 * @return Domain
	 */
	E toEntity(C c);

	/**
	 * Domain 转 DataObject.
	 * @param e Domain
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
	 * DataObject 转 Domain.
	 * @param d DataObject
	 * @return Domain
	 */
	E convertEntity(D d);

	/**
	 * DataObject List 转 Domain List.
	 * @param list DataObject List
	 * @return Domain List
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
	 * Domain 转 ClientObject.
	 * @param e Domain
	 * @return ClientObject
	 */
	C convertClientObject(E e);

	/**
	 * Domain List 转 ClientObject List.
	 * @param list Domain List
	 * @return ClientObject List
	 */
	List<C> convertClientObjectList(List<E> list);

}
