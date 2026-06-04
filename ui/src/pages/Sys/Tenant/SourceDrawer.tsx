import {
	modifySource,
	saveSource,
	testSource,
} from '@/services/admin/source';
import {
	DrawerForm,
	ProFormText,
} from '@ant-design/pro-components';
import { Button, Form, message } from 'antd';
import { useEffect, useState } from 'react';

interface SourceDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visit: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: API.SourceCO;
	onComponent: () => void;
}

export const SourceDrawer = (props: SourceDrawerProps) => {
	const { modalVisit, setModalVisit, title, readOnly, dataSource, onComponent } =
		props;
	const [form] = Form.useForm<API.SourceCO>();
	const [testLoading, setTestLoading] = useState(false);

	useEffect(() => {
		if (modalVisit) {
			form.setFieldsValue(dataSource);
		}
	}, [modalVisit, dataSource, form]);

	const onTestConnection = async () => {
		try {
			const values = await form.validateFields();
			setTestLoading(true);
			const res = await testSource({ co: { ...dataSource, ...values } });
			if (res.code === 'OK') {
				message.success('连接成功');
			} else {
				message.error('连接失败: ' + res.msg);
			}
		} catch (error) {
			console.error(error);
		} finally {
			setTestLoading(false);
		}
	};

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
			submitter={{
				render: (props, defaultDoms) => {
					return [
						<Button
							key="test"
							loading={testLoading}
							onClick={onTestConnection}
						>
							测试连接
						</Button>,
						...defaultDoms,
					];
				},
			}}
			onFinish={async (values) => {
				const isUpdate = dataSource?.id !== undefined;
				const payload = { co: { ...dataSource, ...values } };
				const res = isUpdate
					? await modifySource(payload)
					: await saveSource(payload);
				if (res.code === 'OK') {
					message.success('保存成功');
					setModalVisit(false);
					onComponent();
					return true;
				}
				return false;
			}}
		>
			<ProFormText
				name="name"
				label="数据源名称"
				rules={[{ required: true, message: '请输入数据源名称' }]}
				disabled={readOnly}
			/>
			<ProFormText
				name="driverClassName"
				label="驱动名称"
				rules={[{ required: true, message: '请输入驱动名称' }]}
				disabled={readOnly}
				tooltip="TDengine 默认为 com.taosdata.jdbc.rs.RestfulDriver"
			/>
			<ProFormText
				name="url"
				label="连接地址"
				rules={[{ required: true, message: '请输入连接地址' }]}
				disabled={readOnly}
				placeholder="例如: jdbc:TAOS-RS://127.0.0.1:6041/log"
			/>
			<ProFormText
				name="username"
				label="用户名"
				rules={[{ required: true, message: '请输入用户名' }]}
				disabled={readOnly}
			/>
			<ProFormText.Password
				name="password"
				label="密码"
				rules={[{ required: true, message: '请输入密码' }]}
				disabled={readOnly}
			/>
		</DrawerForm>
	);
};
