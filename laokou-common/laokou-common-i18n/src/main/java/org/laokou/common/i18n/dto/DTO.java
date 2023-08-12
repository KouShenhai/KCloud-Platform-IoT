package org.laokou.common.i18n.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * Data Transfer object, including Command, Query and Response, Command and Query is CQRS
 * concept.
 *
 * @author Frank Zhang 2020.11.13
 *
 */
public abstract class DTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

}
