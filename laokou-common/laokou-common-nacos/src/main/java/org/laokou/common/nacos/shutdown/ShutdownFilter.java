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

package org.laokou.common.nacos.shutdown;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.nacos.holder.ShutdownHolder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Slf4j
@WebFilter(filterName = "shutdownFilter", urlPatterns = "/graceful-shutdown")
public class ShutdownFilter implements Filter {

    private static final ScheduledExecutorService NEWED_SCHEDULED_THREAD_POOL = Executors.newScheduledThreadPool(1);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("加载优雅停机过滤器");
        Filter.super.init(filterConfig);
    }

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        // 注册关闭钩子函数
        log.info("已注册钩子函数，请关闭进程");
        // 打开挡板（直接响应前端 -> 服务正在维护）
        ShutdownHolder.open();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            int second = 120 * 1000;
            long start = IdGenerator.SystemClock.now();
            NEWED_SCHEDULED_THREAD_POOL.scheduleWithFixedDelay(() -> {
                long end = IdGenerator.SystemClock.now();
                if (end - start >= second || ShutdownHolder.get() == 0) {
                    NEWED_SCHEDULED_THREAD_POOL.shutdown();
                }
            }, 0, 1, TimeUnit.SECONDS);
        }));
    }

    @Override
    public void destroy() {
        log.info("优雅停机执行完毕");
        Filter.super.destroy();
    }

}
