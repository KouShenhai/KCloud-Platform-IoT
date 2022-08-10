package io.laokou.oauth2.exception;
import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/4/23 0023 上午 11:44
 */
@Data
@AllArgsConstructor
public class RenHttpResult {

    private String code;

    private String msg;

}
