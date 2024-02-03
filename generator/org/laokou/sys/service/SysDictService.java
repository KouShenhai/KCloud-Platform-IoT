package org.laokou.sys.service;

import org.laokou.sys.entity.SysDictDO;
import com.baomidou.mybatisplus.extension.service.IService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.sys.vo.*;
import org.laokou.sys.qo.*;
import org.laokou.sys.dto.*;

import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.easy.excel.service.ResultService;
/**
 * <p>
 * 字典 服务类
 * </p>
 *
 * @author laokou
 * @since 2024-02-01
 */
public interface SysDictService extends IService<SysDictDO>, ResultService<SysDictQo,SysDictVO> {

     IPage<SysDictVO> queryPageList(SysDictQo qo);

     Boolean saveSysDict(SysDictDTO dto);

     void exportData(SysDictQo qo, HttpServletResponse response);
}
