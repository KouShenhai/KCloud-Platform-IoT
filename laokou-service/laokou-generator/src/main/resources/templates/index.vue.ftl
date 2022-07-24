<template>
	<el-card>
		<el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
		<#list columnList as column>
		  <#if column.query>
			<el-form-item>
			<#if column.formType == 'text' || column.formType == 'textarea' || column.formType == 'editor'>
			  <el-input v-model="state.queryForm.${column.attrName}" placeholder="${column.columnComment!}" clearable></el-input>
			<#elseif column.formType == 'select'>
			  <#if column.dictName??>
			  <fast-select v-model="state.queryForm.${column.attrName}" dict-type="${column.dictName}" placeholder="${column.columnComment!}"></fast-select>
			  <#else>
			  <el-select v-model="state.queryForm.${column.attrName}" placeholder="${column.columnComment!}">
				<el-option label="选择" value="0"></el-option>
			  </el-select>
			  </#if>
			<#elseif column.formType == 'radio'>
			  <#if column.dictName??>
			  <fast-radio-group v-model="state.queryForm.${column.attrName}" dict-type="${column.dictName}"></fast-radio-group>
			  <#else>
			  <el-radio-group v-model="state.queryForm.${column.attrName}">
				<el-radio :label="0">单选</el-radio>
			  </el-radio-group>
			  </#if>
			<#elseif column.formType == 'date'>
			  <el-date-picker
				v-model="daterange"
				type="daterange"
				value-format="yyyy-MM-dd">
			  </el-date-picker>
			<#elseif column.formType == 'datetime'>
			  <el-date-picker
				v-model="datetimerange"
				type="datetimerange"
				value-format="yyyy-MM-dd HH:mm:ss">
			  </el-date-picker>
			<#else>
			  <el-input v-model="state.queryForm.${column.attrName}" placeholder="${column.columnComment!}" clearable></el-input>
			</#if>
			</el-form-item>
		  </#if>
		  </#list>
			<el-form-item>
				<el-button @click="getDataList()">查询</el-button>
			</el-form-item>
			<el-form-item>
				<el-button v-auth="'<#if moduleName??>${moduleName}:</#if>${classname}:save'" type="primary" @click="addOrUpdateHandle()">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-button v-auth="'<#if moduleName??>${moduleName}:</#if>${classname}:delete'" type="danger" @click="deleteBatchHandle()">删除</el-button>
			</el-form-item>
		</el-form>
		<el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%" @selection-change="selectionChangeHandle">
			<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
	    <#list columnList as column>
	    <#if column.list>
		  <#if column.dictName??>
			<fast-table-column prop="${column.attrName}" label="${column.columnComment!}" dict-type="${column.dictName}"></fast-table-column>
		  <#else>
			<el-table-column prop="${column.attrName}" label="${column.columnComment!}" header-align="center" align="center"></el-table-column>
		  </#if>
	    </#if>
        </#list>
			<el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
				<template #default="scope">
					<el-button v-auth="'<#if moduleName??>${moduleName}:</#if>${classname}:update'" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
					<el-button v-auth="'<#if moduleName??>${moduleName}:</#if>${classname}:delete'" type="text" size="small" @click="deleteBatchHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination
			:current-page="state.page"
			:page-sizes="state.pageSizes"
			:page-size="state.limit"
			:total="state.total"
			layout="total, sizes, prev, pager, next, jumper"
			@size-change="sizeChangeHandle"
			@current-change="currentChangeHandle"
		>
		</el-pagination>

		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update ref="addOrUpdateRef" @refreshDataList="getDataList"></add-or-update>
	</el-card>
</template>

<script setup lang="ts" name="<#if moduleName??>${moduleName}</#if>${ClassName}Index">
import { useCrud } from '@/hooks'
import { reactive, ref } from 'vue'
import AddOrUpdate from './add-or-update.vue.ftl'
import { IHooksOptions } from '@/hooks/interface'

const state: IHooksOptions = reactive({
	dataListUrl: '/<#if moduleName??>${moduleName}/</#if>${classname}/page',
	deleteUrl: '/<#if moduleName??>${moduleName}/</#if>${classname}',
	queryForm: {
		<#list columnList as column>
		<#if column.query>
		<#if column.formType == 'date'>
		startDate: '',
		endDate: '',
		<#elseif column.formType == 'datetime'>
		startDateTime: '',
		endDateTime: '',
		<#else>
		${column.attrName}: '',
		</#if>
		</#if>
		</#list>
	}
})

const addOrUpdateRef = ref()
const addOrUpdateHandle = (id?: number) => {
	addOrUpdateRef.value.init(id)
}

const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state)
</script>
