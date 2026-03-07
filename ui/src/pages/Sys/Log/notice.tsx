import { NoticeLogDrawer } from '@/pages/Sys/Log/NoticeDrawer';
import {
	exportNoticeLog,
	getNoticeLogById,
	pageNoticeLog,
} from '@/services/admin/noticeLog';
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
		code: string | undefined;
		name: string | undefined;
		status: string | undefined;
		param: string | undefined;
		errorMessage: string | undefined;
		createTime: string | undefined;
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
			code: trim(params?.code),
			name: trim(params?.name),
			status: params?.statusValue,
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
			title: t('sys.log.notice.code'),
			dataIndex: 'code',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.log.notice.placeholder.code'),
			},
		},
		{
			title: t('sys.log.notice.name'),
			dataIndex: 'name',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.log.notice.placeholder.name'),
			},
		},
		{
			title: t('sys.log.notice.status'),
			key: 'statusValue',
			dataIndex: 'statusValue',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: t('sys.log.notice.placeholder.status'),
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
			},
		},
		{
			title: t('sys.log.notice.status'),
			dataIndex: 'status',
			hideInSearch: true,
			valueEnum: {
				'0': { text: t('sys.log.common.success'), status: 'Success' },
				'1': { text: t('sys.log.common.fail'), status: 'Error' },
			},
			ellipsis: true,
		},
		{
			title: t('sys.log.notice.errorMessage'),
			dataIndex: 'errorMessage',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.log.notice.placeholder.errorMessage'),
			},
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
				access.canNoticeLogGetDetail && (
					<a
						key="get"
						onClick={() => {
							getNoticeLogById({ id: record?.id }).then((res) => {
								if (res.code === 'OK') {
									setDataSource(res?.data);
									setModalVisit(true);
								}
							});
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
					const list: TableColumns[] = [];
					return pageNoticeLog(getPageQueryParam(params)).then(
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
									'code',
									'name',
									'status',
									'param',
									'errorMessage',
									'createTime',
								],
								sheetHeader: [
									t('sys.log.notice.code'),
									t('sys.log.notice.name'),
									t('sys.log.notice.status'),
									t('sys.log.notice.param'),
									t('sys.log.notice.errorMessage'),
									t('common.createTime'),
								],
								fileName:
									t('sys.log.notice.exportFilePrefix') +
									moment(new Date()).format('YYYYMMDDHHmmss'),
								sheetName: t('sys.log.notice.title'),
							});
						}}
					>
						{t('sys.log.common.export')}
					</Button>,
					access.canNoticeLogExport && (
						<Button
							loading={loading}
							key="exportAll"
							type="primary"
							icon={<ExportOutlined />}
							onClick={() => {
								setLoading(true);
								exportNoticeLog(param).finally(() => {
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
					title: t('sys.log.notice.title'),
					tooltip: t('sys.log.notice.title'),
				}}
			/>
		</>
	);
};
