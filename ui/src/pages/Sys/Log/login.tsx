import { exportLoginLog, pageLoginLog } from '@/services/admin/loginLog';
import { ExportToExcel } from '@/utils/export';
import { trim } from '@/utils/format';
import { useAccess, useIntl } from '@@/exports';
import { ExportOutlined } from '@ant-design/icons';
import type { ProColumns } from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';
import { Button } from 'antd';
import moment from 'moment';
import { useRef, useState } from 'react';

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
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const actionRef = useRef<any>(null);
	const [list, setList] = useState<TableColumns[]>([]);
	const [param, setParam] = useState<any>({});
	const [loading, setLoading] = useState(false);

	const getLoginType = (type: string) => {
		return {
			username_password: t('login.usernamePassword'),
			mobile: t('login.mobile'),
			mail: t('login.mail'),
			authorization_code: t('sys.loginLog.type.authorizationCode'),
		}[type];
	};

	const getLoginStatus = (status: string) => {
		return {
			'0': t('sys.loginLog.status.success'),
			'1': t('sys.loginLog.status.fail'),
		}[status];
	};

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
			width: 85,
		},
		{
			title: t('sys.user.username'),
			dataIndex: 'username',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.user.placeholder.username'),
			},
		},
		{
			title: t('sys.loginLog.ip'),
			dataIndex: 'ip',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.loginLog.placeholder.ip'),
			},
		},
		{
			title: t('sys.loginLog.address'),
			dataIndex: 'address',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.loginLog.placeholder.address'),
			},
		},
		{
			title: t('sys.loginLog.browser'),
			dataIndex: 'browser',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.loginLog.placeholder.browser'),
			},
		},
		{
			title: t('sys.loginLog.os'),
			dataIndex: 'os',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.loginLog.placeholder.os'),
			},
		},
		{
			title: t('sys.loginLog.status'),
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
						label: t('sys.loginLog.status.success'),
					},
					{
						value: '1',
						label: t('sys.loginLog.status.fail'),
					},
				],
				placeholder: t('sys.loginLog.placeholder.status'),
			},
		},
		{
			title: t('sys.loginLog.status'),
			dataIndex: 'status',
			hideInSearch: true,
			valueEnum: {
				'0': { text: t('sys.loginLog.status.success'), status: 'Success' },
				'1': { text: t('sys.loginLog.status.fail'), status: 'Error' },
			},
			ellipsis: true,
		},
		{
			title: t('sys.loginLog.errorMessage'),
			dataIndex: 'errorMessage',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.loginLog.placeholder.errorMessage'),
			},
		},
		{
			title: t('sys.loginLog.type'),
			key: 'typeValue',
			dataIndex: 'typeValue',
			valueType: 'select',
			hideInTable: true,
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				options: [
					{
						value: 'username_password',
						label: t('login.usernamePassword'),
					},
					{
						value: 'mail',
						label: t('login.mail'),
					},
					{
						value: 'mobile',
						label: t('login.mobile'),
					},
					{
						value: 'authorization_code',
						label: t('sys.loginLog.type.authorizationCode'),
					},
				],
				placeholder: t('sys.loginLog.placeholder.type'),
			},
		},
		{
			title: t('sys.loginLog.type'),
			dataIndex: 'type',
			hideInSearch: true,
			valueEnum: {
				authorization_code: {
					text: t('sys.loginLog.type.authorizationCode'),
					status: 'Error',
				},
				mail: { text: t('login.mail'), status: 'Success' },
				mobile: { text: t('login.mobile'), status: 'Default' },
				username_password: {
					text: t('login.usernamePassword'),
					status: 'Processing',
				},
			},
			width: 160,
			ellipsis: true,
		},
		{
			title: t('sys.loginLog.loginTime'),
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
			ellipsis: true,
		},
		{
			title: t('sys.loginLog.loginTime'),
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
	];

	return (
		<ProTable<TableColumns>
			actionRef={actionRef}
			columns={columns}
			request={async (params) => {
				// 表单搜索项会从 params 传入，传递给后端接口。
				const list: TableColumns[] = [];
				return pageLoginLog(getPageQueryParam(params)).then((res) => {
					res?.data?.records?.forEach((item: TableColumns) => {
						item.status = item.status as string;
						item.type = item.type as string;
						list.push(item);
					});
					setList(list);
					return Promise.resolve({
						data: list,
						total: parseInt(res?.data?.total || 0),
						success: true,
					});
				});
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
							item.status = getLoginStatus(item.status as string);
							item.type = getLoginType(item.type as string);
							_list.push(item);
						});
						ExportToExcel({
							sheetData: _list,
							sheetFilter: [
								'username',
								'ip',
								'address',
								'browser',
								'os',
								'status',
								'errorMessage',
								'type',
								'createTime',
							],
							sheetHeader: [
								t('sys.user.username'),
								t('sys.loginLog.ip'),
								t('sys.loginLog.address'),
								t('sys.loginLog.browser'),
								t('sys.loginLog.os'),
								t('sys.loginLog.status'),
								t('sys.loginLog.errorMessage'),
								t('sys.loginLog.type'),
								t('sys.loginLog.loginTime'),
							],
							fileName:
								t('sys.loginLog.exportFilePrefix') +
								moment(new Date()).format('YYYYMMDDHHmmss'),
							sheetName: t('sys.loginLog.title'),
						});
					}}
				>
					{t('sys.commonLog.export')}
				</Button>,
				access.canLoginLogExport && (
					<Button
						loading={loading}
						key="exportAll"
						type="primary"
						icon={<ExportOutlined />}
						onClick={() => {
							setLoading(true);
							exportLoginLog(param).finally(() => {
								setLoading(false);
							});
						}}
					>
						{t('sys.commonLog.exportAll')}
					</Button>
				),
			]}
			dateFormatter="string"
			toolbar={{
				title: t('menu.sys.log.login'),
				tooltip: t('menu.sys.log.login'),
			}}
		/>
	);
};
