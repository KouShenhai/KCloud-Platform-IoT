import {
	getGatewayProperties,
	sendGatewayCommand,
	type GatewayProperties,
} from '@/services/iot/gateway';
import { useIntl } from '@@/exports';
import { SendOutlined } from '@ant-design/icons';
import {
	Alert,
	Button,
	Descriptions,
	Drawer,
	Empty,
	Form,
	Input,
	Space,
	Table,
	Tabs,
	Typography,
} from 'antd';
import { useEffect, useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

type GatewayPreviewTarget = {
	id: string | number;
	name?: string;
	sn?: string;
};

type GatewayPreviewProps = {
	gateway: GatewayPreviewTarget;
	canReadProperties: boolean;
	canSendCommand: boolean;
	onClose: () => void;
};

const commandMaxLength = 4096;
const propertiesRefreshInterval = 15_000;

export const GatewayPreviewDrawer = ({
	gateway,
	canReadProperties,
	canSendCommand,
	onClose,
}: GatewayPreviewProps) => {
	const intl = useIntl();
	const t = (id: string) => intl.formatMessage({ id });
	const [form] = Form.useForm<{ command: string }>();
	const [snapshot, setSnapshot] = useState<GatewayProperties>();
	const [loading, setLoading] = useState(canReadProperties);
	const [loadError, setLoadError] = useState(false);
	const [sending, setSending] = useState(false);
	const [commandResult, setCommandResult] = useState<'success' | 'error'>();

	useEffect(() => {
		if (!canReadProperties) {
			return;
		}
		const controller = new AbortController();
		let requestPending = false;
		setLoading(true);
		setLoadError(false);
		const loadProperties = async () => {
			if (requestPending || controller.signal.aborted) {
				return;
			}
			requestPending = true;
			try {
				const response = await getGatewayProperties(
					gateway.id,
					controller.signal,
				);
				if (controller.signal.aborted) {
					return;
				}
				if (response.code === 'OK' && response.data) {
					setSnapshot(response.data);
					setLoadError(false);
				} else {
					setLoadError(true);
				}
			} catch {
				if (!controller.signal.aborted) {
					setLoadError(true);
				}
			} finally {
				requestPending = false;
				if (!controller.signal.aborted) {
					setLoading(false);
				}
			}
		};

		void loadProperties();
		const timer = window.setInterval(
			loadProperties,
			propertiesRefreshInterval,
		);
		return () => {
			window.clearInterval(timer);
			controller.abort();
		};
	}, [canReadProperties, gateway.id]);

	const submitCommand = async ({ command }: { command: string }) => {
		setSending(true);
		setCommandResult(undefined);
		try {
			const response = await sendGatewayCommand(
				gateway.id,
				command,
				uuidV7(),
			);
			if (response.code === 'OK') {
				form.resetFields();
				setCommandResult('success');
			} else {
				setCommandResult('error');
			}
		} catch {
			setCommandResult('error');
		} finally {
			setSending(false);
		}
	};

	const timestamp = snapshot?.timestamp;
	const reportedAt =
		timestamp == null ? undefined : new Date(Number(timestamp));
	const rows = Object.entries(snapshot?.properties ?? {}).map(
		([name, value]) => ({
			name,
			value,
		}),
	);

	const tabs = [];
	if (canReadProperties) {
		tabs.push({
			key: 'properties',
			label: t('iot.gateway.preview.properties'),
			children: (
				<Space
					direction="vertical"
					size="middle"
					style={{ width: '100%' }}
				>
					{reportedAt && !Number.isNaN(reportedAt.getTime()) && (
						<Typography.Text type="secondary">
							{intl.formatMessage(
								{ id: 'iot.gateway.preview.reportedAt' },
								{
									time: reportedAt.toLocaleString(
										intl.locale,
									),
								},
							)}
						</Typography.Text>
					)}
					{loadError && (
						<Alert
							type="error"
							showIcon
							message={t('iot.gateway.preview.loadError')}
						/>
					)}
					<Table
						size="small"
						rowKey="name"
						loading={loading}
						dataSource={rows}
						pagination={false}
						locale={{
							emptyText:
								loading || loadError ? (
									'—'
								) : (
									<Empty
										image={Empty.PRESENTED_IMAGE_SIMPLE}
										description={t(
											'iot.gateway.preview.empty',
										)}
									/>
								),
						}}
						columns={[
							{
								title: t('iot.gateway.preview.propertyName'),
								dataIndex: 'name',
								width: '38%',
							},
							{
								title: t('iot.gateway.preview.propertyValue'),
								dataIndex: 'value',
								render: (value: unknown) => (
									<Typography.Text
										style={{
											whiteSpace: 'pre-wrap',
											overflowWrap: 'anywhere',
										}}
									>
										{typeof value === 'string'
											? value
											: JSON.stringify(value, null, 2)}
									</Typography.Text>
								),
							},
						]}
					/>
				</Space>
			),
		});
	}
	if (canSendCommand) {
		tabs.push({
			key: 'command',
			label: t('iot.gateway.preview.command'),
			children: (
				<Form
					form={form}
					layout="vertical"
					onFinish={submitCommand}
					disabled={sending}
				>
					<Form.Item
						name="command"
						label={t('iot.gateway.preview.commandContent')}
						rules={[
							{
								required: true,
								whitespace: true,
								message: t(
									'iot.gateway.preview.commandRequired',
								),
							},
						]}
					>
						<Input.TextArea
							rows={5}
							maxLength={commandMaxLength}
							showCount
						/>
					</Form.Item>
					<Space
						direction="vertical"
						size="middle"
						style={{ width: '100%' }}
					>
						<Button
							type="primary"
							htmlType="submit"
							icon={<SendOutlined />}
							loading={sending}
						>
							{t('iot.gateway.preview.send')}
						</Button>
						{commandResult && (
							<Alert
								type={commandResult}
								showIcon
								message={t(
									`iot.gateway.preview.send.${commandResult}`,
								)}
							/>
						)}
					</Space>
				</Form>
			),
		});
	}

	return (
		<Drawer
			open
			title={t('iot.gateway.preview.title')}
			size={800}
			closable={false}
			keyboard={false}
			styles={{ footer: { textAlign: 'right' } }}
			footer={<Button onClick={onClose}>{t('common.close')}</Button>}
		>
			<Descriptions
				size="small"
				column={{ xs: 1, sm: 3 }}
				items={[
					{
						key: 'sn',
						label: t('iot.gateway.sn'),
						children: gateway.sn || '—',
					},
					{
						key: 'name',
						label: t('iot.gateway.name'),
						children: gateway.name || '—',
					},
					{
						key: 'name1',
						label: t('iot.gateway.name'),
						children: gateway.name || '—',
					},
				]}
			/>
			<Tabs items={tabs} />
		</Drawer>
	);
};
