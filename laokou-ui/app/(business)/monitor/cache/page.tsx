"use client";

import { useEffect, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import { PageContainer, ProCard } from "@ant-design/pro-components";
import { fetchApi } from "@/app/_modules/func";

import {
  faServer,
  faChartPie,
  faGaugeHigh,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Statistic, Col, Row, Flex } from "antd";

import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Doughnut } from "react-chartjs-2";

import CountUp from "react-countup";

import ReactSpeedometer from "react-d3-speedometer";

ChartJS.register(ArcElement, Tooltip, Legend);

//查询API
const queryAPI = "/api/monitor/cache";

//图表相关定义
type ChartDataSet = {
  label: string;
  data: string[];
  backgroundColor: string[];
};

type ChartDataType = {
  labels: string[];
  datasets: Array<ChartDataSet>;
};

export default function Cache() {
  const { push } = useRouter();
  const [data, setData] = useState(undefined as any);
  const [commandChartData, setCommandChartData] = useState<ChartDataType>(
    {} as ChartDataType
  );

  //查询数据
  const queryData = async () => {
    const body = await fetchApi(queryAPI, push);
    if (body !== undefined) {
      if (body.code == 200) {
        setData(body.data);
        parseCommandChart(body.data.commandStats);
      }
    }
  };

  //处理命令图表数据
  const parseCommandChart = (commandStats: any[]) => {
    const labels: Array<string> = new Array<string>();
    const datas: Array<string> = new Array<string>();

    commandStats.forEach((item) => {
      labels.push(item.name);
      datas.push(item.value);
    });

    const commandSet: ChartDataSet = {
      label: "数量",
      data: datas,
      backgroundColor: [
        "#66BB6A",
        "#FFA726",
        "#42A5F5",
        "#EC407A",
        "#78909C",
        "#FF7043",
        "#26A69A",
        "#9575CD",
        "#FFB74D",
      ],
    };

    const commandData: ChartDataType = {
      labels: labels,
      datasets: [commandSet],
    };

    setCommandChartData(commandData);
  };

  const options = {
    plugins: {
      tooltip: {
        callbacks: {
          label: function (context: any) {
            let label = context.dataset.label || "";
            if (label) {
              label += ": ";
            }

            label +=
              context.parsed +
              "(" +
              Math.round(
                (context.parsed * 100) /
                  context.dataset.data.reduce(
                    (a: string, b: string) => parseInt(a) + parseInt(b),
                    0
                  )
              ) +
              "%)";

            return label;
          },
        },
      },
    },
  };

  const speedParentRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    queryData();
    window.addEventListener("resize", handleResize);
    setDaynamicWidth();
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, [speedParentRef]);

  //数字展示的动态效果
  const formatter = (value: any) => (
    <CountUp end={parseInt(value)} separator="," />
  );

  //超小屏的边界值
  const xsScreenWidth = 768;
  //界面两个ProCard横向布局空白空间宽度
  const spaceTotalWidth = 212;
  //侧边栏展开占据的宽度
  const siderExpand = 214;
  //计算初始的速度表宽度
  const setDaynamicWidth = () => {
    const screenWidth = window.innerWidth;
    if (screenWidth < xsScreenWidth) {
      setParentWidth((screenWidth - spaceTotalWidth) / 2);
    } else if (screenWidth >= xsScreenWidth) {
      setParentWidth((screenWidth - spaceTotalWidth - siderExpand) / 2);
    }
  };

  //速度表绘制的宽度值
  const [speedWidth, setParentWidth] = useState(0);

  //处理屏幕变化时，速度表宽度动态变化
  const handleResize = () => {
    if (speedParentRef.current) {
      setParentWidth(speedParentRef.current.clientWidth);
    } else {
      console.log("no current");
    }
  };

  return (
    <PageContainer title={false}>
      {data !== undefined && (
        <>
          <ProCard
            title={
              <>
                <FontAwesomeIcon icon={faServer} />
                <span style={{ marginLeft: 6 }}>基本信息</span>
              </>
            }
            direction="row"
            headerBordered
            bordered
            hoverable
          >
            <ProCard>
              <Statistic title="Redis版本" value={data.info.redis_version} />
              <Statistic
                title="运行模式"
                value={data.info.redis_mode == "standalone" ? "单机" : "集群"}
              />
              <Statistic
                title="端口"
                value={data.info.tcp_port}
                groupSeparator=""
              />
              <Statistic
                title="客户端数"
                value={data.info.connected_clients}
                formatter={formatter}
              />
            </ProCard>
            <ProCard>
              <Statistic
                title="运行天数"
                value={data.info.uptime_in_days}
                formatter={formatter}
              />
              <Statistic title="使用内存" value={data.info.used_memory_human} />
              <Statistic
                title="使用CPU"
                value={data.info.used_cpu_user_children}
                precision={2}
              />
              <Statistic title="内存配置" value={data.info.maxmemory_human} />
            </ProCard>
            <ProCard>
              <Statistic
                title="AOF是否开启"
                value={data.info.aof_enabled == "0" ? "否" : "是"}
              />
              <Statistic
                title="RDB是否成功"
                value={data.info.rdb_last_bgsave_status}
              />
              <Statistic
                title="Key数量"
                value={data.dbSize}
                formatter={formatter}
              />
              <Statistic
                title="网络入口/出口"
                value={`${data.info.instantaneous_input_kbps}kps/${data.info.instantaneous_output_kbps}kps`}
              />
            </ProCard>
          </ProCard>
          <Row gutter={16} style={{ marginTop: 16 }}>
            <Col span={12}>
              <ProCard
                title={
                  <>
                    <FontAwesomeIcon icon={faChartPie} />
                    <span style={{ marginLeft: 6 }}>命令统计</span>
                  </>
                }
                style={{ height: "100%" }}
                headerBordered
                bordered
                hoverable
              >
                <Flex justify="center">
                  <Doughnut data={commandChartData} options={options} />
                </Flex>
              </ProCard>
            </Col>
            <Col span={12}>
              <ProCard
                title={
                  <>
                    <FontAwesomeIcon icon={faGaugeHigh} />
                    <span style={{ marginLeft: 6 }}>内存信息</span>
                  </>
                }
                style={{ height: "100%" }}
                headerBordered
                bordered
                hoverable
              >
                <Flex justify="center" ref={speedParentRef}>
                  <ReactSpeedometer
                    key={speedWidth}
                    width={speedWidth}
                    currentValueText={`内存消耗：${data.info.used_memory_human}`}
                    value={parseFloat(data.info.used_memory_human)}
                    needleColor="#1677ff"
                    segmentColors={[
                      "#82C182",
                      "#A3D972",
                      "#F5D061",
                      "#FF9933",
                      "#FF6666",
                    ]}
                  />
                </Flex>
              </ProCard>
            </Col>
          </Row>
        </>
      )}
    </PageContainer>
  );
}
