package org.laokou.auth.domain.gateway;

public interface CaptchaGateway {

	void set(String uuid, String code);

	Boolean validate(String uuid, String code);

}
