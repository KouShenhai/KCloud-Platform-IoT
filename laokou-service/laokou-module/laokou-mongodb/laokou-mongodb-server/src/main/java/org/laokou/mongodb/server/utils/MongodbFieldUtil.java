package org.laokou.mongodb.server.utils;

import org.laokou.common.core.utils.JacksonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author laokou
 */
public class MongodbFieldUtil {

    private static final Map<String,Class<?>> CLASS_MAP = new HashMap<>(16);

    static {

    }

    public static Object getObj(final String collectionName,final String jsonData) {
        final Class<?> clazz = CLASS_MAP.get(collectionName);
        return JacksonUtil.toBean(jsonData,clazz);
    }

    public static List<? extends Object> getObjList(final String collectionName,final String jsonData) {
        final Class<?> clazz = CLASS_MAP.get(collectionName);
        return JacksonUtil.toList(jsonData, clazz);
    }

    public static Class<?> getClazz(final String collectionName) {
        return CLASS_MAP.get(collectionName);
    }

}
