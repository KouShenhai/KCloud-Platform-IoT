import service from './service/index';

const failResponse = {
  code: null,
  data: null,
  message: 'invaild path',
};

export const request = (url, data) =>
  new Promise((resolve, reject) => {
    const waitTime = Math.random() * 300 + 200;
    const target = service.find((item) => item.path === url);
    setTimeout(() => {
      if (target) {
        const { response } = target;
        resolve(typeof response === 'function' ? response(data) : response);
      } else {
        reject(failResponse);
      }
    }, waitTime); // 200-500ms
  });
