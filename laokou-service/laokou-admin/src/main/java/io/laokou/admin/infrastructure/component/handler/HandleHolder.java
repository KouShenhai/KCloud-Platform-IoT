package io.laokou.admin.infrastructure.component.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HandleHolder {

    private Map<Integer,Handler> handlers = new HashMap<>(16);

    public void putHandler(Integer channelCode,Handler handler) {
        handlers.put(channelCode,handler);
    }

    public Handler route(Integer channelCode) {
        return handlers.get(channelCode);
    }

}
