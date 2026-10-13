package org.laokou.mqtt.client;

import java.util.Collection;

/**
 * 在途消息存储（扩展点）。
 *
 * <p>默认实现为内存版 {@link InMemoryInflightStore}；
 * 集群或崩溃恢复场景可实现 Redis / 数据库持久化版本，
 * 保证 QoS1/2 消息跨进程重启不丢失。
 */
public interface InflightStore {

    /** 不存在同 packetId 的消息时放入并返回 null，否则返回已存在的消息 */
    OutboundMessage putIfAbsent(OutboundMessage message);

    OutboundMessage get(int packetId);

    OutboundMessage remove(int packetId);

    Collection<OutboundMessage> all();

    int size();

    void clear();
}
