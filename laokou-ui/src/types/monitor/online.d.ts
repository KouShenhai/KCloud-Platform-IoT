
declare namespace API.Monitor {

  export type OnlineUserType = {
    tokenId: string;
    userName: string;
    ipaddr: string;
    loginLocation: string;
    browser: string;
    os: string;
    deptName: string;
    loginTime: string;
  };

  export type OnlineUserListPagination = {
    total: number;
    pageSize: number;
    current: number;
  };

  export type OnlineUserListData = {
    list: OnlineUserType[];
    pagination: Partial<OnlineUserListPagination>;
  };

  export type OnlineUserListParams = {
    tokenId?: string;
    userName?: string;
    ipaddr?: string;
    loginLocation?: string;
    browser?: string;
    os?: string;
    deptName?: string;
    loginTime?: string;
    pageSize?: string;
    current?: string;
    pageNum?: string;
    filter?: string;
    sorter?: string;
  };

  export interface OnlineUserPageResult {
    code: number;
    msg: string;
    total: number;
    rows: Array<OnlineUser>;
  }

}
