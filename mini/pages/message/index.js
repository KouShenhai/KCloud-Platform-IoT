// pages/message/message.js
import { fetchMessageList, markMessagesRead } from '~/mock/chat';

const app = getApp();
const { socket } = app.globalData; // 获取已连接的 socketTask
let currentUser = null; // 当前打开的聊天用户 { userId, eventChannel }

Page({
  /** 页面的初始数据 */
  data: {
    messageList: [], // 完整消息列表 { userId, name, avatar, messages }
    loading: true, // 是否正在加载（用于下拉刷新）
  },

  /** 生命周期函数--监听页面加载 */
  onLoad(options) {
    this.getMessageList();
    // 处理接收到的数据
    socket.onMessage((data) => {
      data = JSON.parse(data);
      if (data.type === 'message') {
        const { userId, message } = data.data;
        const { user, index } = this.getUserById(userId);
        this.data.messageList.splice(index, 1);
        this.data.messageList.unshift(user);
        user.messages.push(message);
        if (currentUser && userId === currentUser.userId) {
          this.setMessagesRead(userId);
          currentUser.eventChannel.emit('update', user);
        }
        this.setData({ messageList: this.data.messageList });
        app.setUnreadNum(this.computeUnreadNum());
      }
    });
  },

  /** 生命周期函数--监听页面初次渲染完成 */
  onReady() {},

  /** 生命周期函数--监听页面显示 */
  onShow() {
    currentUser = null;
  },

  /** 生命周期函数--监听页面隐藏 */
  onHide() {},

  /** 生命周期函数--监听页面卸载 */
  onUnload() {},

  /** 页面相关事件处理函数--监听用户下拉动作 */
  onPullDownRefresh() {},

  /** 页面上拉触底事件的处理函数 */
  onReachBottom() {},

  /** 用户点击右上角分享 */
  onShareAppMessage() {},

  /** 获取完整消息列表 */
  getMessageList() {
    fetchMessageList().then(({ data }) => {
      this.setData({ messageList: data, loading: false });
    });
  },

  /** 通过 userId 获取 user 对象和下标 */
  getUserById(userId) {
    let index = 0;
    while (index < this.data.messageList.length) {
      const user = this.data.messageList[index];
      if (user.userId === userId) return { user, index };
      index += 1;
    }
    // TODO：处理 userId 在列表中不存在的情况（）
  },

  /** 计算未读消息数量 */
  computeUnreadNum() {
    let unreadNum = 0;
    this.data.messageList.forEach(({ messages }) => {
      unreadNum += messages.filter((item) => !item.read).length;
    });
    return unreadNum;
  },

  /** 打开对话页 */
  toChat(event) {
    const { userId } = event.currentTarget.dataset;
    wx.navigateTo({ url: `/pages/chat/index?userId${userId}` }).then(({ eventChannel }) => {
      currentUser = { userId, eventChannel };
      const { user } = this.getUserById(userId);
      eventChannel.emit('update', user);
    });
    this.setMessagesRead(userId);
  },

  /** 将用户的所有消息标记为已读 */
  setMessagesRead(userId) {
    const { user } = this.getUserById(userId);
    user.messages.forEach((message) => {
      message.read = true;
    });
    this.setData({ messageList: this.data.messageList });
    app.setUnreadNum(this.computeUnreadNum());
    markMessagesRead(userId);
  },
});
