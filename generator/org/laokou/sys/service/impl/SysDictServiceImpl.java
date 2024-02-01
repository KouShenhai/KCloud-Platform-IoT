package org.laokou.sys.service.impl;

import org.laokou.sys.entity.SysDictDO;
import org.laokou.sys.mapper.SysDictMapper;
import org.laokou.sys.service.SysDictService;
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
 * 字典 服务实现类
 * </p>
 *
 * @author laokou
 * @since 2024-02-01
 */
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDictDO> implements SysDictService {

   private final ExcelTemplate<SysDictQo,SysDictVO> excelTemplate;

   public IPage<SysDictVO> queryPageList(SysDictQo qo) {
       IPage<SysDictVO> pageQuery = new Page<>(qo.getPageNum(),qo.getPageSize());

       return this.baseMapper.queryPageList(pageQuery,qo);
   }

   @Transactional(rollbackFor = Exception.class)
   public Boolean saveSysDict(SysDictDTO dto) {
       ValidatorUtil.validateEntity(dto);

       SysDictDO sysDO = ConvertUtil.sourceToTarget(dto, SysDictDO.class);
       return this.saveOrUpdate(sysDO);
   }

   public void exportData(SysDictQo qo, HttpServletResponse response){
       excelTemplate.export(500,response,qo,this, SysDictExcel.class);
   }

   public void resultList(SysDictQo qo, ResultHandler<SysDictVO> resultHandler) {
       this.baseMapper.resultList(qo,resultHandler);
   }
}
