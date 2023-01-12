/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.rocketmq.client.constant;
/**
 * 队列常量值
 * @author laokou
 * @version 1.0
 * @date 2020/9/18 0018 上午 7:07
 */
public interface RocketmqConstant {

    /**
     * 操作日志
     */
    String LAOKOU_OPERATE_LOG_TOPIC = "laokou-operate-log-topic";

    /**
     * 登录日志
     */
    String LAOKOU_LOGIN_LOG_TOPIC = "laokou-login-log-topic";

    /**
     * 审批日志
     */
    String LAOKOU_AUDIT_LOG_TOPIC = "laokou-audit-log-topic";

    /**
     * 同步索引
     */
    String LAOKOU_SYNC_INDEX_TOPIC = "laokou-sync-index-topic";

    /**
     * 创建索引
     */
    String LAOKOU_CREATE_INDEX_TOPIC = "laokou-create-index-topic";

    /**
     * 删除索引
     */
    String LAOKOU_DELETE_INDEX_TOPIC = "laokou-delete-index-topic";

    /**
     * 消息通知
     */
    String LAOKOU_MESSAGE_NOTICE_TOPIC = "laokou-message-notice-topic";

    /**
     * 消息重试次数
     */
    Integer RECONSUME_TIMES = 3;

}
