// @ts-ignore
import ExportJsonExcel from 'js-export-excel';

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
