import { modifyGateway, saveGateway } from '@/services/iot/gateway';
import { useIntl } from '@@/exports';
import {
	DrawerForm, ProFormSelect,
	ProFormText,
} from '@ant-design/pro-components';
import { ProFormTextArea } from '@ant-design/pro-form';
import { message } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface GatewayDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	requestId: string;
	sessionOptions: any[]
	setRequestId: (requestId: string) => void;
}

type TableColumns = {
	id: number;
	sn: string | undefined;
	name: string | undefined;
	sessionId: string | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

export const GatewayDrawer: React.FC<GatewayDrawerProps> = ({
	modalVisit,
	setModalVisit,
	title,
	readOnly,
	dataSource,
	onComponent,
	requestId,
	setRequestId,
	sessionOptions,
}) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [loading, setLoading] = useState(false);

	return (
		<DrawerForm<TableColumns>
			open={modalVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
			}}
			initialValues={dataSource}
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
				if (value.id === undefined) {
					saveGateway({ co: value }, requestId)
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
					modifyGateway({ co: value })
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
				name="sn"
				label={t('iot.gateway.sn')}
				placeholder={t('iot.gateway.placeholder.sn')}
				rules={[
					{
						required: true,
						message: t('iot.gateway.required.sn'),
					},
				]}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="name"
				label={t('iot.gateway.name')}
				placeholder={t('iot.gateway.placeholder.name')}
				rules={[
					{
						required: true,
						message: t('iot.gateway.required.name'),
					},
				]}
			/>

			<ProFormSelect
				disabled={loading}
				readonly={readOnly}
				name="sessionId"
				label="绑定会话"
				options={sessionOptions}
				rules={[
					{
						required: true,
						message: "请选择会话",
					}
				]}
			/>

			<ProFormTextArea
				disabled={loading}
				readonly={readOnly}
				name="remark"
				label={t('iot.gateway.remark')}
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
