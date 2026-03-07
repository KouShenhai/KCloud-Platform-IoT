import { OperateLogDrawer } from '@/pages/Sys/Log/OperateDrawer';
import {
	exportOperateLog,
	getOperateLogById,
	pageOperateLog,
} from '@/services/admin/operateLog';
import { ExportToExcel } from '@/utils/export';
import { trim } from '@/utils/format';
import { useAccess, useIntl } from '@@/exports';
import { ExportOutlined } from '@ant-design/icons';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import { Button } from 'antd';
import moment from 'moment';
import { useRef, useState } from 'react';

export default () => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<any>({});
	const [loading, setLoading] = useState(false);

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

	const access = useAccess();
	const actionRef = useRef<any>(null);
	const [list, setList] = useState<TableColumns[]>([]);
	const [param, setParam] = useState<any>({});

	const getStatus = (status: string) => {
		return {
			'0': t('sys.log.common.success'),
			'1': t('sys.log.common.fail'),
		}[status];
	};

	const getPageQueryParam = (params: any) => {
		const param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: trim(params?.name),
			status: params?.statusValue,
			moduleName: trim(params?.moduleName),
			ip: trim(params?.ip),
			requestType: trim(params?.requestType),
			operator: trim(params?.operator),
			profile: trim(params?.profile),
			errorMessage: trim(params?.errorMessage),
			params: {
				startTime: params?.startDate
					? `${params.startDate} 00:00:00`
					: undefined,
				endTime: params?.endDate
					? `${params.endDate} 23:59:59`
					: undefined,
			},
		};
		setParam(param);
		return param;
	};

	const columns: ProColumns<TableColumns>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 60,
		},
		{
			title: t('sys.log.operate.moduleName'),
			dataIndex: 'moduleName',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.log.operate.placeholder.moduleName'),
			},
		},
		{
			title: t('sys.log.operate.name'),
			dataIndex: 'name',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.log.operate.placeholder.name'),
			},
		},
		{
			title: t('sys.log.operate.requestType'),
			dataIndex: 'requestType',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.log.operate.placeholder.requestType'),
			},
		},
		{
			title: t('sys.log.operate.operator'),
			dataIndex: 'operator',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.log.operate.placeholder.operator'),
			},
		},
		{
			title: t('sys.log.operate.ip'),
			dataIndex: 'ip',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.log.operate.placeholder.ip'),
			},
		},
		{
			title: t('sys.log.operate.address'),
			dataIndex: 'address',
			ellipsis: true,
			hideInSearch: true,
		},
		{
			title: t('sys.log.operate.status'),
			dataIndex: 'statusValue',
			valueType: 'select',
			hideInTable: true,
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				options: [
					{
						label: t('sys.log.common.success'),
						value: '0',
					},
					{
						label: t('sys.log.common.fail'),
						value: '1',
					},
				],
				placeholder: t('sys.log.operate.placeholder.status'),
			},
		},
		{
			title: t('sys.log.operate.status'),
			dataIndex: 'status',
			hideInSearch: true,
			valueEnum: {
				'0': { text: t('sys.log.common.success'), status: 'Success' },
				'1': { text: t('sys.log.common.fail'), status: 'Error' },
			},
			width: 80,
		},
		{
			title: t('sys.log.operate.errorMessage'),
			dataIndex: 'errorMessage',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.log.operate.placeholder.errorMessage'),
			},
		},
		{
			title: t('sys.log.operate.costTime'),
			dataIndex: 'costTime',
			hideInSearch: true,
			ellipsis: true,
		},
		{
			title: t('common.createTime'),
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
			ellipsis: true,
		},
		{
			title: t('common.createTime'),
			dataIndex: 'createTimeValue',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: [t('common.selectStartTime'), t('common.selectEndTime')],
			},
			search: {
				transform: (value) => {
					return {
						startDate: value[0],
						endDate: value[1],
					};
				},
			},
		},
		{
			title: t('common.operation'),
			valueType: 'option',
			key: 'option',
			render: (_, record) => [
				access.canOperateLogGetDetail && (
					<a
						key="get"
						onClick={() => {
							getOperateLogById({ id: record?.id }).then(
								(res) => {
									if (res.code === 'OK') {
										setDataSource(res?.data);
										setModalVisit(true);
									}
								},
							);
						}}
					>
						{t('common.view')}
					</a>
				),
			],
		},
	];

	return (
		<>
			<OperateLogDrawer
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
					const list: TableColumns[] = [];
					return pageOperateLog(getPageQueryParam(params)).then(
						(res) => {
							res?.data?.records?.forEach(
								(item: TableColumns) => {
									item.status = item.status as string;
									list.push(item);
								},
							);
							setList(list);
							return Promise.resolve({
								data: list,
								total: parseInt(res?.data?.total || 0),
								success: true,
							});
						},
					);
				}}
				rowKey="id"
				pagination={{
					showQuickJumper: true,
					showSizeChanger: false,
					pageSize: 10,
				}}
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
				toolBarRender={() => [
					<Button
						key="export"
						type="primary"
						ghost
						icon={<ExportOutlined />}
						onClick={() => {
							const _list: TableColumns[] = [];
							// 格式化数据
							list.forEach((item) => {
								item.status = getStatus(item.status as string);
								_list.push(item);
							});
							ExportToExcel({
								sheetData: _list,
								sheetFilter: [
									'moduleName',
									'name',
									'requestType',
									'operator',
									'ip',
									'address',
									'status',
									'errorMessage',
									'costTime',
									'createTime',
								],
								sheetHeader: [
									t('sys.log.operate.moduleName'),
									t('sys.log.operate.name'),
									t('sys.log.operate.requestType'),
									t('sys.log.operate.operator'),
									t('sys.log.operate.ip'),
									t('sys.log.operate.address'),
									t('sys.log.operate.status'),
									t('sys.log.operate.errorMessage'),
									t('sys.log.operate.costTime'),
									t('common.createTime'),
								],
								fileName:
									t('sys.log.operate.exportFilePrefix') +
									moment(new Date()).format('YYYYMMDDHHmmss'),
								sheetName: t('sys.log.operate.title'),
							});
						}}
					>
						{t('sys.log.common.export')}
					</Button>,
					access.canOperateLogExport && (
						<Button
							loading={loading}
							key="exportAll"
							type="primary"
							icon={<ExportOutlined />}
							onClick={() => {
								setLoading(true);
								exportOperateLog(param).finally(() => {
									setLoading(false);
								});
							}}
						>
							{t('sys.log.common.exportAll')}
						</Button>
					),
				]}
				dateFormatter="string"
				toolbar={{
					title: t('sys.log.operate.title'),
					tooltip: t('sys.log.operate.title'),
				}}
			/>
		</>
	);
};
