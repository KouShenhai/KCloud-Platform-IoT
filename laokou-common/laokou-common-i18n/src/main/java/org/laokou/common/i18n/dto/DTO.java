package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * Data Transfer object, including Command, Query and Response, Command and Query is CQRS
 * concept.
 *
 * @author Frank Zhang 2020.11.13
 *
 */
@Schema(name = "DTO", description = "数据传输对象")
public abstract class DTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

}
