import useToastBehavior from '~/behaviors/useToast';

Page({
  behaviors: [useToastBehavior],
  data: {
    menuData: [
      [
        {
          title: '通用设置',
          url: '',
          icon: 'app',
        },
        {
          title: '通知设置',
          url: '',
          icon: 'notification',
        },
      ],
      [
        {
          title: '深色模式',
          url: '',
          icon: 'image',
        },
        {
          title: '字体大小',
          url: '',
          icon: 'chart',
        },
        {
          title: '播放设置',
          url: '',
          icon: 'sound',
        },
      ],
      [
        {
          title: '账号安全',
          url: '',
          icon: 'secured',
        },
        {
          title: '隐私',
          url: '',
          icon: 'info-circle',
        },
      ],
    ],
  },

  onEleClick(e) {
    const { title, url } = e.currentTarget.dataset.data;
    if (url) return;
    this.onShowToast('#t-toast', title);
  },
});
