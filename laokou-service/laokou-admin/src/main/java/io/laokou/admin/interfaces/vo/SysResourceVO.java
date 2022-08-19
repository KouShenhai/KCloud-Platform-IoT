package io.laokou.admin.interfaces.vo;

import lombok.Data;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:45
 */
@Data
public class SysResourceVO {

    private Long id;
    private String title;
    private String author;
    private String uri;
    private Integer status;
    private String code;
    private String remark;
    private String tags;
    private String processInstanceId;

}
