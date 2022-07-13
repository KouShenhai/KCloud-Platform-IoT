package io.laokou.common.utils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import io.laokou.common.exception.CustomException;
import io.laokou.common.exception.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.Map;
/**
 * 校验工具类
 *
 * @author limingze
 * @create: 2022-07-13 09:45
 * @since 1.0.0
 */
public class AssertUtil {

    public static void isBlank(String str, String... params) {
        isBlank(str, ErrorCode.NOT_NULL, params);
    }

    public static void isBlank(String str, Integer code, String... params) {
        if(code == null){
            throw new CustomException(ErrorCode.NOT_NULL, "code");
        }
        if (StringUtils.isBlank(str)) {
            throw new CustomException(code, params);
        }
    }

    public static void isNull(Object object, String... params) {
        isNull(object, ErrorCode.NOT_NULL, params);
    }

    public static void isNull(Object object, Integer code, String... params) {
        if(code == null){
            throw new CustomException(ErrorCode.NOT_NULL, "code");
        }
        if (object == null) {
            throw new CustomException(code, params);
        }
    }

    public static void isArrayEmpty(Object[] array, String... params) {
        isArrayEmpty(array, ErrorCode.NOT_NULL, params);
    }

    public static void isArrayEmpty(Object[] array, Integer code, String... params) {
        if(code == null){
            throw new CustomException(ErrorCode.NOT_NULL, "code");
        }
        if(ArrayUtil.isEmpty(array)){
            throw new CustomException(code, params);
        }
    }

    public static void isListEmpty(List<?> list, String... params) {
        isListEmpty(list, ErrorCode.NOT_NULL, params);
    }

    public static void isListEmpty(List<?> list, Integer code, String... params) {
        if(code == null){
            throw new CustomException(ErrorCode.NOT_NULL, "code");
        }
        if(CollUtil.isEmpty(list)){
            throw new CustomException(code, params);
        }
    }

    public static void isMapEmpty(Map map, String... params) {
        isMapEmpty(map, ErrorCode.NOT_NULL, params);
    }

    public static void isMapEmpty(Map map, Integer code, String... params) {
        if(code == null){
            throw new CustomException(ErrorCode.NOT_NULL, "code");
        }
        if(MapUtil.isEmpty(map)){
            throw new CustomException(code, params);
        }
    }
}
