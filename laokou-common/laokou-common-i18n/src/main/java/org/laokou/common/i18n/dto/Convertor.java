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

import java.util.List;

/**
 * @author laokou
 */
public interface Convertor<C, E, D> {

	E toEntity(C c);

	D toDataObject(E e);

	D toDataObj(C c);

	E convertEntity(D d);

	List<E> convertEntityList(List<D> list);

	C convertClientObj(D d);

	List<C> convertClientObjList(List<D> list);

	C convertClientObject(E e);

	List<C> convertClientObjectList(List<E> list);

}
