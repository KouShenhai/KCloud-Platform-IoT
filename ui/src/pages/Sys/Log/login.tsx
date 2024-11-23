import type {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {exportV3, pageV3, removeV3} from "@/services/admin/loginLog";
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

	const typeEnum = {
		'password': 'password',
		'mobile': 'mobile',
		'mail': 'mail',
		'authorization_code': 'authorization_code'
	};

	type TableColumns = {
		id: number | undefined;
		username: string | undefined;
		ip: string | undefined;
		address: string | undefined;
		browser: string | undefined;
		os: string | undefined;
		status: string | undefined;
		errorMessage: string | undefined;
		type: string | undefined;
		createTime: string | undefined;
	};

	const actionRef = useRef();

	const [selectedRowKeys, setSelectedRowKeys] = useState([]);

	let loginLogList: TableColumns[]

	let loginLogParam: any

	const getPageQuery = (params: any) => {
		loginLogParam = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			username: trim(params?.username),
			ip: trim(params?.ip),
			address: trim(params?.address),
			browser: trim(params?.browser),
			status: params?.status,
			os: trim(params?.os),
			type: params?.type,
			errorMessage: trim(params?.errorMessage),
			params: {
				startDate: params?.startDate,
				endDate: params?.endDate
			}
		};
		return loginLogParam;
	}

	const getStatusDesc = (status: string | undefined) => {
		if (status === "0") {
			return "登录成功"
		} else {
			return "登录失败"
		}
	}

	const getTypeDesc = (type: string | undefined) => {
		if (type === "password") {
			return "用户名密码登录";
		} else if (type === "mail") {
			return "邮箱登录"
		} else if (type === "mobile") {
			return "手机号登录";
		} else if (type === "authorization_code") {
			return "授权码登录";
		}
	}

	const exportToExcel = async () => {
		let params: Excel
		const list: TableColumns[] = [];
		// 格式化数据
		loginLogList.forEach(item => {
			item.status = getStatusDesc(item.status)
			item.type = getTypeDesc(item.type)
			list.push(item)
		})
		params = {
			sheetData: list,
			sheetFilter: ["username", "ip", "address", "browser", "os", "status", "errorMessage", "type", "createTime"],
			sheetHeader: ["用户名", "IP地址", "归属地", "浏览器", "操作系统", "登录状态", "错误信息", "登录类型", "登录日期"],
			fileName: "登录日志" + "_" + moment(new Date()).format('YYYYMMDDHHmmss'),
			sheetName: "登录日志"
		}
		ExportToExcel(params)
	}

	const exportAllToExcel = async () => {
		exportV3(loginLogParam)
	}

	const listLoginLog = async (params: any) => {
		loginLogList = []
		return pageV3(getPageQuery(params)).then(res => {
			res?.data?.records?.forEach((item: TableColumns) => {
				item.status = statusEnum[item.status as '0'];
				item.type = typeEnum[item.type as 'password'];
				loginLogList.push(item);
			});
			return Promise.resolve({
				data: loginLogList,
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

	const deleteLoginLog = async () => {
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
			title: '用户名',
			dataIndex: 'username',
			ellipsis: true
		},
		{
			title: 'IP地址',
			dataIndex: 'ip',
			ellipsis: true
		},
		{
			title: '归属地',
			dataIndex: 'address',
			ellipsis: true
		},
		{
			title: '浏览器',
			dataIndex: 'browser',
			ellipsis: true
		},
		{
			title: '操作系统',
			dataIndex: 'os',
			ellipsis: true
		},
		{
			title: '登录状态',
			dataIndex: 'status',
			valueEnum: {
				0: {text: '登录成功', status: 'Success'},
				1: {text: '登录失败', status: 'Error'},
			},
			ellipsis: true
		},
		{
			title: '错误信息',
			dataIndex: 'errorMessage',
			ellipsis: true
		},
		{
			title: '登录类型',
			dataIndex: 'type',
			valueEnum: {
				password: {text: '用户名密码登录', status: 'Processing'},
				mobile: {text: '手机号登录', status: 'Default'},
				mail: {text: '邮箱登录', status: 'Success'},
				authorization_code: {text: '授权码登录', status: 'Error'}
			},
			width: 160,
			ellipsis: true
		},
		{
			title: '登录日期',
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
			ellipsis: true
		},
		{
			title: '登录日期',
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
				<a key="editable"
					onClick={() => {
						Modal.confirm({
							title: '确认删除?',
							content: '您确定要删除吗?',
							okText: '确认',
							cancelText: '取消',
							onOk: async () => {
								// @ts-ignore
								removeV3([record.id]).then(() => {
									message.success("删除成功").then()
									// @ts-ignore
									actionRef?.current?.reload();
								})
							},
						});
					}}
				>
					删除
				</a>
			],
		},
	];

	// @ts-ignore
	// @ts-ignore
	return (
		<ProTable<TableColumns>
			actionRef={actionRef}
			columns={columns}
			request={(params) => {
				// 表单搜索项会从 params 传入，传递给后端接口。
				return listLoginLog(params)
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
					<Button key="delete" danger ghost icon={<DeleteOutlined/>} onClick={deleteLoginLog}>
						删除
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
				title: '登录日志',
				tooltip: '登录日志',
			}}
		/>
	);
};
