/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.oauth2.exception;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
/**
 * 自定义异常
 *
 * @author Kou Shenhai
 */
public class RenOAuth2Exception extends OAuth2Exception {
	private String msg;
	private String code;

	public RenOAuth2Exception(String msg) {
		super(msg);
		this.msg = msg;
	}

	public RenOAuth2Exception(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public RenOAuth2Exception(int code, String msg) {
		super(msg);
		this.msg = msg;
		this.code = "" + code;
	}

	public RenOAuth2Exception(String code, String msg) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = "" + code;
	}

	@Override
	public String getOAuth2ErrorCode() {
		if(null == this.code){
			return "invalid_request";
		}
		return this.code;
	}
}
