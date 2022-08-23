package io.laokou.admin.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import lombok.Data;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:51
 */
@Data
@TableName("boot_sys_resource")
public class SysResourceDO extends BaseDO {

    private String title;
    private String author;
    private String uri;
    /**
     * 0待审核  1审核中 2已完成
     */
    private Integer status;
    private String code;
    private String remark;
    private String tags;
    /**
     * 流程id
     */
    private String processInstanceId;
    private String md5;

}
