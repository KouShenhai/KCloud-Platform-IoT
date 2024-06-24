
declare namespace API.System {

  interface Post {
    postId: number;
    postCode: string;
    postName: string;
    postSort: number;
    status: string;
    createBy: string;
    createTime: Date;
    updateBy: string;
    updateTime: Date;
    remark: string;
  }

  export interface PostListParams {
    postId?: string;
    postCode?: string;
    postName?: string;
    postSort?: string;
    status?: string;
    createBy?: string;
    createTime?: string;
    updateBy?: string;
    updateTime?: string;
    remark?: string;
    pageSize?: string;
    current?: string;
  }

  export interface PostInfoResult { 
    code: number;
    msg: string;
    data: Post;
  } 

   export interface PostPageResult { 
    code: number;
    msg: string;
    total: number;
    rows: Array<Post>;
  }

}