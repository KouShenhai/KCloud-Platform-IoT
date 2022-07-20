package io.laokou.admin.interfaces.vo;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/20 0020 下午 3:52
 */
@Data
public class MessageVO implements Serializable {

    private Long id;

    private String title;

    private String username;

    private Date createDate;

}
