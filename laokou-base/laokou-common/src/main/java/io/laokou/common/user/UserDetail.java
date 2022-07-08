package io.laokou.common.user;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Data
public class UserDetail {
    private static final long serialVersionUID = 1L;
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    private String username;
    private String imgUrl;
    private Integer superAdmin;
    private Integer status;
    private String email;
    private String mobile;
    private String password;
    private String zfbOpenid;
    private List<String> permissionsList;

}
