package org.laokou.auth.dto.clientobject;

import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

@Data
public class TenantCO extends ClientObject {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 租户名称.
	 */
	private String name;

}
