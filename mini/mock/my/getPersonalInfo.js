import { getLocalUrl } from '~/utils/util.js';

export default {
  path: '/api/genPersonalInfo',
  data: {
    code: 200,
    message: 'success',
    data: {
      image: '/static/avatar1.png',
      name: '小小轩',
      star: '天枰座',
      gender: 0,
      birth: '1994-09-27',
      address: ['440000', '440300'],
      brief: '在你身边，为你设计',
      photos: [
        {
          url: getLocalUrl('/static/img_td.png', 'uploaded1.png'),
          name: 'uploaded1.png',
          type: 'image',
        },
        {
          url: getLocalUrl('/static/img_td.png', 'uploaded2.png'),
          name: 'uploaded2.png',
          type: 'image',
        },
      ],
    },
  },
};
