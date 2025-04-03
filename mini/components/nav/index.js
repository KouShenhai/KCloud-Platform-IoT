Component({
  options: {
    styleIsolation: 'shared',
  },
  properties: {
    navType: {
      type: String,
      value: 'title',
    },
    titleText: String,
  },
  data: {
    visible: false,
    sidebar: [
      {
        title: '首页',
        url: 'pages/home/index',
        isSidebar: true,
      },
      {
        title: '搜索页',
        url: 'pages/search/index',
        isSidebar: false,
      },
      {
        title: '发布页',
        url: 'pages/release/index',
        isSidebar: false,
      },
      {
        title: '消息列表页',
        url: 'pages/message/index',
        isSidebar: true,
      },
      {
        title: '对话页',
        url: 'pages/chat/index',
        isSidebar: false,
      },
      {
        title: '个人中心页',
        url: 'pages/my/index',
        isSidebar: true,
      },
      {
        title: '个人信息表单页',
        url: 'pages/my/info-edit/index',
        isSidebar: false,
      },
      {
        title: '设置页',
        url: 'pages/setting/index',
        isSidebar: false,
      },
      {
        title: '数据图表页',
        url: 'pages/dataCenter/index',
        isSidebar: false,
      },
      {
        title: '登录注册页',
        url: 'pages/login/login',
        isSidebar: false,
      },
    ],
    statusHeight: 0,
  },
  lifetimes: {
    ready() {
      const statusHeight = wx.getWindowInfo().statusBarHeight;
      this.setData({ statusHeight });
    },
  },
  methods: {
    openDrawer() {
      this.setData({
        visible: true,
      });
    },
    itemClick(e) {
      const that = this;
      const { isSidebar, url } = e.detail.item;
      if (isSidebar) {
        wx.switchTab({
          url: `/${url}`,
        }).then(() => {
          // 防止点回tab时，sidebar依旧是展开模式
          that.setData({
            visible: false,
          });
        });
      } else {
        wx.navigateTo({
          url: `/${url}`,
        }).then(() => {
          that.setData({
            visible: false,
          });
        });
      }
    },

    searchTurn() {
      wx.navigateTo({
        url: `/pages/search/index`,
      });
    },
  },
});
