import { checkRole, matchPermission } from './utils/permission';
/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: { currentUser?: API.CurrentUser } | undefined) {
  const { currentUser } = initialState ?? {};
  const hasPerms = (perm: string) => {
    return matchPermission(initialState?.currentUser?.permissions, perm);
  };
  const roleFiler = (route: { authority: string[] }) => {
    return checkRole(initialState?.currentUser?.roles, route.authority);
  };
  return {
    canAdmin: currentUser && currentUser.access === 'admin',
    hasPerms,
    roleFiler,
  };
}

export function setSessionToken(
  access_token: string | undefined,
  refresh_token: string | undefined,
  expireTime: number,
): void {
  if (access_token) {
    localStorage.setItem('access_token', access_token);
  } else {
    localStorage.removeItem('access_token');
  }
  if (refresh_token) {
    localStorage.setItem('refresh_token', refresh_token);
  } else {
    localStorage.removeItem('refresh_token');
  }
  localStorage.setItem('expireTime', `${expireTime}`);
}

export function getAccessToken() {
  return localStorage.getItem('access_token');
}

export function getRefreshToken() {
  return localStorage.getItem('refresh_token');
}

export function getTokenExpireTime() {
  return localStorage.getItem('expireTime');
}

export function clearSessionToken() {
  sessionStorage.removeItem('user');
  localStorage.removeItem('access_token');
  localStorage.removeItem('refresh_token');
  localStorage.removeItem('expireTime');
}
