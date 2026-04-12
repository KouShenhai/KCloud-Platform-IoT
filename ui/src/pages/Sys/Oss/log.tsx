import { pageOssLog } from '@/services/admin/ossLog';
import { useIntl } from '@@/exports';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';
import { Space, Tag } from 'antd';
import { useRef } from 'react';

export default () => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	type TableColumns = {
		id: number | undefined;
		name: string | undefined;
		md5: number | undefined;
		url: string | undefined;
		size: number | undefined;
		contentType: string | undefined;
		format: string | undefined;
		ossId: number | undefined;
		type: string | undefined;
		createTime: string | undefined;
	};

	const actionRef = useRef<ActionType | null>(null);

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
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
			title: t('sys.ossLog.name'),
			dataIndex: 'name',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true,
		},
		{
			title: 'MD5',
			dataIndex: 'md5',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true,
		},
		{
			title: t('sys.ossLog.size'),
			dataIndex: 'size',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true,
		},
		{
			title: t('sys.ossLog.contentType'),
			dataIndex: 'contentType',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true,
		},
		{
			title: t('sys.ossLog.type'),
			dataIndex: 'type',
			ellipsis: true,
			valueType: 'text',
			renderFormItem: (_, { defaultRender }) => {
				return defaultRender(_);
			},
			render: (_, record) => (
				<Space>
					{record?.type === 'image' && (
						<Tag color={'rgb(51 114 253)'} key={'image'}>
							{t('sys.ossLog.type.image')}
						</Tag>
					)}
					{record?.type === 'video' && (
						<Tag color={'#fd5251'} key={'video'}>
							{t('sys.ossLog.type.video')}
						</Tag>
					)}
					{record?.type === 'audio' && (
						<Tag color={'#ffa500'} key={'audio'}>
							{t('sys.ossLog.type.audio')}
						</Tag>
					)}
				</Space>
			),
		},
		{
			title: t('sys.ossLog.format'),
			dataIndex: 'format',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true,
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
				return pageOssLog(getPageQueryParam(params)).then((res) => {
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
				title: t('menu.sys.oss.log'),
				tooltip: t('menu.sys.oss.log'),
			}}
		/>
	);
};
