import {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {exportV3, pageV3, getByIdV3} from "@/services/admin/operateLog";
import {Button} from "antd";
import {ExportOutlined} from "@ant-design/icons";
import {trim} from "@/utils/format";
import {ExportToExcel} from "@/utils/export";
import moment from "moment";
import {useRef, useState} from "react";
import {getStatus, STATUS} from "@/services/constant";
import {OperateLogDrawer} from "@/pages/Sys/Log/OperateDrawer";
import {useAccess} from "@@/exports";

export default () => {

	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<any>({})

	const statusEnum = {
		0: '0',
		1: '1'
	};

	type TableColumns = {
		id: number;
		name: string | undefined;
		status: string | undefined;
		errorMessage: string | undefined;
		createTime: string | undefined;
		moduleName: string | undefined;
		requestType: string | undefined;
		ip: string | undefined;
		address: string | undefined;
		operator: string | undefined;
		costTime: number | string;
	};

	const access = useAccess()
	const actionRef = useRef();
	const [list, setList] = useState<TableColumns[]>([]);
	const [param, setParam] = useState<any>({});

	const getPageQuery = (params: any) => {
		const param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: trim(params?.name),
			status: params?.status,
			moduleName: trim(params?.moduleName),
			ip: trim(params?.ip),
			requestType: trim(params?.requestType),
			operator: trim(params?.operator),
			profile: trim(params?.profile),
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
			title: '模块名称',
			dataIndex: 'moduleName',
			ellipsis: true
		},
		{
			title: '操作名称',
			dataIndex: 'name',
			ellipsis: true
		},
		{
			title: '请求类型',
			dataIndex: 'requestType',
			ellipsis: true
		},
		{
			title: '操作人员',
			dataIndex: 'operator',
			ellipsis: true
		},
		{
			title: 'IP地址',
			dataIndex: 'ip',
			ellipsis: true
		},
		{
			title: 'IP归属地',
			dataIndex: 'address',
			ellipsis: true,
			hideInSearch: true
		},
		{
			title: '状态',
			dataIndex: 'status',
			valueEnum: {
				[STATUS.OK]: getStatus(STATUS.OK),
				[STATUS.FAIL]: getStatus(STATUS.FAIL)
			},
			ellipsis: true
		},
		{
			title: '错误信息',
			dataIndex: 'errorMessage',
			ellipsis: true
		},
		{
			title: '消耗时间(毫秒)',
			dataIndex: 'costTime',
			hideInSearch: true,
			ellipsis: true
		},
		{
			title: '创建时间',
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
			ellipsis: true
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
		},
		{
			title: '操作',
			valueType: 'option',
			key: 'option',
			render: (_, record) => [
				( access.canOperateLogGetDetail && <a key="get"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   if (res.code === 'OK') {
							   setDataSource(res?.data)
							   setModalVisit(true)
						   }
					   })
				   }}
				>
					查看
				</a>)
			],
		},
	];

	return (
		<>
			<OperateLogDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				dataSource={dataSource}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					const list: TableColumns[] = []
					return pageV3(getPageQuery(params)).then(res => {
						res?.data?.records?.forEach((item: TableColumns) => {
							item.status = statusEnum[item.status as '0'];
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
								item.status = getStatus(item.status as '0')?.text
								_list.push(item)
							})
							ExportToExcel({
								sheetData: _list,
								sheetFilter: ["moduleName", "name", "requestType", "operator", "ip", "address", "status", "errorMessage", "costTime", "createTime"],
								sheetHeader: ["模块名称", "操作名称", "请求类型", "操作人员", "IP地址", "IP归属地", "状态", "错误信息", "消耗时间(毫秒)", "创建时间"],
								fileName: "操作日志_导出_" + moment(new Date()).format('YYYYMMDDHHmmss'),
								sheetName: "操作日志"
							})
						}}>
							导出
						</Button>,
						( access.canOperateLogExport && <Button key="exportAll" type="primary" icon={<ExportOutlined/>} onClick={() => {
							exportV3(param)
						}}>
							导出全部
						</Button>)
					]
				}
				dateFormatter="string"
				toolbar={{
					title: '操作日志',
					tooltip: '操作日志',
				}}
			/>
		</>
	);
};
