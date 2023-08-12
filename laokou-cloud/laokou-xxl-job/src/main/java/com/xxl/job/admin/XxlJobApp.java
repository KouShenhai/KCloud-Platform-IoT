package com.xxl.job.admin;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication
@EnableEncryptableProperties
public class XxlJobApp {

	public static void main(String[] args) {
		// 账号/密码 => admin/123456
		SpringApplication.run(XxlJobApp.class, args);
	}

}