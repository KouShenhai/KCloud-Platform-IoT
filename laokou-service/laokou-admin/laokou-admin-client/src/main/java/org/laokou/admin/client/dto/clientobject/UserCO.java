package org.laokou.admin.client.dto.clientobject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.laokou.common.i18n.dto.ClientObject;

import java.io.Serial;
import java.util.List;

/**
 * @author laokou
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = 2478790090537077784L;

	private Long id;

	private String username;

	private Integer status;

	private List<Long> roleIds;

	private String password;

	private String avatar;

	private String mail;

	private String mobile;

	private Long editor;

	private Long deptId;

}
