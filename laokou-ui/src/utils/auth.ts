import { storageLocal } from "@pureadmin/utils";

export interface TokenInfo {
  /** token */
  access_token: string;
  /** 用于调用刷新accessToken的接口时所需的token */
  refresh_token: string;
  /** `accessToken`的过期时间（时间戳） */
  expires_in: number;
}

export const userKey = "user-info";
export const TokenKey = "authorized-token";
/**
 * 通过`multiple-tabs`是否在`cookie`中，判断用户是否已经登录系统，
 * 从而支持多标签页打开已经登录的系统后无需再登录。
 * 浏览器完全关闭后`multiple-tabs`将自动从`cookie`中销毁，
 * 再次打开浏览器需要重新登录系统
 * */
export const multipleTabsKey = "multiple-tabs";

/** 获取`token` */
export function getToken(): TokenInfo {
  return storageLocal().getItem(TokenKey);
}

/**
 * @description 设置`token`以及一些必要信息并采用无感刷新`token`方案
 * 无感刷新：后端返回`accessToken`（访问接口使用的`token`）、`refreshToken`（用于调用刷新`accessToken`的接口时所需的`token`，`refreshToken`的过期时间（比如30天）应大于`accessToken`的过期时间（比如2小时））、`expires`（`accessToken`的过期时间）
 * 将`accessToken`、`expires`这两条信息放在key值为authorized-token的cookie里（过期自动销毁）
 * 将`username`、`roles`、`refreshToken`、`expires`这四条信息放在key值为`user-info`的localStorage里（利用`multipleTabsKey`当浏览器完全关闭后自动销毁）
 */
export function setToken(data: TokenInfo) {
  const { access_token, refresh_token, expires_in } = data;
  storageLocal().setItem(TokenKey, { access_token, refresh_token, expires_in });
}

/** 删除`token`以及key值为`user-info`的localStorage信息 */
export function removeToken() {
  storageLocal().removeItem(TokenKey);
}

/** 格式化token（jwt格式） */
export const formatToken = (token: string): string => {
  return "Bearer " + token;
};
