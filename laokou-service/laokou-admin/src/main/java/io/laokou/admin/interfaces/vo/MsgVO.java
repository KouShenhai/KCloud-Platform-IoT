package io.laokou.admin.interfaces.vo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/1/24 0024 上午 11:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MsgVO {
    private String data;
}
