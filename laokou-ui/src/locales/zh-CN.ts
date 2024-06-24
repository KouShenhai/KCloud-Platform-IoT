import app from './zh-CN/app';
import component from './zh-CN/component';
import globalHeader from './zh-CN/globalHeader';
import sysmenu from './zh-CN/menu';
import pages from './zh-CN/pages';
import pwa from './zh-CN/pwa';
import settingDrawer from './zh-CN/settingDrawer';
import settings from './zh-CN/settings';
import user from './zh-CN/system/user';
import menu from './zh-CN/system/menu';
import dict from './zh-CN/system/dict';
import dictData from './zh-CN/system/dict-data';
import role from './zh-CN/system/role';
import dept from './zh-CN/system/dept';
import post from './zh-CN/system/post';
import config from './zh-CN/system/config';
import notice from './zh-CN/system/notice';
import operlog from './zh-CN/monitor/operlog';
import logininfor from './zh-CN/monitor/logininfor';
import onlineUser from './zh-CN/monitor/onlineUser';
import job from './zh-CN/monitor/job';
import joblog from './zh-CN/monitor/job-log';
import server from './zh-CN/monitor/server';

export default {
  'navBar.lang': '语言',
  'layout.user.link.help': '帮助',
  'layout.user.link.privacy': '隐私',
  'layout.user.link.terms': '条款',
  'app.copyright.produced': '蚂蚁集团体验技术部出品',
  'app.preview.down.block': '下载此页面到本地项目',
  'app.welcome.link.fetch-blocks': '获取全部区块',
  'app.welcome.link.block-list': '基于 block 开发，快速构建标准页面',
  ...app,
  ...pages,
  ...globalHeader,
  ...sysmenu,
  ...settingDrawer,
  ...settings,
  ...pwa,
  ...component,
  ...user,
  ...menu,
  ...dict,
  ...dictData,
  ...role,
  ...dept,
  ...post,
  ...config,
  ...notice,
  ...operlog,
  ...logininfor,
  ...onlineUser,
  ...job,
  ...joblog,
  ...server,
};
