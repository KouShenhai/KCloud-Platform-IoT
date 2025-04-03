// pages/release/index.js

Page({
  /**
   * 页面的初始数据
   */
  data: {
    originFiles: [
      {
        url: '/static/image1.png',
        name: 'uploaded1.png',
        type: 'image',
      },
      {
        url: '/static/image2.png',
        name: 'uploaded2.png',
        type: 'image',
      },
    ],
    gridConfig: {
      column: 4,
      width: 160,
      height: 160,
    },
    config: {
      count: 1,
    },
    tags: ['AI绘画', '版权素材', '原创', '风格灵动'],
  },
  handleSuccess(e) {
    const { files } = e.detail;
    this.setData({
      originFiles: files,
    });
  },
  handleRemove(e) {
    const { index } = e.detail;
    const { originFiles } = this.data;
    originFiles.splice(index, 1);
    this.setData({
      originFiles,
    });
  },
  gotoMap() {
    wx.showToast({
      title: '获取当前位置...',
      icon: 'none',
      image: '',
      duration: 1500,
      mask: false,
      success: () => {},
      fail: () => {},
      complete: () => {},
    });
  },
  saveDraft() {
    wx.reLaunch({
      url: `/pages/home/index?oper=save`,
    });
  },
  release() {
    wx.reLaunch({
      url: `/pages/home/index?oper=release`,
    });
  },
});
