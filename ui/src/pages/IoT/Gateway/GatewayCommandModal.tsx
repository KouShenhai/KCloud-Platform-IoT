import {
	readGatewayProperty,
	rebootGateway,
	writeGatewayProperty,
} from '@/services/iot/gateway';
import { useIntl } from '@@/exports';
import {
	ModalForm,
	ProFormList,
	ProFormSelect,
	ProFormText,
} from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface GatewayCommandModalProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	gateway: any;
	requestId: string;
	setRequestId: (requestId: string) => void;
}

const COMMAND_REBOOT = 1;
const COMMAND_READ_PROPERTY = 2;
const COMMAND_WRITE_PROPERTY = 3;

export const GatewayCommandModal: React.FC<GatewayCommandModalProps> = ({
	modalVisit,
	setModalVisit,
	gateway,
	requestId,
	setRequestId,
}) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [loading, setLoading] = useState(false);
	const [commandType, setCommandType] = useState<number>(COMMAND_REBOOT);

	const commandOptions = [
		{ value: COMMAND_REBOOT, label: t('iot.gateway.command.reboot') },
		{
			value: COMMAND_READ_PROPERTY,
			label: t('iot.gateway.command.readProperty'),
		},
		{
			value: COMMAND_WRITE_PROPERTY,
			label: t('iot.gateway.command.writeProperty'),
		},
	];

	const onFinishReboot = () => {
		return rebootGateway({ gatewayId: gateway?.id }, requestId);
	};

	const onFinishReadProperty = (value: any) => {
		const identifiers = (value?.identifiers || [])
			.map((item: any) => item?.identifier)
			.filter((item: any) => item !== undefined && item !== null && item !== '');
		return readGatewayProperty(
			{
				gatewayId: gateway?.id,
				deviceKey: value?.deviceKey,
				identifiers,
			},
			requestId,
		);
	};

	const onFinishWriteProperty = (value: any) => {
		const properties = (value?.properties || []).reduce(
			(result: Record<string, any>, item: any) => {
				if (item?.key !== undefined && item?.key !== null && item?.key !== '') {
					result[item.key] = item?.value;
				}
				return result;
			},
			{},
		);
		return writeGatewayProperty(
			{
				gatewayId: gateway?.id,
				deviceKey: value?.deviceKey,
				properties,
			},
			requestId,
		);
	};

	return (
		<ModalForm
			open={modalVisit}
			title={t('iot.gateway.command')}
			modalProps={{
				destroyOnClose: true,
				maskClosable: false,
			}}
			initialValues={{ commandType: COMMAND_REBOOT }}
			onOpenChange={(visible) => {
				setModalVisit(visible);
				if (visible) {
					setCommandType(COMMAND_REBOOT);
				}
			}}
			submitter={{
				submitButtonProps: {
					disabled: loading,
				},
			}}
			onFinish={async (value) => {
				setLoading(true);
				let promise;
				if (commandType === COMMAND_READ_PROPERTY) {
					promise = onFinishReadProperty(value);
				} else if (commandType === COMMAND_WRITE_PROPERTY) {
					promise = onFinishWriteProperty(value);
				} else {
					promise = onFinishReboot();
				}
				return promise
					.then((res: any) => {
						if (res.code === 'OK') {
							message
								.success(t('iot.gateway.command.sendSuccess', { id: res?.data }))
								.then();
							setModalVisit(false);
							return true;
						}
						return false;
					})
					.finally(() => {
						setRequestId(uuidV7());
						setLoading(false);
					});
			}}
		>
			<ProFormText
				readonly={true}
				name="gatewayName"
				label={t('iot.gateway.name')}
				initialValue={gateway?.name}
			/>

			<ProFormSelect
				name="commandType"
				label={t('iot.gateway.command.type')}
				placeholder={t('iot.gateway.command.placeholder.type')}
				options={commandOptions}
				allowClear={false}
				fieldProps={{
					onChange: (value: number) => setCommandType(value),
				}}
				rules={[
					{
						required: true,
						message: t('iot.gateway.command.required.type'),
					},
				]}
			/>

			{commandType !== COMMAND_REBOOT && (
				<ProFormText
					name="deviceKey"
					label={t('iot.gateway.command.deviceKey')}
					placeholder={t('iot.gateway.command.placeholder.deviceKey')}
					rules={[
						{
							required: true,
							message: t('iot.gateway.command.required.deviceKey'),
						},
					]}
				/>
			)}

			{commandType === COMMAND_READ_PROPERTY && (
				<ProFormList
					name="identifiers"
					label={t('iot.gateway.command.identifiers')}
					creatorButtonProps={{
						creatorButtonText: t('iot.gateway.command.addIdentifier'),
					}}
					rules={[
						{
							required: true,
							validator: async (_, value) => {
								if (!value || value.length === 0) {
									return Promise.reject(
										new Error(t('iot.gateway.command.required.identifiers')),
									);
								}
								return Promise.resolve();
							},
						},
					]}
				>
					<ProFormText
						name="identifier"
						placeholder={t('iot.gateway.command.placeholder.identifier')}
						rules={[
							{
								required: true,
								message: t('iot.gateway.command.required.identifier'),
							},
						]}
					/>
				</ProFormList>
			)}

			{commandType === COMMAND_WRITE_PROPERTY && (
				<ProFormList
					name="properties"
					label={t('iot.gateway.command.properties')}
					creatorButtonProps={{
						creatorButtonText: t('iot.gateway.command.addProperty'),
					}}
					rules={[
						{
							required: true,
							validator: async (_, value) => {
								if (!value || value.length === 0) {
									return Promise.reject(
										new Error(t('iot.gateway.command.required.properties')),
									);
								}
								return Promise.resolve();
							},
						},
					]}
				>
					<ProFormText
						name="key"
						placeholder={t('iot.gateway.command.placeholder.propertyKey')}
						rules={[
							{
								required: true,
								message: t('iot.gateway.command.required.propertyKey'),
							},
						]}
					/>
					<ProFormText
						name="value"
						placeholder={t('iot.gateway.command.placeholder.propertyValue')}
						rules={[
							{
								required: true,
								message: t('iot.gateway.command.required.propertyValue'),
							},
						]}
					/>
				</ProFormList>
			)}
		</ModalForm>
	);
};
