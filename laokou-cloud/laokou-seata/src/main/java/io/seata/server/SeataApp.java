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

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author spilledyear@outlook.com
 */
@SpringBootApplication(scanBasePackages = { "io.seata" })
@EnableEncryptableProperties
public class SeataApp {

	public static void main(String[] args) {
		// -Dtls.enable=true -Dtls.client.authServer=true
		// -Dtls.client.trustCertPath=d:\\nacos.crt
		SpringApplication.run(SeataApp.class, args);
	}

}
