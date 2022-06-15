package io.laokou.common.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 分布式锁工具类
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/7/1 0001 上午 8:18
 */
public class LockUtil {

    /**
     * 获取本机网卡地址
     * @return
     */
    public static String getLocalMAC() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            //获取网络接口对象(即网卡),并得到mac地址，MAC地址存在一个byte数组中
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            //把字符串所有小写都改成大写成为正规的mac地址并返回
            return sb.toString().toUpperCase().replaceAll("-","");
        } catch (UnknownHostException | SocketException e) {
            throw new IllegalStateException("getLocalMAC error");
        }
    }

    public static String getJvmPid() {
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        int indexOf = pid.indexOf("@");
        if (indexOf != -1) {
            pid = pid.substring(0,indexOf);
            return pid;
        }
        throw new IllegalStateException("ManagementFactory error");
    }

}
