import { defineStore } from "pinia";
import { store } from "@/store";
import type { userType } from "./types";
import { routerArrays } from "@/layout/types";
import { router, resetRouter } from "@/router";
import { storageLocal } from "@pureadmin/utils";
import {AuthResult, loginApi, refreshTokenApi, RefreshTokenResult} from "@/api/auth";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import { type DataInfo, setToken, removeToken, userKey } from "@/utils/auth";

export const useUserStore = defineStore({
  id: "laokou-user",
  // state: (): userType => ({
  //   // 用户名
  //   username: storageLocal().getItem<DataInfo<number>>(userKey)?.username ?? "",
  //   // 页面级别权限
  //   roles: storageLocal().getItem<DataInfo<number>>(userKey)?.roles ?? [],
  // }),
  actions: {
    // /** 存储用户名 */
    // SET_USERNAME(username: string) {
    //   this.username = username;
    // },
    // /** 存储角色 */
    // SET_ROLES(roles: Array<string>) {
    //   this.roles = roles;
    // },
    /** 登录 */
    async loginByUsername(data) {
      return new Promise<AuthResult>((resolve, reject) => {
        loginApi(data)
          .then(res => {
            setToken(res);
            resolve(res);
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    /** 前端登出（不调用接口） */
    logOut() {
      this.username = "";
      this.roles = [];
      removeToken();
      useMultiTagsStoreHook().handleTags("equal", [...routerArrays]);
      resetRouter();
      router.push("/login").then(r => {});
    },
    /** 刷新`token` */
    async handRefreshToken(data) {
      return new Promise<RefreshTokenResult>((resolve, reject) => {
        refreshTokenApi(data)
          .then(data => {
            if (data) {
              // setToken(data.data);
              resolve(data);
            }
          })
          .catch(error => {
            reject(error);
          });
      });
    }
  }
});

export function useUserStoreHook() {
  return useUserStore(store);
}
