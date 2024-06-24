
declare namespace API.System {

  interface Role {
    roleId: number;
    roleName: string;
    roleKey: string;
    roleSort: number;
    dataScope: string;
    menuCheckStrictly: number;
    deptCheckStrictly: number;
    status: string;
    delFlag: string;
    createBy: string;
    createTime: Date;
    updateBy: string;
    updateTime: Date;
    remark: string;
  }

  export interface RoleListParams {
    roleId?: string;
    roleName?: string;
    roleKey?: string;
    roleSort?: string;
    dataScope?: string;
    menuCheckStrictly?: string;
    deptCheckStrictly?: string;
    status?: string;
    delFlag?: string;
    createBy?: string;
    createTime?: string;
    updateBy?: string;
    updateTime?: string;
    remark?: string;
    pageSize?: string;
    current?: string;
  }

  export interface RoleInfoResult { 
    code: number;
    msg: string;
    data: Role;
  } 

   export interface RolePageResult { 
    code: number;
    msg: string;
    total: number;
    rows: Array<Role>;
  }

  export type RoleMenuNode = {
    id: number|string;
    label: string;
    children?: Array<RoleMenuNode>;
  }
  export interface RoleMenuResult { 
    code: number;
    msg: string;
    checkedKeys: number[];
    menus: Array<RoleMenuNode>;
  }

}