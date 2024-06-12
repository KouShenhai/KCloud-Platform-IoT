import axios, { AxiosRequestConfig, Canceler } from "axios";

const cancelMap = new Map<string, Canceler>();

export class AxiosCancel {
  /**
   * 添加请求的 cancel
   * @param config
   */
  addPending(config: AxiosRequestConfig) {
    const { url, method } = config;

    if (!url || !method) return;

    // 处理同个api，同时多次请求的情况，先移除上一个
    this.removePending(url, method);

    const key = getCancelMapKey(url, method);
    config.cancelToken = new axios.CancelToken((cancel: Canceler) => {
      if (!cancelMap.has(key)) {
        cancelMap.set(key, cancel);
      }
    });
  }

  /**
   * 移除请求
   * @param url
   * @param method
   */
  removePending(url: string | undefined, method: string | undefined) {
    if (!url || !method) return;    

    const key = getCancelMapKey(url, method);
    const cancel = cancelMap.get(key);
    if (cancel) {
      cancel();
      cancelMap.delete(key);
    }
  }

  /**
   * 移除所有请求
   */
  removeAllPending() {
    cancelMap.forEach((cancel) => {
      cancel && cancel();
    });
    cancelMap.clear();
  }
}

function getCancelMapKey(url: string, method: string) {
  return `${url}_${method}`;
}

