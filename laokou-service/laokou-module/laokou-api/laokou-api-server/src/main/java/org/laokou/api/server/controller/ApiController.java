package org.laokou.api.server.controller;

import org.laokou.common.secret.annotation.ApiSecret;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	@ApiSecret
	@GetMapping()
	public void test() {
		System.out.println("33");
	}

}
