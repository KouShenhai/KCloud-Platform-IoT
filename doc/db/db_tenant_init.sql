-- /**
--  * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
--  * <p>
--  * Licensed under the Apache License, Version 2.0 (the "License");
--  * you may not use this file except in compliance with the License.
--  * You may obtain a copy of the License at
--  * <p>
--  *   http://www.apache.org/licenses/LICENSE-2.0
--  * <p>
--  * Unless required by applicable law or agreed to in writing, software
--  * distributed under the License is distributed on an "AS IS" BASIS,
--  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--  * See the License for the specific language governing permissions and
--  * limitations under the License.
--  */
CREATE TABLE `boot_sys_dict` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
                                 `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                 `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                 `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                 `dict_value` varchar(500) DEFAULT NULL COMMENT '值',
                                 `dict_label` varchar(100) DEFAULT NULL COMMENT '标签',
                                 `type` varchar(100) DEFAULT NULL COMMENT '类型',
                                 `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                 `sort` int(11) DEFAULT '1' COMMENT '排序',
                                 `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典';

CREATE TABLE `boot_sys_message` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `title` varchar(400) DEFAULT NULL COMMENT '标题',
                                    `content` longtext COMMENT '内容',
                                    `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                    `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                    `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                    `type` tinyint(2) DEFAULT '0' COMMENT '0通知 1提醒',
                                    `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息';

CREATE TABLE `boot_sys_message_detail` (
                                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                           `message_id` bigint(20) DEFAULT NULL COMMENT '消息id',
                                           `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
                                           `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                           `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                           `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                           `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                           `read_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读 0未读 1已读',
                                           `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息详情';

CREATE TABLE `boot_sys_oss` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                `name` varchar(20) DEFAULT NULL COMMENT '名称',
                                `endpoint` varchar(200) DEFAULT NULL COMMENT '终端地址',
                                `region` varchar(10) DEFAULT NULL COMMENT '区域',
                                `access_key` varchar(50) DEFAULT NULL COMMENT '访问密钥',
                                `secret_key` varchar(100) DEFAULT NULL COMMENT '用户密钥',
                                `bucket_name` varchar(20) DEFAULT NULL COMMENT '桶名',
                                `path_style_access_enabled` tinyint(1) DEFAULT NULL COMMENT '路径样式访问 1已开启 0未启用',
                                `status` tinyint(1) DEFAULT NULL COMMENT '状态 1已启用 0未启用',
                                `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对象存储';

CREATE TABLE `boot_sys_oss_log` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                    `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                    `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `del_flag` tinyint(1) DEFAULT '0' COMMENT '1已删除 0未删除',
                                    `md5` varchar(100) DEFAULT NULL COMMENT 'md5标识',
                                    `url` varchar(200) DEFAULT NULL COMMENT '路径',
                                    `file_name` varchar(100) DEFAULT NULL COMMENT '文件名称',
                                    `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小',
                                    `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储日志';

-- 2023/2/24 增加boot_sys_message_detail索引 老寇
ALTER table boot_sys_message_detail add INDEX idx_read_flag_user_id(read_flag,user_id) COMMENT '已读_用户编号_索引';