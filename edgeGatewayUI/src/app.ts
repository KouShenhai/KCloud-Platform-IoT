import type { RequestConfig } from '@umijs/max';

interface CurrentUser {
  id?: string;
  username?: string;
  role?: string;
}

const parseCurrentUser = (): CurrentUser | undefined => {
  const token = localStorage.getItem('edge_token');
  if (!token) {
    return undefined;
  }

  try {
    const body = token.split('.')[1] || '';
    const normalized = body.replace(/-/g, '+').replace(/_/g, '/');
    const padded = normalized.padEnd(
      normalized.length + ((4 - (normalized.length % 4)) % 4),
      '=',
    );
    const payload = JSON.parse(atob(padded));
    return {
      id: payload.userId,
      username: payload.username,
      role: payload.role,
    };
  } catch {
    localStorage.removeItem('edge_token');
    return undefined;
  }
};

export async function getInitialState(): Promise<{
  name: string;
  currentUser?: CurrentUser;
}> {
  const currentUser = parseCurrentUser();
  return {
    name: currentUser?.username || 'Edge Gateway',
    currentUser,
  };
}

export const layout = ({
  initialState,
}: {
  initialState?: { name?: string };
}) => {
  return {
    logo: 'https://img.alicdn.com/tfs/TB1YHEpwUT1gK0jSZFhXXaAtVXa-28-27.svg',
    rightContentRender: () => initialState?.name || 'Edge Gateway',
    menu: {
      locale: false,
    },
  };
};

export const request: RequestConfig = {
  timeout: 10000,
  errorConfig: {
    errorHandler() {},
    errorThrower() {},
  },
  requestInterceptors: [
    (config: any) => {
      const token = localStorage.getItem('edge_token');
      if (token) {
        config.headers = {
          ...config.headers,
          Authorization: `Bearer ${token}`,
        };
      }
      return config;
    },
  ],
};
