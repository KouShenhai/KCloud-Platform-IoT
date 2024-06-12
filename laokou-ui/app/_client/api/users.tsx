import request from "@/app/_client/utils/request";
/**
 * UserCO，用户
 */
interface UserCO {
  /**
   * 头像
   */
  avatar?: string;
  /**
   * 创建时间
   */
  createDate?: Date;
  /**
   * 部门ID
   */
  deptId?: number;
  /**
   * 部门PATH
   */
  deptPath?: string;
  /**
   * 编辑人
   */
  editor?: number;
  /**
   * ID
   */
  id?: number;
  /**
   * 邮箱
   */
  mail?: string;
  /**
   * 手机号
   */
  mobile?: string;
  /**
   * 密码
   */
  password?: string;
  /**
   * 角色IDS
   */
  roleIds?: number[];
  /**
   * 用户状态 0正常 1锁定
   */
  status?: number;
  /**
   * 超级管理员标识 0否 1是
   */
  superAdmin?: number;
  /**
   * 用户名
   */
  username?: string;
}

//修改用户
const updateUser = async (params: UserCO) => {
  const result = await request.put('/laokou/admin/v1/users', params, { headers: { noLoading: true } });
  return result.data
}

// 新增用户
const addUser = async (params: UserCO) => {
  const result = await request.post('/laokou/admin/v1/users', params, { headers: { noLoading: true } });
  return result.data
}


interface SettingUserStatus {
  /**
   * ID
   */
  id: number;
  /**
   * 用户状态 0正常 1锁定
   */
  status: number;
}

// 修改用户状态
const updateUserStatus = async (params: SettingUserStatus) => {
  const result = await request.put('/laokou/admin/v1/users/status', params, { headers: { noLoading: true } });
  return result.data
}

interface ResetPassWord {
  /**
   * ID
   */
  id: number;
  /**
   * 密码
   */
  password: string;
}

//重置密码
const resetPassWord = async (params: ResetPassWord) => {
  const result = await request.put('/laokou/admin/v1/users/reset-password', params, { headers: { noLoading: true } });
  return result.data
}


// 查看个人信息
const userProfile = async () => {
  const result = await request.get('/laokou/admin/v1/users/profile', {}, { headers: { noLoading: true } });
  return result.data
}
interface UserProfileCO {
  /**
   * 头像
   */
  avatar?: string;
  /**
   * ID
   */
  id?: number;
  /**
   * 邮箱
   */
  mail?: string;
  /**
   * 手机号
   */
  mobile?: string;
  /**
   * 菜单权限标识集合
   */
  permissions?: string[];
  /**
   * 超级管理员标识 0否 1是
   */
  superAdmin?: number;
  /**
   * 租户ID
   */
  tenantId?: number;
  /**
   * 用户名
   */
  username?: string;
}

//修改个人信息
const updataaUserProfile = async (params: UserProfileCO) => {
  const result = await request.put('/laokou/admin/v1/users/profile', params, { headers: { noLoading: true } });
  return result.data
}

//修改密码
const updataPassWord = async (params: ResetPassWord) => {
  const result = await request.put('/laokou/admin//v1/users/password', params, { headers: { noLoading: true } });
  return result.data
}
interface User {
  /**
   * 结束时间
   */
  endTime?: string;
  /**
   * 忽略数据权限
   */
  ignore?: boolean;
  /**
   * 上一次ID，可以用于深度分页
   */
  lastId?: number;
  /**
   * 索引
   */
  pageIndex?: number;
  /**
   * 页码
   */
  pageNum?: number;
  /**
   * 条数
   */
  pageSize?: number;
  /**
   * SQL拼接
   */
  sqlFilter?: string;
  /**
   * 开始时间
   */
  startTime?: string;
  /**
   * 用户名
   */
  username?: string;
}
// 在线用户列表
const onlineUser = async (params: User) => {
  const result = await request.post('/laokou/admin//v1/users/online-list', params, { headers: { noLoading: true } });
  return result.data
}

//查询用户列表
const userList = async (params: User) => {
  const result = await request.post('/laokou/admin/v1/users/list', params, { headers: { noLoading: true } });
  return result.data
}
// 查看用户
const searchUserById = async (id: number) => {
  const result = await request.get('/laokou/admin/v1/users/' + id, {}, { headers: { noLoading: true } });
  return result.data
}

// 删除用户
const deleteUserById = async (id: number) => {
  const result = await request.delete('/laokou/admin/v1/users/' + id, {}, { headers: { noLoading: true } });
  return result.data
}

// 下拉列表
const optionList = async () => {
  const result = await request.get('/laokou/admin/v1/users/option-list', {}, { headers: { noLoading: true } });
  return result.data
}
interface DownUser {
  /**
   * 令牌
   */
  token: string;
}
// 强踢在线用户
const downUser = async (params: DownUser) => {
  const result = await request.delete('/laokou/admin/v1/users/kill-online', params, { headers: { noLoading: true } });
  return result.data
}

export {
  updateUser,
  addUser,
  updateUserStatus,
  resetPassWord,
  userProfile,
  updataaUserProfile,
  updataPassWord,
  onlineUser,
  userList,
  searchUserById,
  deleteUserById,
  optionList,
  downUser
}