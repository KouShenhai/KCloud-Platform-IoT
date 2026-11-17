import {
	pageGatewayCommand,
	type GatewayCommand,
	type GatewayCommandPageQry,
	type GatewayId,
} from '@/services/iot/gateway';
import { trim } from '@/utils/format';
import { useIntl } from '@@/exports';
import { ProTable } from '@ant-design/pro-components';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { Descriptions, message } from 'antd';
import { useRef } from 'react';

type TableQueryParams = {
	current?: number;
	pageSize?: number;
	command?: string;
	status?: number;
	startDate?: string;
	endDate?: string;
	replyStatus?: number;
};

export const GatewayCommandTable = ({ id }: { id: GatewayId }) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const actionRef = useRef<ActionType | null>(null);

	const getPageQueryParam = (
		params: TableQueryParams,
	): GatewayCommandPageQry => {
		const pageSize = params.pageSize ?? 10;
		const pageNum = params.current ?? 1;
		return {
			gatewayId: id,
			pageSize,
			pageNum,
			pageIndex: pageSize * (pageNum - 1),
			command: trim(params.command ?? ''),
			status: params.status == null ? undefined : Number(params.status),
			replyStatus: params.replyStatus == null ? undefined : Number(params.replyStatus),
			params: {
				startTime: params.startDate
					? `${params.startDate} 00:00:00`
					: undefined,
				endTime: params.endDate
					? `${params.endDate} 23:59:59`
					: undefined,
			},
		};
	};

	const columns: ProColumns<GatewayCommand>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 85,
		},
		{
			title: t('iot.gateway.command.id'),
			dataIndex: 'id',
			valueType: 'text',
			hideInSearch: true,
			width: 180,
			ellipsis: true,
		},
		{
			title: t('iot.gateway.command.command'),
			dataIndex: 'command',
			valueType: 'select',
			width: 160,
			initialValue: 'reboot_gateway',
			hideInTable: true,
			valueEnum: {
				reboot_gateway: { text: t('iot.gateway.command.reboot') }
			},
		},
		{
			title: t('iot.gateway.command.command'),
			dataIndex: 'command',
			width: 160,
			hideInSearch: true,
			valueEnum: {
				reboot_gateway: { text: t('iot.gateway.command.reboot') },
				read_device_properties: { text: t('iot.gateway.command.read') },
				write_device_properties: {
					text: t('iot.gateway.command.write'),
				},
			},
		},
		{
			title: t('iot.gateway.command.deviceSn'),
			dataIndex: 'deviceSn',
			valueType: 'text',
			hideInSearch: true,
			width: 160,
			ellipsis: true,
		},
		{
			title: t('iot.gateway.command.status'),
			dataIndex: 'status',
			valueType: 'select',
			width: 110,
			valueEnum: {
				0: {
					text: t('iot.gateway.command.status.0'),
					status: 'Processing',
				},
				1: {
					text: t('iot.gateway.command.status.1'),
					status: 'Success',
				},
				2: {
					text: t('iot.gateway.command.status.2'),
					status: 'Error'
				},
			},
		},
		{
			title: t('iot.gateway.command.replyStatus'),
			dataIndex: 'replyStatus',
			valueType: 'select',
			width: 110,
			valueEnum: {
				0: {
					text: t('iot.gateway.command.replyStatus.0'),
					status: 'Success',
				},
				1: {
					text: t('iot.gateway.command.replyStatus.1'),
					status: 'Error',
				},
			},
		},
		{
			title: t('iot.gateway.command.downTime'),
			dataIndex: 'downTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
		},
		{
			title: t('iot.gateway.command.replyTime'),
			dataIndex: 'replyTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
		},
		{
			title: t('common.createTime'),
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
		},
		{
			title: t('common.createTime'),
			dataIndex: 'createTime',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: [
					t('common.selectStartTime'),
					t('common.selectEndTime'),
				],
			},
			search: {
				transform: (value) => ({
					startDate: value[0],
					endDate: value[1],
				}),
			},
		},
	];

	return (
		<ProTable<GatewayCommand, TableQueryParams>
			key={id}
			actionRef={actionRef}
			columns={columns}
			request={async (params) => {
				const res = await pageGatewayCommand(getPageQueryParam(params));
				if (res.code !== 'OK') {
					throw new Error(res.msg || t('iot.gateway.command.error'));
				}
				return {
					data: res.data?.records ?? [],
					total: Number(res.data?.total ?? 0),
					success: true,
				};
			}}
			onRequestError={() => {
				message.error(t('iot.gateway.command.error'));
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
			scroll={{ x: 1400 }}
			locale={{ emptyText: t('iot.gateway.command.empty') }}
			expandable={{
				expandedRowRender: (record) => (
					<Descriptions
						column={1}
						size="small"
						styles={{
							content: {
								overflowWrap: 'anywhere',
								whiteSpace: 'pre-wrap',
							},
						}}
						items={(
							[
								'deviceName',
								'topic',
								'param',
								'errorMessage',
								'replyPayload',
								'replyMessage',
							] as const
						).map((key) => ({
							key,
							label: t(`iot.gateway.command.${key}`),
							children: record[key] || '—',
						}))}
					/>
				),
			}}
			dateFormatter="string"
			toolbar={{
				title: t('iot.gateway.command.title'),
				tooltip: t('iot.gateway.command.title'),
			}}
		/>
	);
};
