package org.laokou.mqtt.client;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.Timeout;

import java.util.concurrent.CompletableFuture;

/**
 * 一条在途（in-flight）出站 QoS1/QoS2 消息及其协议状态。
 */
public final class OutboundMessage {

    public enum State {
        /** QoS1：等待 PUBACK */
        WAIT_PUBACK,
        /** QoS2：已发 PUBLISH，等待 PUBREC */
        WAIT_PUBREC,
        /** QoS2：已发 PUBREL，等待 PUBCOMP */
        WAIT_PUBCOMP
    }

    final int packetId;
    final String topic;
    final byte[] payload;
    final MqttQoS qos;
    final boolean retained;
    final CompletableFuture<Void> future;

    public volatile State state;
    public volatile boolean dup;
    public volatile int retries;
    /** ACK 超时定时任务句柄 */
    public volatile Timeout timeoutTask;
    /** 断线期间入队、尚未首次写入 */
    public volatile boolean neverWritten = true;

    public OutboundMessage(int packetId, String topic, byte[] payload, MqttQoS qos,
                           boolean retained, CompletableFuture<Void> future) {
        this.packetId = packetId;
        this.topic = topic;
        this.payload = payload;
        this.qos = qos;
        this.retained = retained;
        this.future = future;
        this.state = qos == MqttQoS.EXACTLY_ONCE ? State.WAIT_PUBREC : State.WAIT_PUBACK;
    }

    public int packetId() { return packetId; }
    public String topic() { return topic; }
    public byte[] payload() { return payload; }
    public MqttQoS qos() { return qos; }
    public boolean retained() { return retained; }
    public State state() { return state; }
    public CompletableFuture<Void> future() { return future; }
}
