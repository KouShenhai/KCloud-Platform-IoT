import {
	getGatewayPropertiesById,
	type GatewayId,
	type GatewayProperties,
} from '@/services/iot/gateway';
import { useIntl } from '@@/exports';
import { Alert, Button, Descriptions, Drawer, Table, Typography } from 'antd';
import { useEffect, useState } from 'react';

type GatewayPreviewProps = {
	id: GatewayId;
	onClose: () => void;
};

export const GatewayPreviewDrawer = ({ id, onClose }: GatewayPreviewProps) => {
	const intl = useIntl();
	const t = (id: string) => intl.formatMessage({ id });
	const [snapshot, setSnapshot] = useState<GatewayProperties | null>(null);
	const [loading, setLoading] = useState(false);

	useEffect(() => {
		setLoading(true);
		getGatewayPropertiesById({ id })
			.then(({ data }) => {
				if (data?.timestamp) {
					const newData = {
						...data,
						properties: {
							...data?.properties,
							timestamp: data?.timestamp,
						}
					};
					setSnapshot(newData);
				} else {
					// @ts-ignore
					setSnapshot(data);
				}
			}).catch(err => {
				console.error(err);
			}).finally(() => {
				setLoading(false);
			});
	}, [id]);

	const rows = Object.entries(snapshot?.properties ?? {}).map(
		([name, value]) => ({ name, value }),
	);

	return (
		<Drawer
			open
			title={t('iot.gateway.preview.title')}
			size={800}
			loading={snapshot === undefined}
			closable={false}
			keyboard={false}
			onClose={onClose}
			styles={{ footer: { textAlign: 'right' } }}
			footer={<Button onClick={onClose}>{t('common.close')}</Button>}
		>
			{snapshot && (
				<>
					<Descriptions
						size="small"
						column={{ xs: 1, sm: 3 }}
						items={[
							{
								key: 'sn',
								label: t('iot.gateway.sn'),
								children: snapshot.sn || '—',
							},
							{
								key: 'name',
								label: t('iot.gateway.name'),
								children: snapshot.name || '—',
							},
							{
								key: 'state',
								label: t('iot.gateway.state'),
								children: snapshot.name || '—',
							},
						]}
					/>
					<Typography.Title level={5}>
						{t('iot.gateway.preview.properties')}
					</Typography.Title>
					<Table
						size="small"
						rowKey="name"
						dataSource={rows}
						loading={loading}
						pagination={false}
						locale={{ emptyText: t('iot.gateway.preview.empty') }}
						columns={[
							{
								title: t('iot.gateway.preview.propertyName'),
								dataIndex: 'name',
								width: '35%',
							},
							{
								title: t('iot.gateway.preview.propertyValue'),
								dataIndex: 'value',
								render: (value: unknown) => (
									<Typography.Text style={{ overflowWrap: 'anywhere' }}>
										{typeof value === 'string' ? value : JSON.stringify(value)}
									</Typography.Text>
								),
							},
						]}
					/>
				</>
			)}
		</Drawer>
	);
};
