export default (
  initialState: {
    name?: string;
    currentUser?: {
      id?: string;
      username?: string;
      role?: string;
    };
  } = {},
) => {
  const canSeeAdmin = !!initialState.currentUser;
  const canManageUsers = initialState.currentUser?.role === 'admin';
  const canManageMenus = initialState.currentUser?.role === 'admin';

  return {
    canSeeAdmin,
    canManageUsers,
    canManageMenus,
  };
};
