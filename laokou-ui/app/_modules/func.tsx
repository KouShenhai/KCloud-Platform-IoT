import { getCookie } from "cookies-next";
import JSEncrypt from "jsencrypt";

export async function fetchApi(url: string, push: any, options?: RequestInit) {
  const token = getCookie("token");
  const authHeader = {
    Authorization: "Bearer " + token,
  };

  const requestHeader = {
    ...options?.headers,
    ...authHeader,
    credentials: "include",
  };

  const requestOptions = {
    ...options,
    headers: requestHeader,
  };

  const response = await fetch(url, requestOptions);

  try {
    const body = await response.json();
    if (response.ok) {
      if (body.code == 401) {
        push("/login");
        return;
      }
    }

    return body;
  } catch (error) {
    console.log("fetch error:", error);
  }
}

export async function fetchFile(
  url: string,
  push: any,
  options: RequestInit,
  fileName: string
) {
  const token = getCookie("token");
  const authHeader = {
    Authorization: "Bearer " + token,
  };

  const requestHeader = {
    ...options?.headers,
    ...authHeader,
    credentials: "include",
  };

  const requestOptions = {
    ...options,
    headers: requestHeader,
  };

  const response = await fetch(url, requestOptions);

  try {
    const contentType = response.headers.get("Content-Type");
    //如果文件处理出现了json响应信息，肯定是出错了
    if (contentType && contentType.includes("application/json")) {
      const body = await response.json();
      if (response.ok) {
        if (body.code == 401) {
          push("/login");
          return;
        }
      }
    } else {
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      (a.href = url), (a.download = fileName);
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
    }
  } catch (error) {
    console.log("fetch file error:", error);
  }
}

//公钥
const publicKey =
  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu/JW5RlS6o2Cz3Vlm96u8D+aY3WvzRQyC+B8TQGjOTvtklSXbKYwoccL9RLU1IHUu+Pk1GlZM87OhULq4B3cU9AxXWj24eaxoaWSO4iuMjF7SvsQtWCEmbECJg5WTf8gRi89PjyGntawp04KfR6AXCyTWmTm2vwC+PqoQcnZl9GdNerxeLpD1k7NxSe+syMUaIgM0mVqcirzdPrIVO9v89l5RVxVVUv6XCR3Z05iK8ODQ4ITCoJB8XrGlDPa3TqDlnpME4/upLx3u6oogIRncIPw3P2kAF2viwoll0H23fKkNQvsLdH9jUw+zl0ZJjSb/e2fcMYYrT+AlER3zBOW/wIDAQAB";

//私钥
const privateKey =
  "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC78lblGVLqjYLPdWWb3q7wP5pjda/NFDIL4HxNAaM5O+2SVJdspjChxwv1EtTUgdS74+TUaVkzzs6FQurgHdxT0DFdaPbh5rGhpZI7iK4yMXtK+xC1YISZsQImDlZN/yBGLz0+PIae1rCnTgp9HoBcLJNaZOba/AL4+qhBydmX0Z016vF4ukPWTs3FJ76zIxRoiAzSZWpyKvN0+shU72/z2XlFXFVVS/pcJHdnTmIrw4NDghMKgkHxesaUM9rdOoOWekwTj+6kvHe7qiiAhGdwg/Dc/aQAXa+LCiWXQfbd8qQ1C+wt0f2NTD7OXRkmNJv97Z9wxhitP4CURHfME5b/AgMBAAECggEACOp9Ls8dvNzLuNXD5ToSKHmL9G3v0hXELgYPP4P1X1C1e3yh1lin8/TCX3TuPcqO8f7kqyL4RVnpOC8tf0ZLXnqA7QJ+u8a65IU7Q7G/OchZJfx1FXWntLbN+Eoz0+1ndYzmJd6vMDfVF4q/OqJIypaewuoIfZj49yDE/KH7vZTyQQi8dmP2Re86WjlJyt+FrWUpzQh7R/NcDaRJ1fUBpOB+PZ+WYFK4BHTmvZ6f/XDYSbuiIMEcA/BejklaIFIAv9mMZ7MBCs+pPvgtleIlvfZwmCEEwYWCnZPncYovtaeKmTY7KMjp5LbyAeK/mqblqhiXl9mp3NM1Tk6doCKbyQKBgQDkhLlG50t6BO/xl7kkDUkG5QRe6MctoiN5BB/tNlLWImOhzNka+JPu278R2n6Ls4fYD74RxKwcy8a0683TsVC1mo8S/ck6o6HlDmBB+N6IMbMF8snKksKM6yytvOj4O3V8KZdaJDGzTz/CUEIWKUfBCQmbl7iFhOwpo7bNzxA0QwKBgQDSjI2Ew0v8TUDdNfFcd3exDQm3HmA1XcTIAapmVsgcLK76IIDsJSFqA+b8cHTM1I01kiK1kadPy9ZIR1NNb5ZjCNJqzIpzTI2ZdZdBgyQh7tjw9hdzcd2n7aKXmahZ9vNgzGVOhCT4A4RmBvozCuHLpEHT431A8L04WTdLEalklQKBgA8Fenhis99te6hR5OWtyeMeIs9qVc12HwbRcpfRPli9Ifd807imJnNJFqJBzpe4UXGudzwLxZSPAJzb80e7HCcT5dvFuviT0QyRiVpM1bP2MGJvtzwNsaQ5wVIaXOYUYoCq6zwNrQawauyHAhEa3ZCe23bS3lpIho2mKVoWBmapAoGAbepMMuPVdjhKRXFUuEXx6S76RGuKJDH4ecVM1LI3M2YsTo3LX/weTn8NBfobL5dCxJWuowUPyDuMeR0rIsC/TKIdXv26xWhQf62AsgWpRkGvZVPDeFQYOAN5nxTra1PdSEpMFMotloAXjT/VO/JRYAM3DkuzZsSGs7T3hawJt2UCgYEAmVE0x7c1zxD62h68v2jmLdPbG5UVmuSSwVod2PqSv5WDY/7tg1HUh4/UcSX/9rbXXzacMTBE/aJDZkusFSThsmMFkpKmt5a2JOtsbzwFHo0kSCOngUBwxyPY+JEMgDJo9Y+6fnYNnhQklrjiyAdaUGb4YrPQSNas1G29OzE5Mcs=";

//加密数据
export function encrypt(content: string) {
  const encryptor = new JSEncrypt();
  encryptor.setPublicKey(publicKey);
  return encryptor.encrypt(content);
}

//解密数据
export function decrypt(content: string) {
  const encryptor = new JSEncrypt();
  encryptor.setPrivateKey(privateKey);
  return encryptor.decrypt(content);
}

//获取当前浏览器是否深色模式
export function displayModeIsDark() {
  if (typeof window !== 'undefined') {
    return window.matchMedia("(prefers-color-scheme: dark)").matches;
  }

  return false;
}

//动态获取浏览器是否深色模式
export function watchDarkModeChange(callback: (value: boolean) => void) {
  if (typeof window !== 'undefined') {
    const darkModeQuery = window.matchMedia("(prefers-color-scheme: dark)");

    const onChange = (e: any) => {
      callback(e.matches);
    };

    darkModeQuery.addEventListener("change", onChange);

    return () => {
      darkModeQuery.removeEventListener("change", onChange);
    };
  }

  return () => {};
}
