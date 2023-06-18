package org.laokou.generate;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author laokou
 */
@SpringBootApplication
public class GenerateApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(GenerateApplication.class).web(WebApplicationType.SERVLET).run(args);
	}

}
