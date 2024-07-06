declare namespace API {
  type getCaptchaByUuidV3Params = {
    uuid: string;
  };

  type LogoutCmd = {
    /** 令牌 */
    token?: string;
  };

  type OAuth2Param = {
    username?: string;
    password?: string;
    uuid?: string;
    captcha?: string;
    grant_type?: string;
    tenant_id?: number;
  };

  type LoginParam = {
    username?: string;
    password?: string;
    captcha?: string;
    mail?: string;
    mobile?: string;
  };

  type Result = {
    /** 状态编码 */
    code?: string;
    /** 响应描述 */
    msg?: string;
    /** 响应结果 */
    data?: any;
    /** 链路ID */
    traceId?: string;
  };
}
