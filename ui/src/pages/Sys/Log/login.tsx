import type {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {exportV3, pageV3} from "@/services/admin/loginLog";
import {Button} from "antd";
import {ExportOutlined} from "@ant-design/icons";
import {trim} from "@/utils/format";
import {ExportToExcel} from "@/utils/export";
import moment from "moment";
import {useRef, useState} from "react";
import {getLoginStatus, getLoginType, LOGIN_STATUS, LOGIN_TYPE} from "@/services/constant";
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

	const getPageQuery = (params: any) => {
		const param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			username: trim(params?.username),
			ip: trim(params?.ip),
			address: trim(params?.address),
			browser: trim(params?.browser),
			status: params?.status,
			os: trim(params?.os),
			type: params?.type,
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
				[LOGIN_STATUS.OK]: getLoginStatus(LOGIN_STATUS.OK),
				[LOGIN_STATUS.FAIL]: getLoginStatus(LOGIN_STATUS.FAIL)
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
				[LOGIN_TYPE.AUTHORIZATION_CODE]: getLoginType(LOGIN_TYPE.AUTHORIZATION_CODE),
				[LOGIN_TYPE.MAIL]: getLoginType(LOGIN_TYPE.MAIL),
				[LOGIN_TYPE.MOBILE]: getLoginType(LOGIN_TYPE.MOBILE),
				[LOGIN_TYPE.USERNAME_PASSWORD]: getLoginType(LOGIN_TYPE.USERNAME_PASSWORD)
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
		}
	];

	return (
		<ProTable<TableColumns>
			actionRef={actionRef}
			columns={columns}
			request={async (params) => {
				// 表单搜索项会从 params 传入，传递给后端接口。
				const list: TableColumns[] 	= []
				return pageV3(getPageQuery(params)).then(res => {
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
							item.status = getLoginStatus(item.status as '0').text
							item.type = getLoginType(item.type as '0')?.text
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
