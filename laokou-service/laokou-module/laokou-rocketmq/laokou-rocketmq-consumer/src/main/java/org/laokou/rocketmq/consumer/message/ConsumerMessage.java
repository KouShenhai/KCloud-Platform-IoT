package org.laokou.rocketmq.consumer.message;

/**
 * @author laokou
 */
public interface ConsumerMessage {

    /**
     * 接收消息
     * @param data
     */
    void receiveMessage(String data);

}
