import {
	modifyConnection,
	saveConnection,
} from '@/services/network/connection';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormDigit,
	ProFormSelect,
	ProFormSwitch,
	ProFormText,
} from '@ant-design/pro-components';
import { ProFormDependency, ProFormTextArea } from '@ant-design/pro-form';
import { message } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface ConnectionDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: ConnectionFormValues;
	onComponent: () => void;
	requestId: string;
	setRequestId: (requestId: string) => void;
}

type ConnectionFormValues = API.ConnectionCO & Record<string, any>;

const TYPE_CONFIG_FIELDS: Record<number, string[]> = {
	1: [
		'auth',
		'username',
		'password',
		'maxMessageSize',
		'isAutoClientId',
		'useWebSocket',
		'tcpNoDelay',
		'tcpKeepAlive',
		'ssl',
	],
	2: [
		'auth',
		'compressionSupported',
		'compressionLevel',
		'maxHeaderSize',
		'maxWebSocketFrameSize',
		'http2ClearTextEnabled',
	],
	3: [
		'auth',
		'subscribe',
		'username',
		'password',
		'clientId',
		'clearSession',
		'keepAliveInterval',
		'autoKeepAlive',
		'reconnectInterval',
	],
	4: [
		'bootstrapServers',
		'clientId',
		'groupId',
		'topic',
		'acks',
		'autoOffsetReset',
		'enableAutoCommit',
		'securityProtocol',
		'username',
		'password',
	],
	5: [
		'virtualHost',
		'username',
		'password',
		'exchange',
		'routingKey',
		'queue',
		'requestedHeartbeat',
		'automaticRecoveryEnabled',
		'ssl',
		'trustAll',
	],
};

const parseConfig = (config?: string) => {
	if (!config) {
		return {};
	}
	try {
		const value = JSON.parse(config);
		return value && typeof value === 'object' && !Array.isArray(value)
			? value
			: {};
	} catch {
		return {};
	}
};

const toFormValues = (dataSource: ConnectionFormValues) => {
	return {
		...dataSource,
		...parseConfig(dataSource?.config),
	};
};

const toConnectionCO = (value: ConnectionFormValues): API.ConnectionCO => {
	const type = Number(value.type);
	const config = (TYPE_CONFIG_FIELDS[type] || []).reduce<Record<string, any>>(
		(result, field) => {
			const item = value[field];
			if (item !== undefined && item !== null && item !== '') {
				result[field] = item;
			}
			return result;
		},
		{},
	);
	return {
		id: value.id,
		name: value.name,
		type,
		host: value.host,
		port: value.port,
		enabled: value.enabled,
		config: JSON.stringify(config),
		remark: value.remark,
	};
};

export const ConnectionDrawer: React.FC<ConnectionDrawerProps> = ({
	modalVisit,
	setModalVisit,
	title,
	readOnly,
	dataSource,
	onComponent,
	requestId,
	setRequestId,
}) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [loading, setLoading] = useState(false);

	const typeOptions = [
		{ value: 1, label: t('network.connection.type.mqttServer') },
		{ value: 2, label: t('network.connection.type.httpServer') },
		{ value: 3, label: t('network.connection.type.mqttClient') },
		{ value: 4, label: t('network.connection.type.kafka') },
		{ value: 5, label: t('network.connection.type.rabbitmq') },
	];

	const enabledOptions = [
		{ value: 0, label: t('network.connection.enabled.enabled') },
		{ value: 1, label: t('network.connection.enabled.disabled') },
	];

	const switchProps = {
		disabled: loading || readOnly,
		checkedChildren: t('common.yes'),
		unCheckedChildren: t('common.no'),
	};

	const renderConfigFields = (type: number | undefined) => {
		switch (Number(type)) {
			case 1:
				return (
					<>
						<ProFormSwitch
							{...switchProps}
							name="auth"
							label={t('network.connection.config.auth')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="username"
							label={t('network.connection.config.username')}
							placeholder={t('network.connection.placeholder.username')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="password"
							label={t('network.connection.config.password')}
							placeholder={t('network.connection.placeholder.password')}
						/>
						<ProFormDigit
							disabled={loading}
							readonly={readOnly}
							name="maxMessageSize"
							label={t('network.connection.config.maxMessageSize')}
							min={1}
							fieldProps={{ precision: 0 }}
						/>
						<ProFormSwitch
							{...switchProps}
							name="isAutoClientId"
							label={t('network.connection.config.isAutoClientId')}
						/>
						<ProFormSwitch
							{...switchProps}
							name="useWebSocket"
							label={t('network.connection.config.useWebSocket')}
						/>
						<ProFormSwitch
							{...switchProps}
							name="tcpNoDelay"
							label={t('network.connection.config.tcpNoDelay')}
						/>
						<ProFormSwitch
							{...switchProps}
							name="tcpKeepAlive"
							label={t('network.connection.config.tcpKeepAlive')}
						/>
						<ProFormSwitch
							{...switchProps}
							name="ssl"
							label={t('network.connection.config.ssl')}
						/>
					</>
				);
			case 2:
				return (
					<>
						<ProFormSwitch
							{...switchProps}
							name="auth"
							label={t('network.connection.config.auth')}
						/>
						<ProFormSwitch
							{...switchProps}
							name="compressionSupported"
							label={t('network.connection.config.compressionSupported')}
						/>
						<ProFormDigit
							disabled={loading}
							readonly={readOnly}
							name="compressionLevel"
							label={t('network.connection.config.compressionLevel')}
							min={0}
							max={9}
							fieldProps={{ precision: 0 }}
						/>
						<ProFormDigit
							disabled={loading}
							readonly={readOnly}
							name="maxHeaderSize"
							label={t('network.connection.config.maxHeaderSize')}
							min={1}
							fieldProps={{ precision: 0 }}
						/>
						<ProFormDigit
							disabled={loading}
							readonly={readOnly}
							name="maxWebSocketFrameSize"
							label={t('network.connection.config.maxWebSocketFrameSize')}
							min={1}
							fieldProps={{ precision: 0 }}
						/>
						<ProFormSwitch
							{...switchProps}
							name="http2ClearTextEnabled"
							label={t('network.connection.config.http2ClearTextEnabled')}
						/>
					</>
				);
			case 3:
				return (
					<>
						<ProFormSwitch
							{...switchProps}
							name="auth"
							label={t('network.connection.config.auth')}
						/>
						<ProFormSwitch
							{...switchProps}
							name="subscribe"
							label={t('network.connection.config.subscribe')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="username"
							label={t('network.connection.config.username')}
							placeholder={t('network.connection.placeholder.username')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="password"
							label={t('network.connection.config.password')}
							placeholder={t('network.connection.placeholder.password')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="clientId"
							label={t('network.connection.config.clientId')}
							placeholder={t('network.connection.placeholder.clientId')}
						/>
						<ProFormSwitch
							{...switchProps}
							name="clearSession"
							label={t('network.connection.config.clearSession')}
						/>
						<ProFormDigit
							disabled={loading}
							readonly={readOnly}
							name="keepAliveInterval"
							label={t('network.connection.config.keepAliveInterval')}
							min={1}
							fieldProps={{ precision: 0 }}
						/>
						<ProFormSwitch
							{...switchProps}
							name="autoKeepAlive"
							label={t('network.connection.config.autoKeepAlive')}
						/>
						<ProFormDigit
							disabled={loading}
							readonly={readOnly}
							name="reconnectInterval"
							label={t('network.connection.config.reconnectInterval')}
							min={0}
							fieldProps={{ precision: 0 }}
						/>
					</>
				);
			case 4:
				return (
					<>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="bootstrapServers"
							label={t('network.connection.config.bootstrapServers')}
							placeholder={t('network.connection.placeholder.bootstrapServers')}
							rules={[
								{
									required: true,
									message: t(
										'network.connection.required.bootstrapServers',
									),
								},
							]}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="clientId"
							label={t('network.connection.config.clientId')}
							placeholder={t('network.connection.placeholder.clientId')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="groupId"
							label={t('network.connection.config.groupId')}
							placeholder={t('network.connection.placeholder.groupId')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="topic"
							label={t('network.connection.config.topic')}
							placeholder={t('network.connection.placeholder.topic')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="acks"
							label={t('network.connection.config.acks')}
						/>
						<ProFormSelect
							disabled={loading}
							readonly={readOnly}
							name="autoOffsetReset"
							label={t('network.connection.config.autoOffsetReset')}
							options={[
								{ value: 'latest', label: 'latest' },
								{ value: 'earliest', label: 'earliest' },
								{ value: 'none', label: 'none' },
							]}
						/>
						<ProFormSwitch
							{...switchProps}
							name="enableAutoCommit"
							label={t('network.connection.config.enableAutoCommit')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="securityProtocol"
							label={t('network.connection.config.securityProtocol')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="username"
							label={t('network.connection.config.username')}
							placeholder={t('network.connection.placeholder.username')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="password"
							label={t('network.connection.config.password')}
							placeholder={t('network.connection.placeholder.password')}
						/>
					</>
				);
			case 5:
				return (
					<>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="virtualHost"
							label={t('network.connection.config.virtualHost')}
							placeholder={t('network.connection.placeholder.virtualHost')}
							rules={[
								{
									required: true,
									message: t('network.connection.required.virtualHost'),
								},
							]}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="username"
							label={t('network.connection.config.username')}
							placeholder={t('network.connection.placeholder.username')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="password"
							label={t('network.connection.config.password')}
							placeholder={t('network.connection.placeholder.password')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="exchange"
							label={t('network.connection.config.exchange')}
							placeholder={t('network.connection.placeholder.exchange')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="routingKey"
							label={t('network.connection.config.routingKey')}
							placeholder={t('network.connection.placeholder.routingKey')}
						/>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="queue"
							label={t('network.connection.config.queue')}
							placeholder={t('network.connection.placeholder.queue')}
						/>
						<ProFormDigit
							disabled={loading}
							readonly={readOnly}
							name="requestedHeartbeat"
							label={t('network.connection.config.requestedHeartbeat')}
							min={0}
							fieldProps={{ precision: 0 }}
						/>
						<ProFormSwitch
							{...switchProps}
							name="automaticRecoveryEnabled"
							label={t('network.connection.config.automaticRecoveryEnabled')}
						/>
						<ProFormSwitch
							{...switchProps}
							name="ssl"
							label={t('network.connection.config.ssl')}
						/>
						<ProFormSwitch
							{...switchProps}
							name="trustAll"
							label={t('network.connection.config.trustAll')}
						/>
					</>
				);
			default:
				return null;
		}
	};

	return (
		<DrawerForm<ConnectionFormValues>
			open={modalVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
			}}
			initialValues={toFormValues(dataSource)}
			onOpenChange={setModalVisit}
			autoFocusFirstInput
			submitter={{
				submitButtonProps: {
					disabled: loading,
					style: {
						display: readOnly ? 'none' : 'inline-block',
					},
				},
			}}
			onFinish={async (value) => {
				setLoading(true);
				const co = toConnectionCO(value);
				if (value.id === undefined || value.id === null) {
					saveConnection({ co }, requestId)
						.then((res) => {
							if (res.code === 'OK') {
								message.success(t('toast.saveSuccess')).then();
								setModalVisit(false);
								onComponent();
							}
						})
						.finally(() => {
							setRequestId(uuidV7());
							setLoading(false);
						});
				} else {
					modifyConnection({ co })
						.then((res) => {
							if (res.code === 'OK') {
								message.success(t('toast.modifySuccess')).then();
								setModalVisit(false);
								onComponent();
							}
						})
						.finally(() => {
							setLoading(false);
						});
				}
			}}
		>
			<ProFormText
				disabled={loading}
				name="id"
				label="ID"
				hidden={true}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="name"
				label={t('network.connection.name')}
				placeholder={t('network.connection.placeholder.name')}
				rules={[
					{
						required: true,
						message: t('network.connection.required.name'),
					},
				]}
			/>

			<ProFormSelect
				disabled={loading}
				readonly={
					readOnly ||
					(dataSource?.id !== undefined && dataSource?.id !== null)
				}
				name="type"
				label={t('network.connection.type')}
				placeholder={t('network.connection.placeholder.type')}
				rules={[
					{
						required: true,
						message: t('network.connection.required.type'),
					},
				]}
				options={typeOptions}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="host"
				label={t('network.connection.host')}
				placeholder={t('network.connection.placeholder.host')}
			/>

			<ProFormDigit
				disabled={loading}
				readonly={readOnly}
				name="port"
				label={t('network.connection.port')}
				placeholder={t('network.connection.placeholder.port')}
				min={1}
				max={65535}
				fieldProps={{ precision: 0 }}
			/>

			<ProFormSelect
				disabled={loading}
				readonly={readOnly}
				name="enabled"
				label={t('network.connection.enabled')}
				placeholder={t('network.connection.placeholder.enabled')}
				rules={[
					{
						required: true,
						message: t('network.connection.required.enabled'),
					},
				]}
				options={enabledOptions}
			/>

			<ProFormDependency name={['type']}>
				{({ type }) => renderConfigFields(type)}
			</ProFormDependency>

			<ProFormTextArea
				disabled={loading}
				readonly={readOnly}
				name="remark"
				label={t('network.connection.remark')}
			/>

			{readOnly && (
				<ProFormText
					disabled={loading}
					readonly={true}
					name="createTime"
					label={t('common.createTime')}
				/>
			)}
		</DrawerForm>
	);
};
