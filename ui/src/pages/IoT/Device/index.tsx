import {DrawerForm, ProColumns, ProFormText} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {exportV3, pageV3, getByIdV3} from "@/services/iot/device";
import {Button} from "antd";
import {ExportOutlined} from "@ant-design/icons";
import {trim} from "@/utils/format";
import {Excel, ExportToExcel} from "@/utils/export";
import moment from "moment";
import {useRef, useState} from "react";
import {getStatus, STATUS} from "@/services/constant";

export default () => {

	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState({})

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

	let list: TableColumns[]

	let param: any

	const getPageQuery = (params: any) => {
		let startTime = params?.startDate;
		let endTime = params?.endDate;
		if (startTime && endTime) {
			startTime += ' 00:00:00'
			endTime += ' 23:59:59'
		}
		param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			code: trim(params?.code),
			name: trim(params?.name),
			status: params?.status,
			errorMessage: trim(params?.errorMessage),
			params: {
				startTime: startTime,
				endTime: endTime
			}
		};
		return param;
	}

	const exportToExcel = async () => {
		let _param: Excel
		const _list: TableColumns[] = [];
		// 格式化数据
		list.forEach(item => {
			item.status = getStatus(item.status as '0')?.text
			_list.push(item)
		})
		_param = {
			sheetData: _list,
			sheetFilter: ["code", "name", "status", "param", "errorMessage", "createTime"],
			sheetHeader: ["标识", "名称", "状态", "参数", "错误信息", "创建时间"],
			fileName: "通知日志" + "_" + moment(new Date()).format('YYYYMMDDHHmmss'),
			sheetName: "通知日志"
		}
		ExportToExcel(_param)
	}

	const exportAllToExcel = async () => {
		exportV3(param)
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
				<a key="getable"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   setDataSource(res?.data)
						   setModalVisit(true)
					   })
				   }}
				>
					查看
				</a>
			],
		},
	];

	return (
		<>
			<DrawerForm<{
				code: string;
				name: string;
				status: number;
				param: string;
				errorMessage: string;
				createTime: string;
			}>
				open={modalVisit}
				title="查看通知日志"
				drawerProps={{
					destroyOnClose: true,
					closable: true,
					maskClosable: true
				}}
				initialValues={dataSource}
				onOpenChange={setModalVisit}
				submitter={{
					submitButtonProps: {
						style: {
							display: 'none',
						},
					}
				}}
			>
				<ProFormText
					readonly={true}
					name="code"
					label="标识"
					rules={[{ required: true, message: '请输入标识' }]}
				/>

				<ProFormText
					readonly={true}
					name="name"
					label="名称"
					rules={[{ required: true, message: '请输入名称' }]}
				/>

				<ProFormText
					readonly={true}
					name="status"
					label="状态"
					rules={[{ required: true, message: '请输入状态' }]}
					// @ts-ignore
					convertValue={(value) => {
						return getStatus(value as '0')?.text
					}}
				/>

				<ProFormText
					readonly={true}
					name="param"
					label="参数"
					rules={[{ required: true, message: '请输入参数' }]}
				/>

				<ProFormText
					readonly={true}
					name="errorMessage"
					label="错误信息"
					rules={[{ required: true, message: '请输入错误信息' }]}
				/>

				<ProFormText
					readonly={true}
					name="createTime"
					label="创建时间"
				/>

			</DrawerForm>
			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={(params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					list = []
					return pageV3(getPageQuery(params)).then(res => {
						res?.data?.records?.forEach((item: TableColumns) => {
							item.status = statusEnum[item.status as '0'];
							list.push(item);
						});
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
		</>
	);
};
