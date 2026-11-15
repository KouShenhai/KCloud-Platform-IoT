import {
	pageGatewayCommand,
	type GatewayCommand,
	type GatewayId,
} from '@/services/iot/gateway';
import { formatTimestamp } from '@/utils/format';
import { useIntl } from '@@/exports';
import { Alert, Button, Descriptions, Space, Table, Tag } from 'antd';
import { useEffect, useState } from 'react';

export const GatewayCommandTable = ({ id }: { id: GatewayId }) => {
	const intl = useIntl();
	const t = (key: string) =>
		intl.formatMessage({ id: `iot.gateway.command.${key}` });
	const [page, setPage] = useState({ pageNum: 1, pageSize: 10 });
	const [records, setRecords] = useState<GatewayCommand[]>([]);
	const [total, setTotal] = useState(0);
	const [loading, setLoading] = useState(false);

	useEffect(() => {
		setLoading(true);
		pageGatewayCommand(id, page)
			.then(({ data }) => {
				setRecords(data?.records ?? []);
				setTotal(data?.total ?? 0);
			})
			.catch(() => {
				setRecords([]);
				setTotal(0);
			})
			.finally(() => {
				setLoading(false);
			});
	});

	return (
		<Space orientation="vertical" style={{ width: '100%' }}>
			{<Alert type="error" title={t('error')} />}
			<Table<GatewayCommand>
				size="small"
				rowKey="id"
				loading={loading}
				dataSource={records}
				scroll={{ x: 950 }}
				locale={{ emptyText: t('empty') }}
				pagination={{
					current: page.pageNum,
					pageSize: page.pageSize,
					total,
					showSizeChanger: true,
					pageSizeOptions: [10, 20, 50, 100],
					onChange: (pageNum, pageSize) =>
						setPage({
							pageNum: pageSize !== page.pageSize ? 1 : pageNum,
							pageSize,
						}),
				}}
				columns={[
					{ title: t('id'), dataIndex: 'id', width: 180 },
					{
						title: t('command'),
						dataIndex: 'command',
						width: 140,
						render: (value) =>
							['reboot', 'read', 'write'].includes(value)
								? t(value)
								: value ?? '—',
					},
					{
						title: t('deviceSn'),
						dataIndex: 'deviceSn',
						width: 140,
						render: (value) => value || '—',
					},
					{
						title: t('status'),
						dataIndex: 'status',
						width: 100,
					},
					{
						title: t('dispatchTime'),
						dataIndex: 'dispatchTime',
						width: 210,
					},
					{
						title: t('replyTime'),
						dataIndex: 'replyTime',
						width: 210,
					},
				]}
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
							items={[
								...(
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
									label: t(key),
									children: record[key] || '—',
								})),
								{
									key: 'replyStatus',
									label: t('replyStatus'),
									children:
										record.replyStatus == null
											? '—'
											: t(
													`replyStatus.${record.replyStatus}`,
											  ),
								},
								{
									key: 'createTime',
									label: t('createTime'),
								},
							]}
						/>
					),
				}}
			/>
		</Space>
	);
};
