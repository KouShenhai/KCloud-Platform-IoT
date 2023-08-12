package org.laokou.test.elasticsearch;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author laokou
 */
@SpringBootApplication
@EnableEncryptableProperties
public class ElasticsearchTestApp {

	public static void main(String[] args) {
		SpringApplication.run(ElasticsearchTestApp.class, args);
	}

}
