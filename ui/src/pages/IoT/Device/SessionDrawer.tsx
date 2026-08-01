import {
	modifySession,
	saveCSession,
} from '@/services/iot/session';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormDigit,
	ProFormText,
} from '@ant-design/pro-components';
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

type ConnectionFormValues = API.SessionCO & Record<string, any>;

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

const toConnectionCO = (value: ConnectionFormValues): API.SessionCO => {
	return {
		id: value.id,
		name: value.name,
		host: value.host,
		port: value.port,
		username: value.enabled,
		password: value.password,
	};
};

export const SessionDrawer: React.FC<ConnectionDrawerProps> = ({
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
					saveCSession({ co }, requestId)
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
					modifySession({ co })
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
				label={t('iot.session.name')}
				placeholder={t('iot.session.placeholder.name')}
				rules={[
					{
						required: true,
						message: t('iot.session.placeholder.name'),
					},
				]}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="host"
				label={t('iot.session.host')}
				placeholder={t('iot.session.placeholder.host')}
				rules={[
					{
						required: true,
						message: t('iot.session.placeholder.host'),
					},
				]}
			/>

			<ProFormDigit
				disabled={loading}
				readonly={readOnly}
				name="port"
				label={t('iot.session.port')}
				placeholder={t('iot.session.placeholder.port')}
				min={1}
				max={65535}
				fieldProps={{ precision: 0 }}
				rules={[
					{
						required: true,
						message: t('iot.session.placeholder.port'),
					},
				]}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="username"
				label={t('iot.session.username')}
				placeholder={t('iot.session.placeholder.username')}
				rules={[
					{
						required: true,
						message: t('iot.session.placeholder.username'),
					},
				]}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="password"
				label={t('iot.session.password')}
				placeholder={t('iot.session.placeholder.password')}
				rules={[
					{
						required: true,
						message: t('iot.session.placeholder.password'),
					},
				]}
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
