import React, { Suspense } from 'react'
import Home from '@/app/_client/component/home/Home';
import withAuth from '@/app/_client/auth/auth';
const HomeLayout = ({ children }: React.PropsWithChildren) => {
  
  return (
    <div>
         <Home children={children} />
    </div>
  );
};
export default withAuth(HomeLayout);

