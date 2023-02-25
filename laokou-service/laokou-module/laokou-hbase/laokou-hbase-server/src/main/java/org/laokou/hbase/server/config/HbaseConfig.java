/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.hbase.server.config;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
/**
 * @author laokou
 */
@Configuration
public class HbaseConfig {

    /**
     * 读取HBase的zookeeper地址
     */
    @Value("${hbase.zookeeper.quorum}")
    private String quorum;

    /**
     * 配置HBase连接参数
     */
    @Bean
    public org.apache.hadoop.conf.Configuration hbaseConfiguration() {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set(HConstants.ZOOKEEPER_QUORUM, quorum);
        return configuration;
    }

    /**
     * 每次调用get方法就会创建一个Connection
     */
    @Bean
    public Supplier<Connection> hbaseConnSupplier() {
        return () -> {
            try {
                return hbaseConnection();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    @Scope(value = "prototype")
    /**
     * @Scope标明模式，默认单例模式，prototype多例模式
     * 若是在其他类中直接@Autowired引入的，多例就无效，因为那个类在初始化的时候，已经创建了这个bean，之后调用，不会重新创建
     * 若是想要实现实例，就要每次调用的时候，手动获取bean
     */
    public Connection hbaseConnection() throws IOException {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(200, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue());
        poolExecutor.prestartCoreThread();
        return ConnectionFactory.createConnection(hbaseConfiguration(), poolExecutor);
    }

}
