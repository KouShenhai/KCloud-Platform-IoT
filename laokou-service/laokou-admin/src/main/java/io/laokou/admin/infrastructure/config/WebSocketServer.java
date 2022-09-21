/**
 * Copyright 2020-2022 Kou Shenhai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.laokou.admin.infrastructure.config;
import io.laokou.common.utils.JacksonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import javax.validation.constraints.NotNull;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
/**
 * @author  Kou Shenhai
 */
@Component
@Data
@Slf4j
@ServerEndpoint("/ws/{userId}")
public class WebSocketServer {
    /**
     * 静态变量，用来记录当前在线连接数。设计成线程安全
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set,用来存放每个客户端对应的websocketserver对象
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketServerCopyOnWriteArraySet = new CopyOnWriteArraySet<>();
    /**
     * 与某些客户端的连接会话，需要通过它来给客户打发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private Long userId;
    /**
     * 连接成功后回调方法
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId")Long userId) {
        this.session = session;
        //先设置在添加
        this.userId = userId;
        boolean addFlag = webSocketServerCopyOnWriteArraySet.add(this);
        if (addFlag) {
            addOnlineCount();
        }
        log.info("新加入：{}",userId,",在线人数：{}",getOnlineCount());
    }
    /**
     * 连接关闭调用
     */
    @OnClose
    public void onClose() {
        boolean removeFlag = webSocketServerCopyOnWriteArraySet.remove(this);
        if (removeFlag) {
            subOnlineCount();
        }
         log.info("当前在线人数：{}",getOnlineCount());
    }
    /**
     * 收到客户端消息后调用
     * @param message
     * @param session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message,Session session) throws IOException {
        log.info("收到来自：{}",this.userId,"的消息:{}",message);
        if(StringUtils.isNotBlank(message)) {
            Map<String, Object> map = JacksonUtil.toMap(message, String.class, Object.class);
            log.info("接到数据：{}",message);
            final Long userId = Long.valueOf(map.get("userId").toString());
            sendMessages(message,userId);
        }
    }
    /**
     * 发生错误时调用
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, @NotNull Throwable throwable){
        log.error("发生错误：{}",throwable.getMessage());
        throwable.printStackTrace();
    }
    /**
     * 发送消息
     * @param message
     * @throws IOException
     */
   private void SendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
   }
    /**
     * 发送自定义消息
     * @param message
     * @param userId
     * @throws IOException
     */
    public synchronized void sendMessages(String message,Long userId)throws IOException{
        for (WebSocketServer webSocketServer : webSocketServerCopyOnWriteArraySet){
                if (userId == null) {
                    log.info("推送消息给:{}" , webSocketServer.userId + ",推送内容：{}" , message);
                    webSocketServer.SendMessage(message);
                } else if (userId.equals(webSocketServer.userId)) {
                    log.info("推送消息给:{}" , webSocketServer.userId + ",推送内容：{}" , message);
                    webSocketServer.SendMessage(message);
                }
        }
    }
    /**
     * 返回在线数
     * @return
     */
    private static synchronized int getOnlineCount(){
        return onlineCount;
    }
    /**
     * 连接人数增加时
     */
    private static synchronized void addOnlineCount(){
        WebSocketServer.onlineCount++;
    }
    /**
     * 连接人数减少时
     */
    private static synchronized void subOnlineCount(){
        WebSocketServer.onlineCount--;
    }
}
