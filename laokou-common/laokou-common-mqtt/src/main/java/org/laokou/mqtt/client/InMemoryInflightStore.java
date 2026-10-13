package org.laokou.mqtt.client;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/** 基于 {@link ConcurrentHashMap} 的高并发内存在途消息存储。 */
public class InMemoryInflightStore implements InflightStore {

    private final ConcurrentHashMap<Integer, OutboundMessage> map = new ConcurrentHashMap<>();

    @Override
    public OutboundMessage putIfAbsent(OutboundMessage message) {
        return map.putIfAbsent(message.packetId(), message);
    }

    @Override
    public OutboundMessage get(int packetId) {
        return map.get(packetId);
    }

    @Override
    public OutboundMessage remove(int packetId) {
        return map.remove(packetId);
    }

    @Override
    public Collection<OutboundMessage> all() {
        return map.values();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }
}
