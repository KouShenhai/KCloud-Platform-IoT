import service from '@/utils/request'

export const use${ClassName}Api = (id: Number) => {
	return service.get('/<#if moduleName??>${moduleName}/</#if>${classname}/' + id)
}

export const use${ClassName}SubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return service.put('/<#if moduleName??>${moduleName}/</#if>${classname}', dataForm)
	} else {
		return service.post('/<#if moduleName??>${moduleName}/</#if>${classname}', dataForm)
	}
}