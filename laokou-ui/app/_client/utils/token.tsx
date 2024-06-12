import { setCookie ,parseCookies } from 'nookies';

const setToken = (token: string, expire: number) => {
  if(token&&expire){
    setCookie(null, 'token', token, {
      maxAge: expire,
      path: '/',
    })
  }
}
const getToken = ():string=>{
  const cookies = parseCookies();
  const {token}= cookies;
  return token;
}

const setRefreshToken = (refreshToken: string) => {
  if (refreshToken) {
    localStorage.setItem("refreshToken", refreshToken)
  }
}
const getRefreshToken = () => {
  localStorage.get("refreshToken")
}

export default setToken
export { setRefreshToken, getRefreshToken,getToken }
