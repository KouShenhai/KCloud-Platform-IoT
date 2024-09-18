import type {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {exportV3, pageV3, removeV3} from "@/services/admin/apiLog";
import {Button, message, Modal} from "antd";
import {DeleteOutlined, ExportOutlined} from "@ant-design/icons";
import {trim} from "@/utils/format";
import {Excel, ExportToExcel} from "@/utils/export";
import moment from "moment";
import {useRef, useState} from "react";

export default () => {

	const statusEnum = {
		0: '0',
		1: '1'
	};

	type TableColumns = {
		id: number | undefined;
		code: string | undefined;
		name: string | undefined;
		param: string | undefined;
		address: string | undefined;
		status: string | undefined;
		errorMessage: string | undefined;
		createTime: string | undefined;
	};

	const actionRef = useRef();

	const [selectedRowKeys, setSelectedRowKeys] = useState([]);

	let apiLogList: TableColumns[]

	let apiLogParam: any

	const getPageQuery = (params: any) => {
		apiLogParam = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			name: trim(params?.name),
			code: trim(params?.code),
			status: params?.status,
			errorMessage: trim(params?.errorMessage),
			params: {
				startDate: params?.startDate,
				endDate: params?.endDate
			}
		};
		return apiLogParam;
	}

	const getStatusDesc = (status: string | undefined) => {
		if (status === "0") {
			return "请求成功"
		} else {
			return "请求失败"
		}
	}

	const exportToExcel = async () => {
		let params: Excel
		const list: TableColumns[] = [];
		// 格式化数据
		apiLogList.forEach(item => {
			item.status = getStatusDesc(item.status)
			list.push(item)
		})
		params = {
			sheetData: list,
			sheetFilter: ["username", "ip", "address", "browser", "os", "status", "errorMessage", "type", "createTime"],
			sheetHeader: ["用户名", "IP地址", "归属地", "浏览器", "操作系统", "登录状态", "错误信息", "登录类型", "登录日期"],
			fileName: "Api日志" + "_" + moment(new Date()).format('YYYYMMDDHHmmss'),
			sheetName: "Api日志"
		}
		ExportToExcel(params)
	}

	const exportAllToExcel = async () => {
		exportV3(apiLogParam)
	}

	const listApiLog = async (params: any) => {
		apiLogList = []
		return pageV3(getPageQuery(params)).then(res => {
			res?.data?.records?.forEach((item: TableColumns) => {
				item.status = statusEnum[item.status as '0'];
				apiLogList.push(item);
			});
			return Promise.resolve({
				data: apiLogList,
				total: parseInt(res.data.total),
				success: true,
			});
		})
	}

	const rowSelection = {
		onChange: (selectedRowKeys: any) => {
			setSelectedRowKeys(selectedRowKeys);
		}
	};

	const truncateApiLog = async () => {
		Modal.confirm({
			title: '确认清空?',
			content: '您确定要清空数据吗?',
			okText: '确认',
			cancelText: '取消',
			onOk: async () => {
				// truncateV3().then(() => {
				// 	message.success("数据已清空").then()
				// 	// @ts-ignore
				// 	actionRef?.current?.reload();
				// })
			},
		});
	}

	const deleteApiLog = async () => {
		Modal.confirm({
			title: '确认删除?',
			content: '您确定要删除吗?',
			okText: '确认',
			cancelText: '取消',
			onOk: async () => {
				removeV3(selectedRowKeys).then(() => {
					message.success("删除成功").then()
					// @ts-ignore
					actionRef?.current?.reload();
				})
				setSelectedRowKeys([])
			},
		});
	}

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '序号',
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 60,
		},
		{
			title: 'Api编码',
			dataIndex: 'code'
		},
		{
			title: 'Api名称',
			dataIndex: 'name'
		},
		{
			title: 'Api状态',
			dataIndex: 'status',
			valueEnum: {
				0: {text: '请求成功', status: 'Success'},
				1: {text: '请求失败', status: 'Error'},
			},
		},
		{
			title: '错误信息',
			dataIndex: 'errorMessage'
		},
		{
			title: '创建时间',
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
		},
		{
			title: '创建时间',
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
		}
	];

	// @ts-ignore
	return (
		<ProTable<TableColumns>
			actionRef={actionRef}
			columns={columns}
			request={(params) => {
				// 表单搜索项会从 params 传入，传递给后端接口。
				return listApiLog(params)
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
					<Button key="delete" danger ghost icon={<DeleteOutlined/>} onClick={deleteApiLog}>
						删除
					</Button>,
					<Button key="truncate" type="primary" danger icon={<DeleteOutlined/>} onClick={truncateApiLog}>
						清空
					</Button>,
					<Button key="export" type="primary" ghost icon={<ExportOutlined/>} onClick={exportToExcel}>
						导出
					</Button>,
					<Button key="exportAll" type="primary" icon={<ExportOutlined/>} onClick={exportAllToExcel}>
						导出全部
					</Button>
				]
			}
			rowSelection={
				// 自定义选择项参考: https://ant.design/components/table-cn/#components-table-demo-row-selection-custom
				rowSelection
			}
			dateFormatter="string"
			toolbar={{
				title: 'Api日志',
				tooltip: 'Api日志',
			}}
		/>
	);
};
