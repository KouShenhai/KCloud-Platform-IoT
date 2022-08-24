package io.laokou.admin.interfaces.dto;

import lombok.Data;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:46
 */
@Data
public class SysResourceDTO {
    private Long id;
    private String title;
    private String uri;
    private String code;
    private String remark;
    private String tags;
    private String md5;
}
