package io.laokou.common.utils;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/7/11 0011 上午 10:03
 */
public class HashUtil {

    /**
     * 获取hash值
     * @param node
     * @return
     */
    public static int getHash(String node) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < node.length(); i++) {
            hash = (hash ^ node.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

}
