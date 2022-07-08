package io.laokou.admin.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("boot_zfb_user")
public class ZfbUserDO {

    /**
     * 支付宝用户唯一标识
     */
    private String id;

    /**
     * 性别
     */
    private String gender;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 头像
     */
    private String avatar;

}
