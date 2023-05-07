/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.common.core.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * @author laokou
 */
public class EmptyUtil {

    public static boolean isNotEmpty(Object obj) {
        if (obj instanceof Map<?,?> map) {
            return MapUtils.isNotEmpty(map);
        }
        if (obj instanceof String str) {
            return StringUtil.isNotEmpty(str);
        }
        if (obj instanceof Object[] array) {
            return ArrayUtils.isNotEmpty(array);
        }
        if (obj instanceof Collection<?> collection) {
            return CollectionUtils.isNotEmpty(collection);
        }
        return obj != null;
    }

    public static boolean isEmpty(Object obj) {
        return !isNotEmpty(obj);
    }

    public static void main(String[] args) {
        String[] arr = {"3"};
        List<String> list = List.of("1");
        Map<String, String> stringStringMap = Map.of("1", "1");
        System.out.println(isNotEmpty(arr));
        System.out.println(isNotEmpty(list));
        System.out.println(isNotEmpty(new String[]{}));
        System.out.println(isNotEmpty(new ArrayList<>()));
        System.out.println(isNotEmpty(stringStringMap));
        System.out.println(isNotEmpty(new HashMap<>()));
        System.out.println(isNotEmpty(null));
        System.out.println(isNotEmpty(""));
        System.out.println(isNotEmpty("11"));
    }

}
