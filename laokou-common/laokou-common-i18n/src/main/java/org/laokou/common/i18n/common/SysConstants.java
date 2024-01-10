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

package org.laokou.common.i18n.common;

/**
 * @author laokou
 */
public final class SysConstants {

    private SysConstants() {}

    /**
     * 版本.
     */
    public static final String VERSION = "3.2.1";

    /**
     * 优雅停机.
     */
    public static final String GRACEFUL_SHUTDOWN_URL = "/graceful-shutdown";

    /**
     * undefined.
     */
    public static final String UNDEFINED = "undefined";

    /**
     * 最大文件.
     */
    public static final long MAX_FILE_SIZE = 100 * 1024 * 1024;

    /**
     * RSA加密.
     */
    public static final String ALGORITHM_RSA = "RSA";

    /**
     * 开启
     */
    public static final String ENABLED = "enabled";

    /**
     * spring管理
     */
    public static final String SPRING = "spring";

    /**
     * 空日志信息
     */
    public static final String EMPTY_LOG_MSG = "暂无信息";

    public static final String ALGORITHM_AES = "aes";

    public static final String AES_INSTANCE = "AES/CBC/PKCS5Padding";

    public static final String COMMON_DATA_ID = "application-common.yaml";

    public static final String APPLICATION = "application";

    public static final String RATE_LIMITER_KEY = "___%s_KEY___";

    public static final String REDIS_PROTOCOL_PREFIX = "redis://";

    public static final String REDISS_PROTOCOL_PREFIX = "rediss://";

    public static final String CRYPTO_PREFIX = "ENC(";

    public static final String CRYPTO_SUFFIX = ")";

    public static final String PUBLIC_KEY = "public-key";

    public static final String ALL_PATTERNS = "/**";

    public static final String EXCEL_SUFFIX = ".xlsx";

}
