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

package mq

import (
	"context"
	"errors"
	"fmt"
	"net/url"
	"sync"
	"time"

	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/pkg/config"
	amqp "github.com/rabbitmq/amqp091-go"
	"go.uber.org/zap"
)

var (
	RabbitCli  *RabbitClient
	rabbitOnce sync.Once
)

type RabbitClient struct {
	URL          string
	conn         *amqp.Connection
	channel      *amqp.Channel
	mux          sync.RWMutex
	alive        bool
	reconnecting bool
	isClosed     bool // 新增：标记是否已被应用程序主动关闭
	gen          uint64
}

// InitRabbitMQ 初始化 RabbitMQ 客户端
func InitRabbitMQ(r config.RabbitMQConfig) {
	rabbitOnce.Do(func() {
		// 1. 修复隐患：安全的 URL 拼接，防止密码中包含特殊字符
		safeUser := url.QueryEscape(r.Username)
		safePass := url.QueryEscape(r.Password)
		vhost := r.VirtualHost
		// RabbitMQ 默认的 "/" vhost 需要转义为 "%2f"
		if vhost == "/" {
			vhost = "%2f"
		} else {
			vhost = url.QueryEscape(vhost)
		}

		// 2. 修复隐患：强制开启 15 秒心跳检测，防止网络假死
		amqpURL := fmt.Sprintf("amqp://%s:%s@%s:%s/%s?heartbeat=15",
			safeUser, safePass, r.Host, r.Port, vhost)

		RabbitCli = &RabbitClient{
			URL: amqpURL,
		}

		if err := RabbitCli.connect(); err != nil {
			config.Logger.Error("RabbitMQ connect error:", zap.Error(err))
			RabbitCli.mux.Lock()
			RabbitCli.reconnecting = true
			RabbitCli.mux.Unlock()
			go RabbitCli.reconnectLoop()
		}
	})
}

func (r *RabbitClient) connect() error {
	conn, err := amqp.Dial(r.URL)
	if err != nil {
		return err
	}

	ch, err := conn.Channel()
	if err != nil {
		_ = conn.Close()
		return err
	}

	connClose := make(chan *amqp.Error, 1)
	chClose := make(chan *amqp.Error, 1)
	conn.NotifyClose(connClose)
	ch.NotifyClose(chClose)

	r.mux.Lock()
	if r.channel != nil {
		_ = r.channel.Close()
	}
	if r.conn != nil {
		_ = r.conn.Close()
	}
	r.conn = conn
	r.channel = ch
	r.alive = true
	r.reconnecting = false
	r.gen++
	currentGen := r.gen
	r.mux.Unlock()

	config.Logger.Info("RabbitMQ connected successfully")
	go r.watch(connClose, chClose, currentGen)

	return nil
}

func (r *RabbitClient) watch(connClose, chClose chan *amqp.Error, myGen uint64) {
	var closeErr *amqp.Error
	select {
	case closeErr = <-connClose:
	case closeErr = <-chClose:
	}

	r.mux.Lock()
	// 如果是旧的协程，或者是程序主动调用的 Close()，直接退出不重连
	if r.gen != myGen || r.isClosed {
		r.mux.Unlock()
		return
	}

	r.alive = false
	if r.reconnecting {
		r.mux.Unlock()
		return
	}
	r.reconnecting = true
	r.mux.Unlock()

	if closeErr != nil {
		config.Logger.Error("RabbitMQ connection closed abnormally:", zap.Error(closeErr))
	} else {
		config.Logger.Info("RabbitMQ connection closed normally")
	}

	r.reconnectLoop()
}

func (r *RabbitClient) reconnectLoop() {
	for {
		// 3. 修复隐患：检查是否已经被主动关闭，避免无限死循环
		r.mux.RLock()
		if r.isClosed {
			r.mux.RUnlock()
			return
		}
		r.mux.RUnlock()

		config.Logger.Info("RabbitMQ reconnecting in 5s...")
		time.Sleep(5 * time.Second)
		if err := r.connect(); err != nil {
			config.Logger.Error("RabbitMQ reconnect failed:", zap.Error(err))
			continue
		}
		return
	}
}

// GetChannel 对外提供安全的获取 Channel 方法
func (r *RabbitClient) GetChannel() (*amqp.Channel, error) {
	r.mux.RLock()
	defer r.mux.RUnlock()

	if !r.alive || r.channel == nil || r.isClosed {
		return nil, errors.New("rabbitMQ is currently disconnected")
	}
	return r.channel, nil
}

// Close 优雅退出服务（新增！）
func (r *RabbitClient) Close() error {
	r.mux.Lock()
	defer r.mux.Unlock()

	if r.isClosed {
		return nil
	}
	r.isClosed = true
	r.alive = false

	if r.channel != nil {
		_ = r.channel.Close()
	}
	if r.conn != nil {
		return r.conn.Close()
	}
	return nil
}

// Publish 发送消息，支持传入 Context 控制超时
func (r *RabbitClient) Publish(ctx context.Context, exchange, routingKey string, body []byte) error {
	// 复用上一版提供的 GetChannel()，自带读写锁与安全检查
	ch, err := r.GetChannel()
	if err != nil {
		return err // 返回如 "rabbitMQ is currently disconnected"
	}

	return ch.PublishWithContext(
		ctx, // 优化：允许上层业务控制超时时间，避免拥塞时 goroutine 泄露
		exchange,
		routingKey,
		false,
		false,
		amqp.Publishing{
			ContentType:  "application/json",
			DeliveryMode: amqp.Persistent, // 优化：消息持久化，确保 RabbitMQ 重启不丢消息
			Body:         body,
			Timestamp:    time.Now(),
		},
	)
}

// Consume 注册消费者。引入 ctx 配合服务优雅退出
func (r *RabbitClient) Consume(ctx context.Context, queue string, handler func([]byte) error) {
	go func() {
		for {
			// 1. 检查是否收到系统退出信号
			select {
			case <-ctx.Done():
				config.Logger.Info("RabbitMQ consumer exiting cleanly", zap.String("queue", queue))
				return
			default:
			}

			// 2. 获取可用通道
			ch, err := r.GetChannel()
			if err != nil {
				time.Sleep(2 * time.Second)
				continue
			}

			// 3. 优化：【极其重要】设置 QoS，限制未 Ack 消息数量，防止内存 OOM
			// 每次最多给这个消费者派发 100 条消息
			if err := ch.Qos(100, 0, false); err != nil {
				config.Logger.Error("Failed to set QoS", zap.Error(err))
				time.Sleep(2 * time.Second)
				continue
			}

			// 4. 注册消费者
			msgs, err := ch.Consume(queue, "", false, false, false, false, nil)
			if err != nil {
				config.Logger.Error("RabbitMQ consume register error", zap.Error(err))
				time.Sleep(2 * time.Second)
				continue
			}

			config.Logger.Info("RabbitMQ consumer started", zap.String("queue", queue))

			// 5. 消费消息循环（加入 context 监听）
		ConsumeLoop:
			for {
				select {
				case <-ctx.Done():
					config.Logger.Info("Consumer context canceled, stopping consumption")
					return
				case msg, ok := <-msgs:
					if !ok {
						// 消息通道被关闭（说明网络断开或 channel 崩溃），跳出内层循环，进入重连
						break ConsumeLoop
					}
					// 提取为独立方法处理单条消息
					r.processMessage(msg, handler)
				}
			}

			config.Logger.Info("RabbitMQ consumer stopped, retrying...", zap.String("queue", queue))
			time.Sleep(2 * time.Second)
		}
	}()
}

// processMessage 处理单条消息并管理 Ack/Nack
func (r *RabbitClient) processMessage(msg amqp.Delivery, handler func([]byte) error) {
	defer func() {
		if rec := recover(); rec != nil {
			config.Logger.Error("Consumer panic recovered", zap.Any("panic", rec))
			// 发生 Panic 时，不推荐 requeue (true)，否则容易造成毒消息无限死循环
			if err := msg.Nack(false, false); err != nil {
				config.Logger.Error("Failed to Nack message after panic", zap.Error(err))
			}
		}
	}()
	if err := handler(msg.Body); err != nil {
		config.Logger.Error("Consume business error", zap.Error(err))
		// 优化注意：如果这里传 (false, true) 代表一直重新入队。
		// 对于明确的业务异常（如解析出错），建议传 false 丢弃或路由到死信队列。
		// 只有对于瞬时的网络异常等，才应该重新入队。
		if err := msg.Nack(false, false); err != nil {
			config.Logger.Error("Failed to Nack message after business error", zap.Error(err))
		}
		return
	}
	if err := msg.Ack(false); err != nil {
		config.Logger.Error("Failed to Ack message", zap.Error(err))
	}
}

// SetupTopology 声明 Exchange、Queue 并建立绑定关系
// 如果需要死信队列，可以传入 dlxExchange 和 dlxRoutingKey；如果不需要，传空字符串即可。
func (r *RabbitClient) SetupTopology(exchange, exchangeType, queue, routingKey, dlxExchange, dlxRoutingKey string) error {
	ch, err := r.GetChannel()
	if err != nil {
		return fmt.Errorf("failed to get channel for topology setup: %w", err)
	}
	// 注意：这里不需要手动关闭 ch，因为 GetChannel 返回的是复用的长连接 Channel

	// 1. 声明主 Exchange
	err = ch.ExchangeDeclare(
		exchange,     // name
		exchangeType, // type (如: "direct", "topic", "fanout")
		true,         // durable: 持久化，RabbitMQ 重启后依然存在
		false,        // auto-deleted: 当没有任何绑定时是否自动删除
		false,        // internal: 是否为内部交换机（如果是，客户端无法直接发送消息到此交换机）
		false,        // no-wait
		nil,          // arguments
	)
	if err != nil {
		return fmt.Errorf("failed to declare exchange %s: %w", exchange, err)
	}

	// 2. 准备 Queue 的参数 (用于配置死信队列)
	var queueArgs amqp.Table
	if dlxExchange != "" {
		// 声明死信 Exchange
		err = ch.ExchangeDeclare(dlxExchange, "direct", true, false, false, false, nil)
		if err != nil {
			return fmt.Errorf("failed to declare DLX %s: %w", dlxExchange, err)
		}

		// 声明死信 Queue (通常命名为主队列名 + .dlq)
		dlqName := queue + ".dlq"
		_, err = ch.QueueDeclare(dlqName, true, false, false, false, nil)
		if err != nil {
			return fmt.Errorf("failed to declare DLQ %s: %w", dlqName, err)
		}

		// 将死信 Queue 绑定到死信 Exchange
		err = ch.QueueBind(dlqName, dlxRoutingKey, dlxExchange, false, nil)
		if err != nil {
			return fmt.Errorf("failed to bind DLQ %s: %w", dlqName, err)
		}

		// 将死信参数注入到主队列的配置中
		queueArgs = amqp.Table{
			"x-dead-letter-exchange":    dlxExchange,
			"x-dead-letter-routing-key": dlxRoutingKey,
		}
		config.Logger.Info("DLX configured for queue", zap.String("queue", queue), zap.String("dlx", dlxExchange))
	}

	// 3. 声明主 Queue
	_, err = ch.QueueDeclare(
		queue,
		true,      // durable: 持久化
		false,     // auto-delete: 至少有一个消费者连接过，然后所有消费者都断开时，是否自动删除
		false,     // exclusive: 是否为排他队列（仅当前连接可用，连接断开即删除，网关服务不能设为 true）
		false,     // no-wait
		queueArgs, // args: 绑定死信队列参数
	)
	if err != nil {
		return fmt.Errorf("failed to declare queue %s: %w", queue, err)
	}

	// 4. 将主 Queue 绑定到主 Exchange
	err = ch.QueueBind(
		queue,
		routingKey,
		exchange,
		false,
		nil,
	)
	if err != nil {
		return fmt.Errorf("failed to bind queue %s to exchange %s: %w", queue, exchange, err)
	}

	config.Logger.Info("RabbitMQ topology setup successfully",
		zap.String("exchange", exchange),
		zap.String("queue", queue))

	return nil
}
