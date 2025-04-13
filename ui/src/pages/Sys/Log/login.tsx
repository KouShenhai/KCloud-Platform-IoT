import type {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {exportV3, pageV3} from "@/services/admin/loginLog";
import {Button} from "antd";
import {ExportOutlined} from "@ant-design/icons";
import {trim} from "@/utils/format";
import {ExportToExcel} from "@/utils/export";
import moment from "moment";
import {useRef, useState} from "react";
import {useAccess} from "@@/exports";

export default () => {

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

	const access = useAccess();
	const actionRef = useRef();
	const [list, setList] = useState<TableColumns[]>([]);
	const [param, setParam] = useState<any>({});

	const getLoginType = (type: string) => {
		return {
			'username_password': '用户名密码登录',
			'mobile': '手机号登录',
			'mail': '邮箱登录',
			'authorization_code': '授权码登录',
		}[type]
	}

	const getLoginStatus = (status: string) => {
		return {
			'0': '登录成功',
			'1': '登录失败',
		}[status]
	}

	const getPageQueryParam = (params: any) => {
		const param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			username: trim(params?.username),
			ip: trim(params?.ip),
			address: trim(params?.address),
			browser: trim(params?.browser),
			status: params?.statusValue,
			os: trim(params?.os),
			type: params?.typeValue,
			errorMessage: trim(params?.errorMessage),
			params: {
				startTime: params?.startDate ? `${params.startDate} 00:00:00` : undefined,
				endTime: params?.endDate ? `${params.endDate} 23:59:59` : undefined
			}
		}
		setParam(param)
		return param
	}

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '用户名',
			dataIndex: 'username',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入用户名',
			}
		},
		{
			title: 'IP地址',
			dataIndex: 'ip',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入IP地址',
			}
		},
		{
			title: '归属地',
			dataIndex: 'address',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入归属地',
			}
		},
		{
			title: '浏览器',
			dataIndex: 'browser',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入浏览器',
			}
		},
		{
			title: '操作系统',
			dataIndex: 'os',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入操作系统',
			}
		},
		{
			title: '登录状态',
			key: 'statusValue',
			dataIndex: 'statusValue',
			valueType: 'select',
			hideInTable: true,
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				options: [

					{
						value: '0',
						label: '登录成功',
					},
					{
						value: '1',
						label: '登录失败',
					},
				],
				placeholder: '请选择登录状态',
			}
		},
		{
			title: '登录状态',
			dataIndex: 'status',
			hideInSearch: true,
			valueEnum: {
				'0': { text: '登录成功', status: 'Success'},
				'1': { text: '登录失败', status: 'Error'}
			},
			ellipsis: true
		},
		{
			title: '错误信息',
			dataIndex: 'errorMessage',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入错误信息',
			}
		},
		{
			title: '登录类型',
			key: 'typeValue',
			dataIndex: 'typeValue',
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				options: [
					{
							value: "authorization_code",
							label: "授权码登录",
					},
					{
							value: "mail",
							label: "邮箱登录",
					},
					{
							value: "mobile",
							label: "手机号登录",
					},
					{
							value: "username_password",
							label: "用户名密码登录",
					},
				],
				placeholder: '请选择登录类型',
			},
		},
		{
			title: '登录类型',
			dataIndex: 'type',
			hideInSearch: true,
			valueEnum: {
				'authorization_code':{ text: '授权码登录', status: 'Error'},
				'mail': { text: '邮箱登录', status: 'Success'},
				'mobile': { text: '手机号登录', status: 'Default' },
				'username_password': { text: '用户名密码登录', status: 'Processing' }
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
			dataIndex: 'createTimeValue',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: ['请选择开始日期', '请选择结束日期'],
			},
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

	return (
		<ProTable<TableColumns>
			actionRef={actionRef}
			columns={columns}
			request={async (params) => {
				// 表单搜索项会从 params 传入，传递给后端接口。
				const list: TableColumns[] 	= []
				return pageV3(getPageQueryParam(params)).then(res => {
					res?.data?.records?.forEach((item: TableColumns) => {
						item.status = item.status as '0';
						item.type = item.type as '0';
						list.push(item);
					});
					setList(list)
					return Promise.resolve({
						data: list,
						total: parseInt(res.data.total),
						success: true,
					});
				})
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
					<Button key="export" type="primary" ghost icon={<ExportOutlined/>} onClick={() => {
						const _list: TableColumns[] = [];
						// 格式化数据
						list.forEach(item => {
							item.status = getLoginStatus(item.status as string)
							item.type = getLoginType(item.type as string)
							_list.push(item)
						})
						ExportToExcel({
							sheetData: _list,
							sheetFilter: ["username", "ip", "address", "browser", "os", "status", "errorMessage", "type", "createTime"],
							sheetHeader: ["用户名", "IP地址", "归属地", "浏览器", "操作系统", "登录状态", "错误信息", "登录类型", "登录时间"],
							fileName: "登录日志_导出_" + moment(new Date()).format('YYYYMMDDHHmmss'),
							sheetName: "登录日志"
						})
					}}>
						导出
					</Button>,
					( access.canLoginLogExport && <Button key="exportAll" type="primary" icon={<ExportOutlined/>} onClick={() => {
						exportV3(param)
					}}>
						导出全部
					</Button>)
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
