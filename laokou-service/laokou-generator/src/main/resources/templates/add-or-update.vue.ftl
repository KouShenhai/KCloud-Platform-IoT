<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="100px" @keyup.enter="submitHandle()">
	    <#list columnList as column>
		<#if column.form>
			<#if column.formType == 'text'>
				<el-form-item label="${column.columnComment!}" prop="${column.attrName}">
					<el-input v-model="dataForm.${column.attrName}" placeholder="${column.columnComment!}"></el-input>
				</el-form-item>
			<#elseif column.formType == 'textarea'>
				<el-form-item label="${column.columnComment!}" prop="${column.attrName}">
					<el-input type="textarea" v-model="dataForm.${column.attrName}"></el-input>
				</el-form-item>
			<#elseif column.formType == 'editor'>
				<el-form-item label="${column.columnComment!}" prop="${column.attrName}">
					<el-input type="textarea" v-model="dataForm.${column.attrName}"></el-input>
				</el-form-item>
			<#elseif column.formType == 'select'>
				<#if column.dictName??>
					<el-form-item label="${column.columnComment!}" prop="${column.attrName}">
						<fast-select v-model="dataForm.${column.attrName}" dict-type="${column.dictName}" placeholder="${column.columnComment!}"></fast-select>
					</el-form-item>
				<#else>
					<el-form-item label="${column.columnComment!}" prop="${column.attrName}">
						<el-select v-model="dataForm.${column.attrName}" placeholder="请选择">
							<el-option label="人人" value="0"></el-option>
						</el-select>
					</el-form-item>
				</#if>
			<#elseif column.formType == 'radio'>
				<#if column.dictName??>
					<el-form-item label="${column.columnComment!}" prop="${column.attrName}">
						<fast-radio-group v-model="dataForm.${column.attrName}" dict-type="${column.dictName}"></fast-radio-group>
					</el-form-item>
				<#else>
					<el-form-item label="${column.columnComment!}" prop="${column.attrName}">
						<el-radio-group v-model="dataForm.${column.attrName}">
							<el-radio :label="0">启用</el-radio>
							<el-radio :label="1">禁用</el-radio>
						</el-radio-group>
					</el-form-item>
				</#if>
			<#elseif column.formType == 'checkbox'>
				<el-form-item label="${column.columnComment!}" prop="${column.attrName}">
					<el-checkbox-group v-model="dataForm.${column.attrName}">
						<el-checkbox label="启用" name="type"></el-checkbox>
						<el-checkbox label="禁用" name="type"></el-checkbox>
					</el-checkbox-group>
				</el-form-item>
			<#elseif column.formType == 'date'>
				<el-form-item label="${column.columnComment!}" prop="${column.attrName}">
					<el-date-picker type="date" placeholder="${column.columnComment!}" v-model="dataForm.${column.attrName}"></el-date-picker>
				</el-form-item>
			<#elseif column.formType == 'datetime'>
				<el-form-item label="${column.columnComment!}" prop="${column.attrName}">
					<el-date-picker type="datetime" placeholder="${column.columnComment!}" v-model="dataForm.${column.attrName}"></el-date-picker>
				</el-form-item>
			<#else>
				<el-form-item label="${column.columnComment!}" prop="${column.attrName}">
					<el-input v-model="dataForm.${column.attrName}" placeholder="${column.columnComment!}"></el-input>
				</el-form-item>
			</#if>
		</#if>
	    </#list>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus/es'
import { use${ClassName}Api, use${ClassName}SubmitApi } from '@/api/<#if moduleName??>${moduleName}/</#if>${classname}'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()

const dataForm = reactive({
	<#list columnList as column>
	${column.attrName}: '',
	</#list>
})

const init = (id?: number) => {
	visible.value = true
	dataForm.id = ''

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	if (id) {
		get${ClassName}(id)
	}
}

const get${ClassName} = (id: number) => {
	use${ClassName}Api(id).then(res => {
		Object.assign(dataForm, res.data)
	})
}

const dataRules = ref({
	<#list columnList as column>
	<#if column.form && column.required>
	${column.attrName}: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	</#if>
	</#list>
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		use${ClassName}SubmitApi(dataForm).then(() => {
			ElMessage.success({
				message: '操作成功',
				duration: 500,
				onClose: () => {
					visible.value = false
					emit('refreshDataList')
				}
			})
		})
	})
}

defineExpose({
	init
})
</script>
