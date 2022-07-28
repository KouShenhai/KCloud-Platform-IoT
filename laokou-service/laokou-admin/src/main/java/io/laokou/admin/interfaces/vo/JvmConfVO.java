package io.laokou.admin.interfaces.vo;

import io.laokou.common.utils.DateUtil;

import java.lang.management.ManagementFactory;

/**
 * JVM相关信息
 *
 * @author ruoyi
 */
public class JvmConfVO
{
    /**
     * 当前JVM占用的内存�?�数(M)
     */
    private double total;

    /**
     * JVM�?大可用内存�?�数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    public double getTotal()
    {
        return ArithVO.div(total, (1024 * 1024), 2);
    }

    public void setTotal(double total)
    {
        this.total = total;
    }

    public double getMax()
    {
        return ArithVO.div(max, (1024 * 1024), 2);
    }

    public void setMax(double max)
    {
        this.max = max;
    }

    public double getFree()
    {
        return ArithVO.div(free, (1024 * 1024), 2);
    }

    public void setFree(double free)
    {
        this.free = free;
    }

    public double getUsed()
    {
        return ArithVO.div(total - free, (1024 * 1024), 2);
    }

    public double getUsage()
    {
        return ArithVO.mul(ArithVO.div(total - free, total, 4), 100);
    }

    /**
     * 获取JDK名称
     */
    public String getName()
    {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getHome()
    {
        return home;
    }

    public void setHome(String home)
    {
        this.home = home;
    }

    /**
     * JDK启动时间
     */
    public String getStartTime()
    {
        return DateUtil.parseDateToStr(DateUtil.YYYY_MM_DD_HH_MM_SS, DateUtil.getServerStartDate());
    }

    /**
     * JDK运行时间
     */
    public String getRunTime()
    {
        return DateUtil.getDatePoor(DateUtil.getNowDate(), DateUtil.getServerStartDate());
    }
}
