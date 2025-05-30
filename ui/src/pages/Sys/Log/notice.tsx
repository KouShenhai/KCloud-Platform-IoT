import {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {exportNoticeLog, pageNoticeLog, getNoticeLogById} from "@/services/admin/noticeLog";
import {Button} from "antd";
import {ExportOutlined} from "@ant-design/icons";
import {trim} from "@/utils/format";
import {ExportToExcel} from "@/utils/export";
import moment from "moment";
import {useRef, useState} from "react";
import {NoticeLogDrawer} from "@/pages/Sys/Log/NoticeDrawer";
import {useAccess} from "@@/exports";

export default () => {

	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<any>({})
	const [loading, setLoading] = useState(false)

	type TableColumns = {
		id: number;
		code: string | undefined;
		name: string | undefined;
		status: string | undefined;
		param: string | undefined;
		errorMessage: string | undefined;
		createTime: string | undefined;
	};

	const access = useAccess()
	const actionRef = useRef();
	const [list, setList] = useState<TableColumns[]>([]);
	const [param, setParam] = useState<any>({});

	const getStatus = (status: string) => {
		return {
			'0': '成功',
			'1': '失败',
		}[status]
	}

	const getPageQueryParam = (params: any) => {
		const param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			code: trim(params?.code),
			name: trim(params?.name),
			status: params?.statusValue,
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
			title: '通知编码',
			dataIndex: 'code',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入通知编码',
			}
		},
		{
			title: '通知名称',
			dataIndex: 'name',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入通知名称',
			}
		},
		{
			title: '通知状态',
			key: 'statusValue',
			dataIndex: 'statusValue',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: '请选择通知状态',
				options: [
					{
						label: '成功',
						value: '0',
					},
					{
						label: '失败',
						value: '1',
					},
				],
			}
		},
		{
			title: '通知状态',
			dataIndex: 'status',
			hideInSearch: true,
			valueEnum: {
				'0': {text: '成功', status: 'Success'},
				'1': {text: '失败', status: 'Error'},
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
			dataIndex: 'createTimeValue',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: ['请选择开始时间', '请选择结束时间'],
			},
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
				( access.canNoticeLogGetDetail && <a key="get"
				   onClick={() => {
					   getNoticeLogById({id: record?.id}).then(res => {
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

			<NoticeLogDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				dataSource={dataSource}
				// @ts-ignore
				getStatus={getStatus}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					const list: TableColumns[] = []
					return pageNoticeLog(getPageQueryParam(params)).then(res => {
						res?.data?.records?.forEach((item: TableColumns) => {
							item.status = item.status as string;
							list.push(item);
						});
						setList(list)
						return Promise.resolve({
							data: list,
							total: parseInt(res?.data?.total || 0),
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
								item.status = getStatus(item.status as string)
								_list.push(item)
							})
							ExportToExcel({
								sheetData: _list,
								sheetFilter: ["code", "name", "status", "param", "errorMessage", "createTime"],
								sheetHeader: ["通知编码", "通知名称", "通知状态", "通知参数", "错误信息", "创建时间"],
								fileName: "通知日志_导出_" + moment(new Date()).format('YYYYMMDDHHmmss'),
								sheetName: "通知日志"
							})
						}}>
							导出
						</Button>,
						( access.canNoticeLogExport && <Button loading={loading} key="exportAll" type="primary" icon={<ExportOutlined/>} onClick={() => {
							setLoading(true)
							exportNoticeLog(param).finally(() => {
								setLoading(false)
							})
						}}>
							导出全部
						</Button>)
					]
				}
				dateFormatter="string"
				toolbar={{
					title: '通知日志',
					tooltip: '通知日志',
				}}
			/>
		</>
	);
};
