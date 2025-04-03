export default {
  path: '/dataCenter/member',
  data: {
    returnType: 'succ',
    generateType: 'template',
    manual: {
      succ: {
        resStr: {
          data: '',
          statusCode: '',
          header: '',
        },
      },
      fail: {
        resStr: {
          errMsg: 'request:fail 填写错误信息',
        },
      },
    },
    template: {
      succ: {
        data: {
          list: [
            {
              name: '浏览量',
              number: '202W',
            },
            {
              name: 'PV',
              number: '233W',
            },
            {
              name: 'UV',
              number: '102W',
            },
          ],
        },
        statusCode: 200,
        header: {
          'content-type': 'application/json; charset=utf-8',
        },
      },
      fail: {
        templateStr: {
          errMsg: 'request:fail 填写错误信息',
        },
      },
    },
  },
};
