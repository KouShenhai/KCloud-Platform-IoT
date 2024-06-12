// 接口返回结构，不包含 data
export interface ResponseResult {
    // 状态码
    code: number;
    // 消息
    msg: string;
  }
  
  // 完整的接口返回结构
  export interface ResultData<T = any> extends ResponseResult {
    // 数据
    data: T;
  }
  
  