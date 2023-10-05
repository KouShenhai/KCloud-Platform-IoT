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

package org.laokou.common.mybatisplus.dsl;

import org.laokou.common.i18n.dto.DSL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author laokou
 */
public class OrderBy extends DSL {

    private final List<SortColumn> COLUMNS = new ArrayList<>();

    public OrderBy(Collection<? extends SortColumn> columns) {
        this.COLUMNS.addAll(columns);
    }

    public List<SortColumn> columns() {
        return COLUMNS;
    }

    public static OrderBy of(Collection<? extends SortColumn> columns) {
        return new OrderBy(columns);
    }

}
