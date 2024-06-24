import { request } from '@umijs/max';

const mimeMap = {
  xlsx: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  zip: 'application/zip',
};

/**
 * 解析blob响应内容并下载
 * @param {*} res blob响应内容
 * @param {String} mimeType MIME类型
 */
export function resolveBlob(res: any, mimeType: string) {
  const aLink = document.createElement('a');
  const blob = new Blob([res.data], { type: mimeType });
  // //从response的headers中获取filename, 后端response.setHeader("Content-disposition", "attachment; filename=xxxx.docx") 设置的文件名;
  const patt = new RegExp('filename=([^;]+\\.[^\\.;]+);*');
  // console.log(res);
  const contentDisposition = decodeURI(res.headers['content-disposition']);
  const result = patt.exec(contentDisposition);
  let fileName = result ? result[1] : 'file';
  fileName = fileName.replace(/"/g, '');
  aLink.style.display = 'none';
  aLink.href = URL.createObjectURL(blob);
  aLink.setAttribute('download', fileName); // 设置下载文件名称
  document.body.appendChild(aLink);
  aLink.click();
  URL.revokeObjectURL(aLink.href); // 清除引用
  document.body.removeChild(aLink);
}

export function downLoadZip(url: string) {
  request(url, {
    method: 'GET',
    responseType: 'blob',
    getResponse: true,
  }).then((res) => {
    resolveBlob(res, mimeMap.zip);
  });
}

export async function downLoadXlsx(url: string, params: any, fileName: string) {
  return request(url, {
    ...params,
    method: 'POST',
    responseType: 'blob',
  }).then((data) => {
    const aLink = document.createElement('a');
    const blob = data as any; // new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    aLink.style.display = 'none';
    aLink.href = URL.createObjectURL(blob);
    aLink.setAttribute('download', fileName); // 设置下载文件名称
    document.body.appendChild(aLink);
    aLink.click();
    URL.revokeObjectURL(aLink.href); // 清除引用
    document.body.removeChild(aLink);
  });
}


export function download(fileName: string) {
  window.location.href = `/api/common/download?fileName=${encodeURI(fileName)}&delete=${true}`;
}
