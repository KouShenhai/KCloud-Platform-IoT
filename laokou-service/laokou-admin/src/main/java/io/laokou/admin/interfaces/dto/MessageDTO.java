package io.laokou.admin.interfaces.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class MessageDTO {

    /**
     * 接收者
     */
    private Set<String> receiver;

    @NotBlank(message = "请输入标题")
    private String title;

    @NotBlank(message = "请输入内容")
    private String content;

    @NotNull(message = "发送渠道不为空")
    private Integer sendChannel;

}
