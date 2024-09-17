// @ts-ignore
import ExportJsonExcel from 'js-export-excel';
import {request} from "@umijs/max";
import {message} from "antd";

export type Excel = {
	fileName: string;
	sheetData: any[];
	sheetFilter: string[];
	sheetHeader: string[];
	sheetName: string;
}

export function ExportToExcel(params: Excel) {
	const option: any = {};
	option.fileName = params?.fileName; //导出的Excel文件名
	option.datas = [
		{
			sheetData: params?.sheetData,
			sheetName: params?.sheetName,
			sheetFilter: params?.sheetFilter,
			sheetHeader: params?.sheetHeader
		}
	];
	const toExcel = new ExportJsonExcel(option);
	toExcel.saveExcel();
}

export function ExportAllToExcel(fileName: string, url: string, method: string, body: any, options?: {
	[key: string]: any
}) {
	request<any>(url, {
		method: method,
		responseType: 'blob',
		headers: {
			'Content-Type': 'application/json',
		},
		data: body,
		...(options || {}),
	}).then((res: { data: BlobPart; }) => {
		const blob = new Blob([res.data])
		//将blob格式的响应数据转换为原本的格式，方便判断接口是否返回报错信息
		let reader = new FileReader()
		reader.readAsText(blob)
		reader.onload = () => {
			// @ts-ignore
			const link = document.createElement('a')
			link.download = fileName
			link.style.display = 'none'
			link.href = URL.createObjectURL(blob)
			document.body.appendChild(link)
			link.click()
			// @ts-ignore
			URL.revokeObjectURL(link)
			document.body.removeChild(link)
			message.success('导出成功').then()
		}
	});
}
