package io.laokou.oauth2.exception;
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
