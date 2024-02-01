package org.laokou.sys.service.impl;

import org.laokou.sys.entity.SysDeptDO;
import org.laokou.sys.mapper.SysDeptMapper;
import org.laokou.sys.service.SysDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.sys.vo.*;
import org.laokou.sys.qo.*;
import org.laokou.sys.dto.*;
import org.laokou.sys.excel.*;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.easy.excel.suppert.ExcelTemplate;
/**
 * <p>
 * 部门 服务实现类
 * </p>
 *
 * @author laokou
 * @since 2024-02-01
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDeptDO> implements SysDeptService {

   private final ExcelTemplate<SysDeptQo,SysDeptVO> excelTemplate;

   public IPage<SysDeptVO> queryPageList(SysDeptQo qo) {
       IPage<SysDeptVO> pageQuery = new Page<>(qo.getPageNum(),qo.getPageSize());

       return this.baseMapper.queryPageList(pageQuery,qo);
   }

   @Transactional(rollbackFor = Exception.class)
   public Boolean saveSysDept(SysDeptDTO dto) {
       ValidatorUtil.validateEntity(dto);

       SysDeptDO sysDO = ConvertUtil.sourceToTarget(dto, SysDeptDO.class);
       return this.saveOrUpdate(sysDO);
   }

   public void exportData(SysDeptQo qo, HttpServletResponse response){
       excelTemplate.export(500,response,qo,this, SysDeptExcel.class);
   }

   public void resultList(SysDeptQo qo, ResultHandler<SysDeptVO> resultHandler) {
       this.baseMapper.resultList(qo,resultHandler);
   }
}
