/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.server;

import com.alibaba.nacos.common.tls.TlsSystemConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

/**
 * @author spilledyear@outlook.com
 */
@SpringBootApplication(scanBasePackages = { "io.seata" })
@EnableEncryptableProperties
public class SeataApp {

	public static void main(String[] args) throws IOException {
		// run the spring-boot application
		System.setProperty(TlsSystemConfig.TLS_ENABLE, "true");
		System.setProperty(TlsSystemConfig.CLIENT_AUTH, "true");
		System.setProperty(TlsSystemConfig.CLIENT_TRUST_CERT,
				ResourceUtils.getFile("classpath:nacos.crt").getCanonicalPath());
		SpringApplication.run(SeataApp.class, args);
	}

}
