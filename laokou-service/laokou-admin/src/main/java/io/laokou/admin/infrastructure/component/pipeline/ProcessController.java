package io.laokou.admin.infrastructure.component.pipeline;

import io.laokou.admin.interfaces.dto.MessageDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProcessController {

    private ProcessTemplate processTemplate;

    public void process(MessageDTO dto) {
        List<BusinessProcess> processList = processTemplate.getProcessList();
        for (BusinessProcess process : processList) {
            process.process(dto);
        }
    }

}
