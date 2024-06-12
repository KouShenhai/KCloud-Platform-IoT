import { cookies } from "next/headers";

const serverToken = ():string|undefined=>{
return cookies().get("token")?.value;
}

export default serverToken 