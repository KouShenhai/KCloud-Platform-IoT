package io.laokou.admin.infrastructure.component.handler;

import io.laokou.admin.interfaces.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class BaseHandler implements Handler{

    @Autowired
    private HandleHolder handleHolder;

    protected Integer channelCode;

    @PostConstruct
    public void init() {
        handleHolder.putHandler(channelCode,this);
    }

    @Override
    public void doHandler(MessageDTO dto) {
        handler(dto);
    }

    public abstract boolean handler(MessageDTO dto);

}
