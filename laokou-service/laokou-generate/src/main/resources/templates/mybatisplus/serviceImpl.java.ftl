package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import ${package.Entity?replace("entity","vo")}.*;
import ${package.Entity?replace("entity","qo")}.*;
import ${package.Entity?replace("entity","dto")}.*;
import ${package.Entity?replace("entity","excel")}.*;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.easy.excel.suppert.ExcelTemplate;
/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
@RequiredArgsConstructor
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

   private final ExcelTemplate<${entity?substring(0,entity?length-2)}Qo,${entity?substring(0,entity?length-2)}VO> excelTemplate;

   public IPage<${entity?substring(0,entity?length-2)}VO> queryPageList(${entity?substring(0,entity?length-2)}Qo qo) {
       IPage<${entity?substring(0,entity?length-2)}VO> pageQuery = new Page<>(qo.getPageNum(),qo.getPageSize());

       return this.baseMapper.queryPageList(pageQuery,qo);
   }

   @Transactional(rollbackFor = Exception.class)
   public Boolean save${entity?substring(0,entity?length-2)}(${entity?substring(0,entity?length-2)}DTO dto) {
       ValidatorUtil.validateEntity(dto);

       ${entity?substring(0,entity?length-2)}DO sysDO = ConvertUtil.sourceToTarget(dto, ${entity?substring(0,entity?length-2)}DO.class);
       return this.saveOrUpdate(sysDO);
   }

   public void exportData(${entity?substring(0,entity?length-2)}Qo qo, HttpServletResponse response){
       excelTemplate.export(500,response,qo,this, ${entity?substring(0,entity?length-2)}Excel.class);
   }

   public void resultList(${entity?substring(0,entity?length-2)}Qo qo, ResultHandler<${entity?substring(0,entity?length-2)}VO> resultHandler) {
       this.baseMapper.resultList(qo,resultHandler);
   }
}
</#if>
