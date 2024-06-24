"use client";
import { LockOutlined, UserOutlined } from "@ant-design/icons";
import {
	LoginForm,
	ProConfigProvider,
	ProFormText,
} from "@ant-design/pro-components";
import { message, Spin, theme } from "antd";
import { setCookie, getCookie } from "cookies-next";
import { useRouter } from "next/navigation";

import type { ProFormInstance } from "@ant-design/pro-components";

import Image from "next/image";

import { useEffect, useState, useRef } from "react";
import { LoginParam } from "../_modules/definies";
import {
  decrypt,
  displayModeIsDark,
  watchDarkModeChange,
} from "../_modules/func";

type Captcha = {
  img: string;
  uuid: string;
};



//cookies 记住的用户名 key
const cookie_username_key = "mortnon_username";
//cookies 记住的密码 key
const cookie_password_key = "mortnon_password";

//浅色背景图
const backgroudLight = "/bg3.jpg";
//深色前景图
const backgroundDark = "/bg-dark.jpg";

export default function Login() {
  // 登录背景样式
  // const containerClassName = useEmotionCss(() => {
  //
  // })


  //验证码数据
  const [captcha, setCaptcha] = useState({} as Captcha);
  //是否展示验证码框
  const [showCaptcha, setShowCaptcha] = useState(false);
  //验证码加载状态
  const [isLoadingImg, setIsLoadingImg] = useState(true);

  //获取验证码
  const getCaptcha = async () => {
    try {
      const response = await fetch("/api/captchaImage");
      if (response.ok) {
        const data = await response.json();

        setShowCaptcha(data.captchaEnabled);

        if (data.captchaEnabled) {
          const imagePrefix = "data:image/gif;base64,";

          const captchaData: Captcha = {
            img: imagePrefix + data.img,
            uuid: data.uuid,
          };

          setCaptcha(captchaData);
          setIsLoadingImg(false);
        }
      } else {
      }
    } catch (error) {
    } finally {
    }
  };

  //深色模式
  const [isDark, setIsDark] = useState(false);
  //背景图片
  const [background, setBackground] = useState(backgroudLight);

  useEffect(() => {
    getCaptcha();
    readUserNamePassword();
    setIsDark(displayModeIsDark());
    setBackground(displayModeIsDark() ? backgroundDark : backgroudLight);
    const unsubscribe = watchDarkModeChange((matches: boolean) => {
      setIsDark(matches);
      setBackground(matches ? backgroundDark : backgroudLight);
    });
    return () => {
      unsubscribe();
    };
  }, []);

  const router = useRouter();

  //提交登录
  const userLogin = async (values: any) => {
    const params: LoginParam = {
      username: values.username,
      password: values.password,
      captcha: values.code,
      uuid: captcha.uuid,
	  tenant_id: 0,
	  grant_type: 'password'
    };

    try {
      const response = await fetch("/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(params),
        credentials: "include",
      });

      //获得响应
      if (response.ok) {
        const data = await response.json();
        //登录成功
        if (data.code == 200) {
          message.success("登录成功");
          setCookie("token", data.token);
          router.push("/");
        } else {
          message.open({
            type: "error",
            content: data.msg,
          });
          //异常，自动刷新验证码
          getCaptcha();
        }
      } else {
        const data = await response.json();
        message.open({
          type: "error",
          content: data.msg,
        });
      }
    } catch (error) {
      console.log("error:", error);
      message.open({
        type: "error",
        content: "登录发生异常，请重试",
      });
    } finally {
    }
  };

  const loginFormRef = useRef<ProFormInstance>();

  //读取cookie中用户名密码，并填写到表单中
  const readUserNamePassword = () => {
    const username = getCookie(cookie_username_key);
    const password = getCookie(cookie_password_key);

    if (username !== undefined && password !== undefined) {
      if (loginFormRef) {
        loginFormRef.current?.setFieldsValue({
          username: decrypt(username),
          password: decrypt(password)
        });
      }
    }
  };

  const { token } = theme.useToken();

  return (
    <ProConfigProvider dark={isDark}>
      <div
        style={{
          backgroundColor: "white",
          height: "100vh",
        }}
      >
        <LoginForm
          formRef={loginFormRef}
		  logo={<img alt="logo" src="/logo.png" />}
          title="老寇IoT云平台"
          containerStyle={{
            backgroundColor: "rgba(0,0,0,0)",
            backdropFilter: "blur(4px)",
          }}
          actions={
            <div
              style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              <p style={{ color: "rgba(255,255,255,.6)" }}>
                ©{new Date().getFullYear()} Mortnon.
              </p>
            </div>
          }
          onFinish={userLogin}>
          <>
            <ProFormText
              name="username"
              fieldProps={{
                size: "large",
                prefix: (
                  <UserOutlined
                    style={{
                      color: token.colorText,
                    }}
                    className={"prefixIcon"}
                  />
                ),
              }}
              placeholder={"用户名"}
              rules={[
                {
                  required: true,
                  message: "用户名不能为空",
                },
              ]}
            />
            <ProFormText.Password
              name="password"
              fieldProps={{
                size: "large",
                prefix: (
                  <LockOutlined
                    style={{
                      color: token.colorText,
                    }}
                    className={"prefixIcon"}
                  />
                ),
              }}
              placeholder={"密码"}
              rules={[
                {
                  required: true,
                  message: "密码不能为空",
                },
              ]}
            />
            {showCaptcha && (
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  flexDirection: "row",
                }}
              >
                <ProFormText
                  name="code"
                  fieldProps={{
                    size: "large",
                    prefix: (
                      <UserOutlined
                        style={{
                          color: token.colorText,
                        }}
                        className={"prefixIcon"}
                      />
                    ),
                  }}
                  placeholder={"验证码"}
                  rules={[
                    {
                      required: true,
                      message: "验证码不能为空",
                    },
                  ]}
                />

                <div style={{ margin: "0 0 0 8px" }}>
                  <Spin spinning={isLoadingImg}>
                    {captcha.img === undefined ? (
                      <div style={{ width: 80, height: 40 }}></div>
                    ) : (
                      <Image
                        src={captcha.img}
                        width={80}
                        height={40}
                        alt="captcha"
                        onClick={getCaptcha}
                      />
                    )}
                  </Spin>
                </div>
              </div>
            )}
          </>
          <div
            style={{
              marginBlockEnd: 24,
            }}>
          </div>
        </LoginForm>
      </div>
    </ProConfigProvider>
  );
}
