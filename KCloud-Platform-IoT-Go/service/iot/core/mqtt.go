/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package core

import (
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"go.uber.org/zap"
	"strings"
	"time"
)

type MQTT struct {
	// 用户名
	Username string `json:"username"`
	// 密码
	Password string `json:"password"`
	// 客户端ID
	ClientId string `json:"clientId"`
	// 主机
	Host string `json:"host"`
	// 端口
	Port string `json:"port"`
	// 主题
	Topic string `json:"topic"`
	// Qos【0/1/2】 => 值越大延迟越高
	Qos byte `json:"qos"`
	// 是否保留
	Retained bool `json:"retained"`
}

func (m *MQTT) GetMqttClient() mqtt.Client {
	broker := fmt.Sprintf("tcp://%s:%s", m.Host, m.Port)
	options := mqtt.NewClientOptions()
	options.AddBroker(broker)
	options.SetClientID(m.ClientId)
	options.SetUsername(m.Username)
	options.SetPassword(m.Password)
	options.OnConnect = onConnect
	// 开启自动重连
	options.SetAutoReconnect(true)
	options.OnConnectionLost = onConnectLost
	options.SetDefaultPublishHandler(defaultPublishHandler)
	client := mqtt.NewClient(options)
	if token := client.Connect(); token.Wait() && token.Error() != nil {
		fmt.Println("connect failed", zap.Error(token.Error()))
		time.Sleep(5 * time.Second)
		return m.GetMqttClient()
	}
	//
	return client
}

func (m *MQTT) SubscribeMultiple(client mqtt.Client, callback mqtt.MessageHandler) {
	// 订阅topic
	topics := strings.Split(m.Topic, ",")
	var filters = make(map[string]byte)
	for _, topic := range topics {
		filters[topic] = m.Qos
	}
	client.SubscribeMultiple(filters, callback)
}

func (m *MQTT) DisConnectMQTT(client mqtt.Client) {
	client.Disconnect(30 * 1000)
}
func (m *MQTT) PublishMQTT(client mqtt.Client, topic string, qos byte, retained bool, payload interface{}) {
	client.Publish(topic, qos, retained, payload)
}

func (m *MQTT) DefaultPublishMQTT(client mqtt.Client, topic string, payload interface{}) {
	m.PublishMQTT(client, topic, 0, false, payload)
}

func onConnectLost(client mqtt.Client, err error) {
	fmt.Println("mqtt connect lost", zap.Error(err))
	// 重连
	for i := 0; i < 5; i++ {
		if token := client.Connect(); token.Wait() && token.Error() == nil {
			fmt.Println("reconnect successfully")
			break
		} else {
			fmt.Println("reconnect failed", zap.Any("attempt", i+1), zap.Error(token.Error()))
			time.Sleep(10)
		}
	}
}
func defaultPublishHandler(mqtt.Client, mqtt.Message) {}

func onConnect(client mqtt.Client) {}
