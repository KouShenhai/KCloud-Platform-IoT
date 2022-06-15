package io.laokou.common.utils;
import cn.hutool.core.codec.Base64Encoder;
import java.io.IOException;
import java.io.InputStream;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/10/13 0013 下午 10:23
 */
public class Base64Util {

    public static String convertBase64(InputStream in) {
        try {
            byte[] buf = new byte[in.available()];
            in.read(buf);
            return Base64Encoder.encode(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
