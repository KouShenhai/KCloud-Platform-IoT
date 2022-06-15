package io.laokou.admin.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("boot_zfb_user")
public class ZfbUserDO implements Serializable {

    private String id;
    private String gender;
    private String province;
    private String city;
    private String avatar;

}
