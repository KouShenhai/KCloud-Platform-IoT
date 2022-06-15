package io.laokou.common.user;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Data
public class UserDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String username;
    private String imgUrl;
    private Integer superAdmin;
    private Integer status;
    private String email;
    private String mobile;
    private String password;
    private String wxOpenid;
    private String zfbOpenid;
    private String wxgzhOpenid;
    private List<Long> roleIds;
    private List<String> permissionsList;

}
