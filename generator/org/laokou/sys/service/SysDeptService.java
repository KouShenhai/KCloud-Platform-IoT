package org.laokou.sys.service;

import org.laokou.sys.entity.SysDeptDO;
import com.baomidou.mybatisplus.extension.service.IService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.sys.vo.*;
import org.laokou.sys.qo.*;
import org.laokou.sys.dto.*;

import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.easy.excel.service.ResultService;
/**
 * <p>
 * 部门 服务类
 * </p>
 *
 * @author laokou
 * @since 2024-02-01
 */
public interface SysDeptService extends IService<SysDeptDO>, ResultService<SysDeptQo,SysDeptVO> {

     IPage<SysDeptVO> queryPageList(SysDeptQo qo);

     Boolean saveSysDept(SysDeptDTO dto);

     void exportData(SysDeptQo qo, HttpServletResponse response);
}
