import request  from "@/app/_client/utils/request";

export interface OssCO {
    /**
     * OSS的访问密钥
     */
    accessKey?: string;
    /**
     * OSS的桶名
     */
    bucketName?: string;
    /**
     * OSS的终端地址
     */
    endpoint?: string;
    /**
     * ID
     */
    id?: number;
    /**
     * OSS名称
     */
    name?: string;
    /**
     * 路径样式访问 1已开启 0未启用
     */
    pathStyleAccessEnabled?: number;
    /**
     * OSS的区域
     */
    region?: string;
    /**
     * OSS的用户密钥
     */
    secretKey?: string;
}

// 修改OSS
const addMessage = async () => {
    const result = await request.put('/laokou/admin/v1/oss', { headers: { noLoading: true } });
    return result.data
  }
// 新增OSS
const addOss = async () => {
    const result = await request.post('/laokou/admin/v1/oss', { headers: { noLoading: true } });
    return result.data
  }

// 上传文件
const ossUpload = async () => {
    const result = await request.post('/laokou/admin/v1/oss/upload', { headers: { noLoading: true } });
    return result.data
  }
//查询OSS列表
const ossList = async () => {
    const result = await request.post('/laokou/admin/v1/oss/list', { headers: { noLoading: true } });
    return result.data
  }
// 查看OSS
const getOssById = async (id:number) => {
    const result = await request.get('/laokou/admin/v1/oss/'+id, { headers: { noLoading: true } });
    return result.data
  }
// 删除OSS
const deleteOssById = async (id:number) => {
    const result = await request.delete('/laokou/admin/v1/oss/'+id, { headers: { noLoading: true } });
    return result.data
  }
