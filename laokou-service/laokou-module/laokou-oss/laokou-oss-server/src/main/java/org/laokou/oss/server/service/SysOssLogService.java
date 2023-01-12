package org.laokou.oss.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.oss.server.entity.SysOssLogDO;

/**
 * @author laokou
 */
public interface SysOssLogService extends IService<SysOssLogDO> {

    /**
     * 新增日志
     * @param url
     * @param md5
     * @param fileName
     * @param fileSize
     */
    void insertLog(String url,String md5,String fileName,Long fileSize);

    /**
     * 获取文件日志
     * @param md5
     * @return
     */
    SysOssLogDO getLogByMd5(String md5);

}
