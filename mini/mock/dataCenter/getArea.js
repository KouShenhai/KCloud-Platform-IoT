export default {
  path: '/dataCenter/area',
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
              标题: '视频A',
              全球: '4442',
              华北: '456',
              华东: '456',
            },
            {
              标题: '视频A',
              全球: '4442',
              华北: '456',
              华东: '456',
            },
            {
              标题: '视频A',
              全球: '4442',
              华北: '456',
              华东: '456',
            },
            {
              标题: '视频A',
              全球: '4442',
              华北: '456',
              华东: '456',
            },
            {
              标题: '视频A',
              全球: '4442',
              华北: '456',
              华东: '456',
            },
            {
              标题: '视频A',
              全球: '4442',
              华北: '456',
              华东: '456',
            },
            {
              标题: '视频A',
              全球: '4442',
              华北: '456',
              华东: '456',
            },
            {
              标题: '视频A',
              全球: '4442',
              华北: '456',
              华东: '456',
            },
            {
              标题: '视频A',
              全球: '4442',
              华北: '456',
              华东: '456',
            },
            {
              标题: '视频A',
              全球: '4442',
              华北: '456',
              华东: '456',
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
