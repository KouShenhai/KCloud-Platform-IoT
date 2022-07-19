package io.laokou.admin.infrastructure.pipeline;

import io.laokou.admin.interfaces.dto.MessageDTO;

public interface BusinessProcess<T extends MessageDTO> {

    void process(T dto);

}
