package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * This is the object communicate with Client. The clients could be view layer or other
 * HSF Consumers.
 *
 * @author fulan.zjf 2017-10-27 PM 12:19:15
 */
@Schema(name = "ClientObject", description = "客户端通信对象")
public abstract class ClientObject implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

}
