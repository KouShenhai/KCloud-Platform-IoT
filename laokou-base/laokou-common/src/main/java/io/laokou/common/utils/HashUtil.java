package io.laokou.common.utils;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/7/11 0011 上午 10:03
 */
public class HashUtil {

    /**
     * 获取hash值
     * @param key
     * @return
     */
    public static int getHash(String key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

}
