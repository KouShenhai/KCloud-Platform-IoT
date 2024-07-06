declare namespace API {
  type getByUuidV3Params = {
    uuid: string;
  };

  type LogoutCmd = {
    /** 令牌 */
    token?: string;
  };

  type Option = {
    label?: string;
    value?: string;
  };

  type Result = {
    /** 状态编码 */
    code?: string;
    /** 响应描述 */
    msg?: string;
    /** 响应结果 */
    data?: Option[];
    /** 链路ID */
    traceId?: string;
  };
}
