package org.laokou.common.i18n.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonCommand extends Command {

    private Long operator;

}
