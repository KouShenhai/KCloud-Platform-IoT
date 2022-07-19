package io.laokou.admin.interfaces.dto;

import lombok.Data;

import java.util.Set;

@Data
public class MessageDTO {

    /**
     * 接收者
     */
    private Set<String> receiver;

    private String title;

    private String content;

}
