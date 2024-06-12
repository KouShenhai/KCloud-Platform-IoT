import {redirect } from 'next/navigation';
import serverToken from '@/app/_server/utils/getToken'


const withAuth = (WrappedComponent: React.ComponentType) => {
  return (props: any) => {
    // router.
    // 检查用户是否登录，如果未登录则重定向到登录页面
    if (!serverToken()) {
        redirect('/')
      return null;
    }
    // console.log('@',props)
    // 如果用户已登录，则渲染被包裹的组件
    return <WrappedComponent {...props} />;
  };
};
export default withAuth;

