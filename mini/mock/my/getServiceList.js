export default {
  path: '/api/getServiceList',
  data: {
    code: 200,
    message: 'success',
    data: {
      service: [
        { image: '/static/icon_wx.png', name: '微信', type: 'weixin', url: '' },
        { image: '/static/icon_qq.png', name: 'QQ', type: 'QQ', url: '' },
        { image: '/static/icon_doc.png', name: '腾讯文档', type: 'document', url: '' },
        { image: '/static/icon_map.png', name: '腾讯地图', type: 'map', url: '' },
        { image: '/static/icon_td.png', name: '数据中心', type: 'data', url: '/pages/dataCenter/index' },
        { image: '/static/icon_td.png', name: '数据中心', type: 'data', url: '/pages/dataCenter/index' },
        { image: '/static/icon_td.png', name: '数据中心', type: 'data', url: '/pages/dataCenter/index' },
        { image: '/static/icon_td.png', name: '数据中心', type: 'data', url: '/pages/dataCenter/index' },
      ],
    },
  },
};
