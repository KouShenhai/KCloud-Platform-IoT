export default {
  path: '/dataCenter/complete-rate',
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
              time: '12:00',
              percentage: '80',
            },
            {
              time: '14:00',
              percentage: '60',
            },
            {
              time: '16:00',
              percentage: '85',
            },
            {
              time: '18:00',
              percentage: '43',
            },
            {
              time: '20:00',
              percentage: '60',
            },
            {
              time: '22:00',
              percentage: '95',
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
