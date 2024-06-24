import { ReactNode } from "react";

import { CronLocalization } from "@sbzen/re-cron";

import {
  ApiOutlined,
  BookOutlined,
  CodeOutlined,
  EditOutlined,
  HomeOutlined,
  MessageOutlined,
  MonitorOutlined,
  ToolOutlined,
  UserOutlined,
  AreaChartOutlined,
  PieChartOutlined,
  BarChartOutlined,
  LineChartOutlined,
  SlidersOutlined,
  PhoneOutlined,
  AndroidOutlined,
  AppleOutlined,
  WindowsOutlined,
  ChromeOutlined,
  WechatOutlined,
  AccountBookOutlined,
  BankOutlined,
  BugOutlined,
  CarOutlined,
  ClearOutlined,
  CloudOutlined,
  EnvironmentOutlined,
  ExperimentOutlined,
  FormatPainterOutlined,
  MailOutlined,
  ShoppingCartOutlined,
  SyncOutlined,
  WifiOutlined,
} from "@ant-design/icons";

import {
  faAddressCard,
  faBookAtlas,
  faChalkboardUser,
  faDatabase,
  faDesktop,
  faFileWaveform,
  faGear,
  faList,
  faLocationArrow,
  faMemory,
  faReceipt,
  faRectangleList,
  faSitemap,
  faTableCells,
  faThumbtack,
  faUsers,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

//登录请求
export type LoginParam = {
  uuid: string;
  captcha: string;
  username: string;
  password: string;
  tenant_id: number;
  grant_type: string;
};

//用户简单信息定义
export type UserInfo = {
  nickName: string;
  avatar: string;
};

//用户详细信息
export type UserDetailInfo = {
  userName: string;
  phonenumber: string;
  email: string;
  deptName: string;
  postGroup: string;
  roleName: string;
  nickName: string;
  sex: number | string;
  createTime: string;
};

//路由/菜单定义
export type RouteInfo = {
  path: string;
  name?: string;
  icon?: ReactNode;
  routes?: Array<RouteInfo>;
};

export type IconType = {
  [key: string]: ReactNode;
};

//图标映射
export const IconMap: IconType = {
  system: <FontAwesomeIcon icon={faGear} />,
  monitor: <MonitorOutlined />,
  tool: <ToolOutlined />,
  guide: <FontAwesomeIcon icon={faLocationArrow} />,
  user: <UserOutlined />,
  peoples: <FontAwesomeIcon icon={faUsers} />,
  treetable: <FontAwesomeIcon icon={faList} />,
  tree: <FontAwesomeIcon icon={faSitemap} />,
  post: <FontAwesomeIcon icon={faAddressCard} />,
  dict: <FontAwesomeIcon icon={faBookAtlas} />,
  edit: <EditOutlined />,
  message: <MessageOutlined />,
  log: <BookOutlined />,
  online: <FontAwesomeIcon icon={faChalkboardUser} />,
  job: <FontAwesomeIcon icon={faThumbtack} />,
  druid: <FontAwesomeIcon icon={faFileWaveform} />,
  server: <FontAwesomeIcon icon={faDesktop} />,
  redis: <FontAwesomeIcon icon={faDatabase} />,
  redislist: <FontAwesomeIcon icon={faMemory} />,
  build: <FontAwesomeIcon icon={faTableCells} />,
  code: <CodeOutlined />,
  swagger: <ApiOutlined />,
  form: <FontAwesomeIcon icon={faRectangleList} />,
  logininfor: <FontAwesomeIcon icon={faReceipt} />,
  areachart: <AreaChartOutlined />,
  pie: <PieChartOutlined />,
  barchart: <BarChartOutlined />,
  linechart: <LineChartOutlined />,
  slider: <SlidersOutlined />,
  phone: <PhoneOutlined />,
  android: <AndroidOutlined />,
  apple: <AppleOutlined />,
  window: <WindowsOutlined />,
  chrome: <ChromeOutlined />,
  wechat: <WechatOutlined />,
  account: <AccountBookOutlined />,
  bank: <BankOutlined />,
  bug: <BugOutlined />,
  car: <CarOutlined />,
  clear: <ClearOutlined />,
  cloud: <CloudOutlined />,
  command: <CodeOutlined />,
  map: <EnvironmentOutlined />,
  experiment: <ExperimentOutlined />,
  painter: <FormatPainterOutlined />,
  home: <HomeOutlined />,
  mail: <MailOutlined />,
  shop: <ShoppingCartOutlined />,
  sync: <SyncOutlined />,
  wifi: <WifiOutlined />,
};

//Cron框本地化内容
export const MortnonCronLocalization: CronLocalization = {
  common: {
    month: {
      january: "一月",
      february: "二月",
      march: "三月",
      april: "四月",
      may: "五月",
      june: "六月",
      july: "七月",
      august: "八月",
      september: "九月",
      october: "十月",
      november: "十一月",
      december: "十二月",
    },
    dayOfWeek: {
      sunday: "星期天",
      monday: "星期一",
      tuesday: "星期二",
      wednesday: "星期三",
      thursday: "星期四",
      friday: "星期五",
      saturday: "星期六",
    },
    dayOfMonth: {
      "1st": "第1",
      "2nd": "第2",
      "3rd": "第3",
      "4th": "第4",
      "5th": "第5",
      "6th": "第6",
      "7th": "第7",
      "8th": "第8",
      "9th": "第9",
      "10th": "第10",
      "11th": "第11",
      "12th": "第12",
      "13th": "第13",
      "14th": "第14",
      "15th": "第15",
      "16th": "第16",
      "17th": "第17",
      "18th": "第18",
      "19th": "第19",
      "20th": "第20",
      "21st": "第21",
      "22nd": "第22",
      "23rd": "第23",
      "24th": "第24",
      "25th": "第25",
      "26th": "第26",
      "27th": "第27",
      "28th": "第28",
      "29th": "第29",
      "30th": "第30",
      "31st": "第31",
    },
  },
  tabs: {
    seconds: "秒",
    minutes: "分种",
    hours: "小时",
    day: "天",
    month: "月",
    year: "年",
  },
  quartz: {
    day: {
      every: {
        label: "每天",
      },
      dayOfWeekIncrement: {
        label1: "每",
        label2: "天，从这天开始：",
      },
      dayOfMonthIncrement: {
        label1: "每",
        label2: "天，从本月",
        label3: "天开始",
      },
      dayOfWeekAnd: {
        label: "指定每周的某天（选一天或多天）",
      },
      dayOfWeekRange: {
        label1: "每一天，从",
        label2: "到",
      },
      dayOfMonthAnd: {
        label: "指定每月的某天（选一天或多天）",
      },
      dayOfMonthLastDay: {
        label: "在当月最后一天",
      },
      dayOfMonthLastDayWeek: {
        label: "在当月最后一个工作日",
      },
      dayOfWeekLastNTHDayWeek: {
        label1: "在当月的最后一个",
        label2: "",
      },
      dayOfMonthDaysBeforeEndMonth: {
        label: "天（在当月结束前）",
      },
      dayOfMonthNearestWeekDayOfMonth: {
        label1: "最接近当月",
        label2: "天的工作日（星期一至星期五）",
      },
      dayOfWeekNTHWeekDayOfMonth: {
        label1: "在当月",
        label2: "",
      },
    },
    month: {
      every: {
        label: "每月",
      },
      increment: {
        label1: "每",
        label2: "个月，从这个月开始：",
      },
      and: {
        label: "指定月份（选择一个或多个）",
      },
      range: {
        label1: "每个月，从",
        label2: "到",
      },
    },
    second: {
      every: {
        label: "每秒",
      },
      increment: {
        label1: "每",
        label2: "秒，从这一秒开始：",
      },
      and: {
        label: "指定秒（选择一个或多个）",
      },
      range: {
        label1: "每秒，从",
        label2: "到",
      },
    },
    minute: {
      every: {
        label: "每分钟",
      },
      increment: {
        label1: "每",
        label2: "分钟，从这一分钟开始：",
      },
      and: {
        label: "指定分钟（选择一个或多个）",
      },
      range: {
        label1: "每分钟，从",
        label2: "到",
      },
    },
    hour: {
      every: {
        label: "每小时",
      },
      increment: {
        label1: "每",
        label2: "小时，从这一小时开始：",
      },
      and: {
        label: "指定小时（选择一个或多个）",
      },
      range: {
        label1: "每小时，从",
        label2: "到",
      },
    },
    year: {
      every: {
        label: "任意年份",
      },
      increment: {
        label1: "每",
        label2: "年，从这一年开始：",
      },
      and: {
        label: "指定年份（选择一个或多个）",
      },
      range: {
        label1: "每年，从",
        label2: "到",
      },
    },
  }
};
