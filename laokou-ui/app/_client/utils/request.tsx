import { message } from "antd";
import axios, {
  AxiosInstance,
  InternalAxiosRequestConfig,
  AxiosResponse,
  AxiosError,
} from "axios";

import { ResponseCodeEnum } from "../../_enums/httpEnum";

import {ResultData}  from "../api/type/api.type";
import fullLoading from "./fullLoading";
import { AxiosCancel } from "./axiosCancel";

const config = {
  // baseURL: "https://laokou.org.cn/",
  timeout: 100000,
  withCredentials: false,
  headers: {'Authorization': 'Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8='},
};

const axiosCancel = new AxiosCancel();

class RequestHttp {
  service: AxiosInstance;

  constructor() {
    this.service = axios.create(config);

    /**
     * @description 请求拦截器
     */
    this.service.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        if(!config.headers["Content-Type"]) {
          config.headers["Content-Type"]='application/x-www-form-urlencoded';
        }

        // console.log('@',config)

        // 打开全局 loading
        // 如不需要全局 loading，则第三个参数  { headers: { noLoading: true } }
        if(!config.headers.noLoading) {
          fullLoading.show();
        }

        // 将请求添加到 pending 中
        axiosCancel.addPending(config);

        // 这里如果需要添加token
        // const token = store.getState().global.token; // 我这里用的是 react-redux + redux-toolkit
        // config.headers["X-Access-Token"] = token;

        return config;
      }
    );

    /**
     * @description 响应拦截器
     */
    this.service.interceptors.response.use(
      (response: AxiosResponse) => {
        const { data, config } = response;
        // 关闭全局 loading
        if(!config.headers.noLoading) {
          fullLoading.hide();
        }
        // 请求结束，移除本次请求
        axiosCancel.removePending(config.url, config.method);

        // 接口返回 code 不是 200 的处理
        if (response.status !== ResponseCodeEnum.SUCCESS) {
          message.error(data.msg);

          // 登录失效，清除 token，跳转到登录页面
        //   if (data.code === ResponseCodeEnum.NOLOGIN) {
        //     store.dispatch(setToken(""));
        //     window.location.href = "/login";
        //   }

          return Promise.reject(data);
        }
        return data;
      },

      (error: AxiosError) => {
        fullLoading.hide();

        const { response } = error;
        if (response) {
          checkStatus(response.status);
        }
        return false;
      }
    );
  }

  // 常用请求方法封装
  get<T>(url: string, params?: object, _object = {}): Promise<ResultData<T>> {
    return this.service.get(url, { params, ..._object });
  }
  post<T>(url: string, params?: object, _object = {}): Promise<ResultData<T>> {
    return this.service.post(url, params, _object);
  }
  put<T>(url: string, params?: object, _object = {}): Promise<ResultData<T>> {
    return this.service.put(url, params, _object);
  }
  delete<T>(url: string, params?: any, _object = {}): Promise<ResultData<T>> {
    return this.service.delete(url, { params, ..._object });
  }
}

/**
 * @description: 校验网络请求状态码
 * @param {Number} status
 * @return void
 */
const checkStatus = (status: number): void => {
  switch (status) {
    case 404:
      message.error("资源不存在！");
      break;
    case 405:
      message.error("请求方式错误！");
      break;
    case 500:
      message.error("服务器异常！");
      break;
    default:
      message.error("请求失败！");
  }
};

const request = new RequestHttp();
export default request;
