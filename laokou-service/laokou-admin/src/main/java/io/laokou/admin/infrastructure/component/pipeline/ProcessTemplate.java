package io.laokou.admin.infrastructure.component.pipeline;

import lombok.Data;

import java.util.List;

/**
 * 业务执行模板
 */
@Data
public class ProcessTemplate {

    private List<BusinessProcess> processList;

}
