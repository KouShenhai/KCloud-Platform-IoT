"use client";

import { Flex, Spin } from "antd";

import { getCookie } from "cookies-next";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

import PuffLoader from "react-spinners/PuffLoader";

import styles from "./page.module.css";
import { displayModeIsDark } from "./_modules/func";

//浅色
const lightColor = "white";
//深色
const darkColor = "black";
//主要颜色
const color = "#1677ff";

export default function Home() {
  const { push } = useRouter();

  //是否深色模式
  const [backgroundColor, setBackgroundColor] = useState(lightColor);

  useEffect(() => {
    const token = getCookie("token");
    if (token === "") {
      push("/login");
    } else {
      push("/home");
    }

    setBackgroundColor(displayModeIsDark() ? darkColor : lightColor);
  }, []);

  return (
    <Flex
      vertical
      className={styles.bodyContent}
      style={{ backgroundColor: backgroundColor }}
      justify="center"
      align="center"
    >
      <PuffLoader
        color={color}
        loading={true}
        size={150}
        aria-label="Loading"
      />
      <span style={{ color: color, marginTop: "16px" }}>
        MorTnon，高质量的快速开发框架
      </span>
    </Flex>
  );
}
