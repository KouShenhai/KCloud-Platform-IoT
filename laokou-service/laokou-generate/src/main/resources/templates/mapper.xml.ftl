<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="${cacheClassName}"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>

</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
<#list table.commonFields as field>
        ${field.columnName},
</#list>
        ${table.fieldNames}
    </sql>

</#if>

    <select id="queryPageList" resultType="${package.Entity?replace("entity","vo")}.${entity?substring(0,entity?length-2)}VO">
        SELECT
        id,
        ${table.fieldNames}
        from ${table.name}
        where del_flag = 0
        <#list table.fields as field>
            <if test="qo.${field.propertyName} != null and qo.${field.propertyName} != ''">
                <#if field.propertyType == "boolean">
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyType == "int">
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyType == "Integer">
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyName?lower_case?index_of("type") gte 0 >
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyName?lower_case?index_of("status") gte 0 >
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyName?lower_case?index_of("code") gte 0 >
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyName?lower_case?index_of("value") gte 0 >
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyName?lower_case?index_of("id") gte 0 >
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#else>
                    and `${field.columnName}` like concat('%',${r"#{qo."}${field.propertyName}${r"}"},'%')
                </#if>
            </if>
        </#list>
        <#if table.fieldNames?index_of("sort") gte 0>
            order by sort asc
        </#if>
    </select>

    <select id="resultList" fetchSize="1000" resultSetType="FORWARD_ONLY" resultType="${package.Entity?replace("entity","vo")}.${entity?substring(0,entity?length-2)}VO">
        SELECT

        ${table.fieldNames}
        from ${table.name}
        where del_flag = 0
        <#list table.fields as field>
            <if test="qo.${field.propertyName} != null and qo.${field.propertyName} != ''">
                <#if field.propertyType == "boolean">
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyType == "int">
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyType == "Integer">
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyName?lower_case?index_of("type") gte 0 >
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyName?lower_case?index_of("status") gte 0 >
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyName?lower_case?index_of("code") gte 0 >
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyName?lower_case?index_of("value") gte 0 >
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#elseif field.propertyName?lower_case?index_of("id") gte 0 >
                    and `${field.columnName}` = ${r"#{qo."}${field.propertyName}${r"}"}
                <#else>
                    and `${field.columnName}` like concat('%',${r"#{qo."}${field.propertyName}${r"}"},'%')
                </#if>
            </if>
        </#list>
    </select>
</mapper>
