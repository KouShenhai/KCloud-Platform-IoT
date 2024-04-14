import { http } from "@/utils/http";

export type AuthResult = {
  /** `token` */
  access_token: string;
  /** 用于调用刷新`accessToken`的接口时所需的`token` */
  refresh_token: string;
  /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
  expires_in: number;
};

export type CaptchaResult = {
  code: string,
  msg: string,
  data: string
}

export type PublicKeyResult = {
  code: string,
  msg: string,
  data: string
}

export type RefreshTokenResult = {
  success: boolean;
  data: {
    /** `token` */
    accessToken: string;
    /** 用于调用刷新`accessToken`的接口时所需的`token` */
    refreshToken: string;
    /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
    expires: Date;
  };
};

/** 登录 */
export const loginApi = (data?: object) => {
  return http.request<AuthResult>("post", "/api/auth/oauth2/token", { data }, {
    // 设置序列化请求函数
    transformRequest: (data = {}) => Object.entries(data).map(ent => ent.join('=')).join('&'),
    headers: {
      'Authorization': 'Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=',
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    }
  });
};

export const findCaptchaApi = (data?: string) => {
  return http.request<CaptchaResult>("get", "/api/auth/v1/captchas/" + data);
};

export const findPublicKeyApi = (data?: string) => {
  return http.request<PublicKeyResult>("get", "/api/auth/v1/secrets");

}

/** 刷新token */
export const refreshTokenApi = (data?: object) => {
  return http.request<RefreshTokenResult>("post", "/refresh-token", { data });
};
