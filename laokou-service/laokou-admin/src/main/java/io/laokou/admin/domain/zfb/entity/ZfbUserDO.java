package io.laokou.admin.domain.zfb.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import lombok.Data;

@Data
@TableName("boot_zfb_user")
public class ZfbUserDO extends BaseDO {

    /**
     * 支付宝用户唯一标识
     */
    private String openid;

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
