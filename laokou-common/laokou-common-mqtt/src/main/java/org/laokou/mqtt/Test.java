/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
 *
 */

package org.laokou.mqtt;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public class Test {
    public static void main(String[] args) throws MqttException {
        try {
            MqttClient client = new MqttClient("tcp://127.0.0.1:1883", "123", new MemoryPersistence());
            MqttConnectionOptions options = new MqttConnectionOptions();
            options.setCleanStart(true);
            options.setKeepAliveInterval(180);
            options.setUserName("mqtt");
            options.setPassword("laokou123".getBytes(StandardCharsets.UTF_8));
            options.setReceiveMaximum(5);
            options.setMaximumPacketSize(1024L);
            client.connectWithResult(options);
            client.publish("/test/l", "tt".getBytes(StandardCharsets.UTF_8), 2, false);
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
