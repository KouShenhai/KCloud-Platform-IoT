export default {
  path: '/dataCenter/interaction',
  data: {
    returnType: 'succ',
    generateType: 'template',
    manual: {
      succ: {
        resStr: { data: '', statusCode: '', header: '' },
      },
      fail: {
        resStr: { errMsg: 'request:fail 填写错误信息' },
      },
    },
    template: {
      succ: {
        data: {
          list: [
            { name: '浏览量', number: '919' },
            { name: '点赞量', number: '887' },
            { name: '分享量', number: '104' },
            { name: '收藏', number: '47' },
          ],
        },
        statusCode: 200,
        header: { 'content-type': 'application/json; charset=utf-8' },
      },
      fail: {
        templateStr: { errMsg: 'request:fail 填写错误信息' },
      },
    },
  },
};
