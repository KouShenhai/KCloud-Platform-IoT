import { pageOss } from '@/services/admin/oss';
import { trim } from '@/utils/format';
import { useIntl } from '@@/exports';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';
import { Space, Switch, Tag } from 'antd';
import { useRef } from 'react';

export default () => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	type TableColumns = {
		id: number | undefined;
		name: string | undefined;
		status: number | undefined;
		type: string | undefined;
		createTime: string | undefined;
	};

	const actionRef = useRef<ActionType | null>(null);

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: trim(params?.name),
			status: params?.statusValue,
			type: params?.typeValue,
			params: {
				startTime: params?.startDate
					? `${params.startDate} 00:00:00`
					: undefined,
				endTime: params?.endDate
					? `${params.endDate} 23:59:59`
					: undefined,
			},
		};
	};

	const columns: ProColumns<TableColumns>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 85,
		},
		{
			title: t('sys.oss.config.name'),
			dataIndex: 'name',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.oss.config.placeholder.name'),
			},
		},
		{
			title: t('sys.oss.config.type'),
			key: 'typeValue',
			dataIndex: 'typeValue',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: t('sys.oss.config.placeholder.type'),
				options: [
					{
						value: 'amazon_s3',
						label: t('sys.oss.config.type.amazonS3'),
					},
					{
						value: 'local',
						label: t('sys.oss.config.type.local'),
					},
					{
						value: 'minio',
						label: 'MinIO',
					},
				],
			},
			ellipsis: true,
		},
		{
			disable: true,
			title: t('sys.oss.config.type'),
			dataIndex: 'type',
			hideInSearch: true,
			renderFormItem: (_, { defaultRender }) => {
				return defaultRender(_);
			},
			render: (_, record) => (
				<Space>
					{record?.type === 'amazon_s3' && (
						<Tag color={'rgb(51 114 253)'} key={'amazon_s3'}>
							{t('sys.oss.config.type.amazonS3')}
						</Tag>
					)}
					{record?.type === 'local' && (
						<Tag color={'#fd5251'} key={'local'}>
							{t('sys.oss.config.type.local')}
						</Tag>
					)}
					{record?.type === 'minio' && (
						<Tag color={'green-inverse'} key={'minio'}>
							MinIO
						</Tag>
					)}
				</Space>
			),
		},
		{
			title: t('sys.oss.config.status'),
			key: 'statusValue',
			dataIndex: 'statusValue',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: t('sys.oss.config.placeholder.status'),
				options: [
					{
						value: 0,
						label: t('common.enable'),
					},
					{
						value: 1,
						label: t('common.disable'),
					},
				],
			},
			ellipsis: true,
		},
		{
			title: t('sys.oss.config.status'),
			dataIndex: 'status',
			hideInSearch: true,
			render: (_, record) => (
				<Switch
					checkedChildren={t('common.enable')}
					unCheckedChildren={t('common.disable')}
					disabled={true}
					checked={record?.status === 0}
				/>
			),
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
	];

	return (
		<ProTable<TableColumns>
			actionRef={actionRef}
			columns={columns}
			request={async (params) => {
				// 表单搜索项会从 params 传入，传递给后端接口。
				return pageOss(getPageQueryParam(params)).then((res) => {
					return Promise.resolve({
						data: res?.data?.records,
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
			dateFormatter="string"
			toolbar={{
				title: t('menu.sys.oss.config'),
				tooltip: t('menu.sys.oss.config'),
			}}
		/>
	);
};
