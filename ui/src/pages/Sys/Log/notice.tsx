import type {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {exportV3, pageV3, getByIdV3} from "@/services/admin/noticeLog";
import {Button, message} from "antd";
import {ExportOutlined} from "@ant-design/icons";
import {trim} from "@/utils/format";
import {Excel, ExportToExcel} from "@/utils/export";
import moment from "moment";
import {useRef} from "react";

export default () => {

	const statusEnum = {
		0: '0',
		1: '1'
	};

	type TableColumns = {
		id: number;
		code: string | undefined;
		name: string | undefined;
		status: string | undefined;
		param: string | undefined;
		errorMessage: string | undefined;
		createTime: string | undefined;
	};

	const actionRef = useRef();

	let noticeLogList: TableColumns[]

	let noticeLogParam: any

	const getPageQuery = (params: any) => {
		noticeLogParam = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			code: trim(params?.code),
			name: trim(params?.name),
			status: params?.status,
			errorMessage: trim(params?.errorMessage),
			params: {
				startDate: params?.startDate,
				endDate: params?.endDate
			}
		};
		return noticeLogParam;
	}

	const getStatusDesc = (status: string | undefined) => {
		if (status === "0") {
			return "成功"
		} else {
			return "失败"
		}
	}

	const exportToExcel = async () => {
		let params: Excel
		const list: TableColumns[] = [];
		// 格式化数据
		noticeLogList.forEach(item => {
			item.status = getStatusDesc(item.status)
			list.push(item)
		})
		params = {
			sheetData: list,
			sheetFilter: ["code", "name", "status", "param", "errorMessage", "createTime"],
			sheetHeader: ["标识", "名称", "状态", "参数", "错误信息", "创建时间"],
			fileName: "通知日志" + "_" + moment(new Date()).format('YYYYMMDDHHmmss'),
			sheetName: "通知日志"
		}
		ExportToExcel(params)
	}

	const exportAllToExcel = async () => {
		exportV3(noticeLogParam)
	}

	const listNoticeLog = async (params: any) => {
		noticeLogList = []
		return pageV3(getPageQuery(params)).then(res => {
			res?.data?.records?.forEach((item: TableColumns) => {
				item.status = statusEnum[item.status as '0'];
				noticeLogList.push(item);
			});
			return Promise.resolve({
				data: noticeLogList,
				total: parseInt(res.data.total),
				success: true,
			});
		})
	}

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '序号',
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 60,
		},
		{
			title: '标识',
			dataIndex: 'code',
			ellipsis: true
		},
		{
			title: '名称',
			dataIndex: 'name',
			ellipsis: true
		},
		{
			title: '状态',
			dataIndex: 'status',
			valueEnum: {
				0: {text: '成功', status: 'Success'},
				1: {text: '失败', status: 'Error'},
			},
			ellipsis: true
		},
		{
			title: '错误信息',
			dataIndex: 'errorMessage',
			ellipsis: true
		},
		{
			title: '创建日期',
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
			ellipsis: true
		},
		{
			title: '创建日期',
			dataIndex: 'createTime',
			valueType: 'dateRange',
			hideInTable: true,
			search: {
				transform: (value) => {
					return {
						startDate: value[0],
						endDate: value[1],
					};
				},
			}
		},
		{
			title: '操作',
			valueType: 'option',
			key: 'option',
			render: (_, record) => [
				<a key="getable"
				   onClick={() => {
					   const id = record?.id
					   getByIdV3({id: id})
				   }}
				>
					查看
				</a>
			],
		},
	];

	return (
		<ProTable<TableColumns>
			actionRef={actionRef}
			columns={columns}
			request={(params) => {
				// 表单搜索项会从 params 传入，传递给后端接口。
				return listNoticeLog(params)
			}}
			rowKey="id"
			pagination={{
				showQuickJumper: true,
				showSizeChanger: false,
				pageSize: 10
			}}
			search={{
				layout: 'vertical',
				defaultCollapsed: true,
			}}
			toolBarRender={
				() => [
					<Button key="export" type="primary" ghost icon={<ExportOutlined/>} onClick={exportToExcel}>
						导出
					</Button>,
					<Button key="exportAll" type="primary" icon={<ExportOutlined/>} onClick={exportAllToExcel}>
						导出全部
					</Button>
				]
			}
			dateFormatter="string"
			toolbar={{
				title: '登录日志',
				tooltip: '登录日志',
			}}
		/>
	);
};
