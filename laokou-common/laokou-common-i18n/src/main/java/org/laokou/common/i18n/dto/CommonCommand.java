package org.laokou.common.i18n.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonCommand extends Command {

	@Serial
	private static final long serialVersionUID = 2478790090537077799L;

	private Long operator;

	private Integer version;

}
