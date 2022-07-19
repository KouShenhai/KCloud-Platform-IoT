package io.laokou.admin.infrastructure.config;
import io.laokou.admin.interfaces.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
/**
 * @author  Kou Shenhai
 */
@Component
public class WebsocketStompServer {

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 一对多推送
     */
    @MessageMapping("/one-to-many")
    public void oneToMany(MessageVO vo) {
        template.convertAndSend("/one-to-many", vo);
    }

    /**
     * 一对一推送
     */
    @MessageMapping("/one-to-one")
    public void oneToOne(String userId, MessageVO vo) {
        template.convertAndSendToUser(userId, "/one-to-one", vo);
    }
}
