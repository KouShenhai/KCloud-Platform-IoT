import { UploadAvatarDrawer } from '@/pages/Sys/Permission/UploadAvatarDrawer';
import { modifyUser, saveUser } from '@/services/admin/user';
import { useAccess, useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormRadio,
	ProFormSelect,
	ProFormText,
	ProFormTreeSelect,
} from '@ant-design/pro-components';
import { ProFormItem } from '@ant-design/pro-form';
import { Image, message, UploadFile } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface UserDrawerProps {
	modalVisit: boolean;
	edit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	saveOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	deptTreeList: any[];
	roleList: any[];
	fileList: UploadFile[];
	setFileList: (fileList: UploadFile[]) => void;
	requestId: string;
	setRequestId: (requestId: string) => void;
	logId: number;
	setLogId: (logId: number) => void;
}

type TableColumns = {
	id: number;
	username: string | undefined;
	password: string | undefined;
	status: number | undefined;
	mail: string | undefined;
	mobile: string | undefined;
	avatar: string | undefined;
	deptId: number;
	roleIds: string[];
	createTime: string | undefined;
};

export const UserDrawer: React.FC<UserDrawerProps> = ({
	modalVisit,
	setModalVisit,
	title,
	readOnly,
	dataSource,
	onComponent,
	edit,
	roleList,
	deptTreeList,
	fileList,
	setFileList,
	requestId,
	setRequestId,
	logId,
	setLogId,
	saveOnly
}) => {
	const access = useAccess();
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [previewOpen, setPreviewOpen] = useState(false);
	const [previewImage, setPreviewImage] = useState('');
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
				const co = {
					id: value?.id,
					username: value.username,
					password: value?.password,
					status: value?.status,
					mail: value?.mail,
					mobile: value?.mobile,
					avatar: logId,
					deptId: value?.deptId,
				};
				if (value.id === undefined) {
					// @ts-ignore
					saveUser({ co: co }, requestId)
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
					// @ts-ignore
					modifyUser({ co: co })
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
				name="username"
				label={t('user.username')}
				tooltip={t('user.tooltip.username')}
				disabled={edit}
				readonly={readOnly}
				placeholder={t('user.placeholder.username')}
				rules={[
					{ required: true, message: t('user.required.username') },
				]}
			/>

			{ saveOnly && (<ProFormText.Password
				initialValue={'laokou123'}
				name="password"
				label={t('user.password')}
				tooltip={t('user.tooltip.password')}
				placeholder={t('user.placeholder.password')}
				rules={[
					{ required: true, message: t('user.required.password') },
				]}
			/>)}

			<ProFormText
				disabled={loading}
				name="mail"
				label={t('user.mail')}
				tooltip={t('user.tooltip.mail')}
				readonly={readOnly}
				placeholder={t('user.placeholder.mail')}
			/>

			<ProFormText
				disabled={loading}
				name="mobile"
				label={t('user.mobile')}
				tooltip={t('user.tooltip.mobile')}
				readonly={readOnly}
				placeholder={t('user.placeholder.mobile')}
			/>

			<ProFormTreeSelect
				disabled={loading}
				readonly={readOnly}
				name="deptId"
				label={t('user.dept')}
				allowClear={true}
				placeholder={t('user.placeholder.dept')}
				rules={[
					{ required: true, message: t('user.required.dept') },
				]}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
						children: 'children',
					},
				}}
				request={async () => {
					return deptTreeList;
				}}
			/>

			{!readOnly &&
				(access.canUserModify || access.canUserModify) &&
				access.canOssUpload && (
					<ProFormItem label={t('user.avatar')}>
						<UploadAvatarDrawer
							setPreviewImage={setPreviewImage}
							setPreviewOpen={setPreviewOpen}
							fileList={fileList}
							setLogId={setLogId}
							setFileList={setFileList}
						/>
					</ProFormItem>
				)}

			{readOnly && fileList.length > 0 && (
				<ProFormItem label={t('user.avatar')}>
					<Image width={100} src={fileList[0].url} />
				</ProFormItem>
			)}

			<ProFormRadio.Group
				disabled={loading}
				name="status"
				label={t('user.status')}
				readonly={readOnly}
				rules={[
					{ required: true, message: t('user.required.status') },
				]}
				options={[
					{ label: t('common.enable'), value: 0 },
					{ label: t('common.disable'), value: 1 },
				]}
			/>

			{previewImage && (
				<Image
					wrapperStyle={{ display: 'none' }}
					preview={{
						visible: previewOpen,
						onVisibleChange: (visible) => setPreviewOpen(visible),
						afterOpenChange: (visible) =>
							!visible && setPreviewImage(''),
					}}
					src={previewImage}
				/>
			)}

			{readOnly && (
				<ProFormSelect
					disabled={loading}
					name="roleIds"
					allowClear={true}
					label={t('user.roles')}
					mode={'multiple'}
					readonly={readOnly}
					options={roleList}
					placeholder={t('user.placeholder.roles')}
					rules={[
						{ required: true, message: t('user.required.roles') },
					]}
					fieldProps={{
						fieldNames: {
							label: 'name',
							value: 'id',
						},
					}}
				/>
			)}

			{readOnly && (
				<ProFormText
					disabled={loading}
					readonly={true}
					name="createTime"
					rules={[
						{
							required: true,
							message: t('role.validate.createTimeRequired'),
						},
					]}
					label={t('common.createTime')}
				/>
			)}
		</DrawerForm>
	);
};
