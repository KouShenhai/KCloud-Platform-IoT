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
package org.laokou.common.core.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author laokou
 */
@UtilityClass
public class ObjectUtil extends ObjectUtils {

    @SafeVarargs
    public <T> boolean allChecked(Predicate<T> predicate, T... ts) {
        return Stream.of(ts).allMatch(predicate);
    }

    @SafeVarargs
    public <T> boolean anyChecked(Predicate<T> predicate, T... ts) {
        return Stream.of(ts).anyMatch(predicate);
    }

}
