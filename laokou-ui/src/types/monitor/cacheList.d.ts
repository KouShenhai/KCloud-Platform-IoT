

declare namespace API.Monitor {

  export type CacheContent = {
    cacheKey: string;
    cacheName: string;
    cacheValue: string;
    remark: string;
  };

  export type CacheNamesResponse = {
    data: CacheContent[];
    code: number;
    msg: string;
  };

  export type CacheKeysResponse = {
    data: string[];
    code: number;
    msg: string;
  };

  export type CacheValueResponse = {
    data: CacheContent;
    code: number;
    msg: string;
  };

}
