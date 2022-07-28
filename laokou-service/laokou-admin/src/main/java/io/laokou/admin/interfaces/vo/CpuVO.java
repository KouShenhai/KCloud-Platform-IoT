package io.laokou.admin.interfaces.vo;


/**
 * CPU相关信息
 *
 * @author ruoyi
 */
public class CpuVO
{
    /**
     * 核心�?
     */
    private int cpuNum;

    /**
     * CPU总的使用�?
     */
    private double total;

    /**
     * CPU系统使用�?
     */
    private double sys;

    /**
     * CPU用户使用�?
     */
    private double used;

    /**
     * CPU当前等待�?
     */
    private double wait;

    /**
     * CPU当前空闲�?
     */
    private double free;

    public int getCpuNum()
    {
        return cpuNum;
    }

    public void setCpuNum(int cpuNum)
    {
        this.cpuNum = cpuNum;
    }

    public double getTotal()
    {
        return ArithVO.round(ArithVO.mul(total, 100), 2);
    }

    public void setTotal(double total)
    {
        this.total = total;
    }

    public double getSys()
    {
        return ArithVO.round(ArithVO.mul(sys / total, 100), 2);
    }

    public void setSys(double sys)
    {
        this.sys = sys;
    }

    public double getUsed()
    {
        return ArithVO.round(ArithVO.mul(used / total, 100), 2);
    }

    public void setUsed(double used)
    {
        this.used = used;
    }

    public double getWait()
    {
        return ArithVO.round(ArithVO.mul(wait / total, 100), 2);
    }

    public void setWait(double wait)
    {
        this.wait = wait;
    }

    public double getFree()
    {
        return ArithVO.round(ArithVO.mul(free / total, 100), 2);
    }

    public void setFree(double free)
    {
        this.free = free;
    }
}
