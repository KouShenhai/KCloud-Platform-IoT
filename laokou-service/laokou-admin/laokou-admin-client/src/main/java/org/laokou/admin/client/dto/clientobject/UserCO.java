package org.laokou.admin.client.dto.clientobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.laokou.common.i18n.dto.ClientObject;

import java.io.Serial;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = 2478790090537077784L;

	private Long id;

	@NotBlank(message = "用户名不为空")
	private String username;

	@NotNull(message = "请选择用户状态")
	private Integer status;

	private List<Long> roleIds;

	private String password;

	private String avatar;

	private String mail;

	private String mobile;

	private Long editor;

	private Long deptId;

}
