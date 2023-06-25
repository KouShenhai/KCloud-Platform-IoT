package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${package.Entity?replace("entity","vo")}.*;
import ${package.Entity?replace("entity","qo")}.*;
import ${package.Entity?replace("entity","dto")}.*;

import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.easy.excel.service.ResultService;
/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}>, ResultService<${entity?substring(0,entity?length-2)}Qo,${entity?substring(0,entity?length-2)}VO> {

     IPage<${entity?substring(0,entity?length-2)}VO> queryPageList(${entity?substring(0,entity?length-2)}Qo qo);

     Boolean save${entity?substring(0,entity?length-2)}(${entity?substring(0,entity?length-2)}DTO dto);

     void exportData(${entity?substring(0,entity?length-2)}Qo qo, HttpServletResponse response);
}
</#if>
