'use client'
 
import { usePathname,useRouter,redirect } from 'next/navigation'
import { getToken } from '@/app/_client/utils/token';
export function NavigationEvents({ children }: React.PropsWithChildren) {

  const pathname = usePathname();
  const router = useRouter();

//   const searchParams = useSearchParams()
console.log('----url',pathname,getToken())
  if(pathname==='/home/Icons'){
    redirect('/')
  }
  return (
    <div>
      { children }
    </div>
  )
}
