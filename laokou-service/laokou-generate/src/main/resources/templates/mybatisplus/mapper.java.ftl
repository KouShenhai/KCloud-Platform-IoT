package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
<#if mapperAnnotationClass??>
import ${mapperAnnotationClass.name};
</#if>
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import ${package.Entity?replace("entity","vo")}.*;
import ${package.Entity?replace("entity","qo")}.*;
import org.apache.ibatis.session.ResultHandler;
/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Repository
<#if mapperAnnotationClass??>
@${mapperAnnotationClass.simpleName}
</#if>
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {
     /**
     * 分页查询${table.comment!}
     * @param pageQuery
     * @param qo
     * @return
     */
     IPage<${entity?substring(0,entity?length-2)}VO> queryPageList(IPage<${entity?substring(0,entity?length-2)}VO> pageQuery, @Param("qo") ${entity?substring(0,entity?length-2)}Qo qo);

     /**
     * 查询导出${table.comment!}
     * @param qo
     * @param handler
     */
     void resultList(@Param("qo") ${entity?substring(0,entity?length-2)}Qo qo, ResultHandler<${entity?substring(0,entity?length-2)}VO> handler);
}
</#if>
