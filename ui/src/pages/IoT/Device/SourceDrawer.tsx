import {
	modifySource,
	saveSource,
} from '@/services/iot/source';
import {
	DrawerForm, ProFormSelect,
	ProFormText,
} from '@ant-design/pro-components';
import { Form, message } from 'antd';
import React, { useEffect, useState } from 'react';
import {v7 as uuidV7} from "uuid";
import { useIntl } from '@@/exports';

interface SourceDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visit: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: API.SourceCO;
	onComponent: () => void;
	sourceOptions: any[]
	requestId: string;
	setRequestId: (requestId: string) => void;
}

export const SourceDrawer = (props: SourceDrawerProps) => {
	const { modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, sourceOptions, setRequestId, requestId } =
		props;
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [form] = Form.useForm<API.SourceCO>();
	const [loading, setLoading] = useState(false);

	useEffect(() => {
		if (modalVisit) {
			form.setFieldsValue(dataSource);
		}
	}, [modalVisit, dataSource, form]);

	return (
		<DrawerForm
			title={title}
			form={form}
			open={modalVisit}
			onOpenChange={setModalVisit}
			autoFocusFirstInput
			drawerProps={{
				destroyOnClose: true,
				maskClosable: false,
			}}
			onFinish={async (values) => {
				setLoading(true);
				if (dataSource?.id === undefined) {
					saveSource({ co: { ...dataSource, ...values } }, requestId)
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
					modifySource({ co: { ...dataSource, ...values } })
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
				name="name"
				label="数据源名称"
				rules={[{ required: true, message: '请输入数据源名称' }]}
				disabled={loading}
				readonly={readOnly}
			/>
			<ProFormSelect
				name="type"
				label="数据源类型"
				rules={[{ required: true, message: '请选择数据源类型' }]}
				disabled={loading || dataSource?.id !== undefined}
				readonly={readOnly}
				options={sourceOptions}
			/>
			<ProFormText
				name="endpoint"
				label="数据源地址"
				rules={[{ required: true, message: '请输入数据源地址' }]}
				disabled={loading || dataSource?.id !== undefined}
				readonly={readOnly}
			/>
			<ProFormText
				name="username"
				label="数据源用户名"
				rules={[{ required: true, message: '请输入数据源用户名' }]}
				disabled={loading || dataSource?.id !== undefined}
				readonly={readOnly}
			/>
			<ProFormText.Password
				name="password"
				label="数据源密码"
				rules={[{ required: true, message: '请输入数据源密码' }]}
				disabled={loading || dataSource?.id !== undefined}
				readonly={readOnly}
			/>
			<ProFormText
				name="dbName"
				label="数据源数据库名称"
				rules={[{ required: true, message: '请输入数据源数据库名称' }]}
				disabled={loading || dataSource?.id !== undefined}
				readonly={readOnly}
			/>
		</DrawerForm>
	);
};
