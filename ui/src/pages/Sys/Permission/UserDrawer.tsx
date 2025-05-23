import {DrawerForm, ProFormRadio, ProFormSelect, ProFormText, ProFormTreeSelect} from '@ant-design/pro-components';
import {Image, message, UploadFile} from 'antd';
import {modifyUser, saveUser} from '@/services/admin/user';
import React, {useState} from "react";
import {UploadAvatarDrawer} from "@/pages/Sys/Permission/UploadAvatarDrawer";
import {ProFormItem} from "@ant-design/pro-form";
import {v7 as uuidV7} from "uuid";

interface UserDrawerProps {
	modalVisit: boolean;
	edit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	deptTreeList: any[]
	roleList: any[]
	fileList: UploadFile[]
	setFileList: (fileList: UploadFile[]) => void
	requestId: string
	setRequestId: (requestId: string) => void
}

type TableColumns = {
	id: number;
	username: string | undefined;
	status: number | undefined;
	mail: string | undefined;
	mobile: string | undefined;
	avatar: string | undefined;
	deptIds: string[];
	roleIds: string[];
	createTime: string | undefined;
};

export const UserDrawer: React.FC<UserDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, edit, roleList, deptTreeList, fileList, setFileList, requestId, setRequestId }) => {

	const [previewOpen, setPreviewOpen] = useState(false);
	const [previewImage, setPreviewImage] = useState('');
	const [loading, setLoading] = useState(false)

	return (
		<DrawerForm<TableColumns>
			open={modalVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true
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
				}
			}}
			onFinish={ async (value) => {
				setLoading(true)
				const co = {
					id: value?.id,
					username: value.username,
					status: value?.status,
					mail: value?.mail,
					mobile: value?.mobile,
					avatar: fileList.length > 0 ? (fileList[0]?.url ? fileList[0]?.url : fileList[0]?.response?.data) : "",
				}
				if (value.id === undefined) {
					saveUser({co: co}, requestId).then(res => {
						if (res.code === 'OK') {
							message.success("新增成功").then()
							setModalVisit(false)
							onComponent();
						}
					}).finally(() => {
						setRequestId(uuidV7())
						setLoading(false)
					})
				} else {
					modifyUser({co: co}).then(res => {
						if (res.code === 'OK') {
							message.success("修改成功").then()
							setModalVisit(false)
							onComponent();
						}
					}).finally(() => {
						setLoading(false)
					})
				}
			}}>

			<ProFormText
				disabled={loading}
				name="id"
				label="ID"
				hidden={true}
			/>

			<ProFormText
				name="username"
				label="用户名"
				tooltip={"密码登录【不允许重复，不允许修改】"}
				disabled={edit}
				readonly={readOnly}
				placeholder={'请输入用户名'}
				rules={[{ required: true, message: '请输入用户名' }]}
			/>

			<ProFormText
				disabled={loading}
				name="mail"
				label="用户邮箱"
				tooltip={"邮箱登录【不允许重复】"}
				readonly={readOnly}
				placeholder={'请输入用户邮箱'}
			/>

			<ProFormText
				disabled={loading}
				name="mobile"
				label="用户手机号"
				tooltip={"手机号登录【不允许重复】"}
				readonly={readOnly}
				placeholder={'请输入用户手机号'}
			/>

			{!readOnly && (
				<ProFormItem label={"用户头像"}>
					<UploadAvatarDrawer
						setPreviewImage={setPreviewImage}
						setPreviewOpen={setPreviewOpen}
						fileList={fileList}
						setFileList={setFileList}/>
				</ProFormItem>
			)}

			{readOnly && fileList.length > 0 && (
				<ProFormItem
					label={"用户头像"}>
					<Image width={100} src={fileList[0].url}/>
				</ProFormItem>
			)}

			<ProFormRadio.Group
				disabled={loading}
				name="status"
				label="用户状态"
				readonly={readOnly}
				rules={[{required: true, message: '请选择用户状态',}]}
				options={[
					{label:"启用",value: 0 },
					{label:"禁用",value: 1 }
				]}
			/>

			{previewImage && (
				<Image
					wrapperStyle={{ display: 'none' }}
					preview={{
						visible: previewOpen,
						onVisibleChange: (visible) => setPreviewOpen(visible),
						afterOpenChange: (visible) => !visible && setPreviewImage(''),
					}}
					src={previewImage}/>
			)}

			{ readOnly && (
			<ProFormSelect
				disabled={loading}
				name="roleIds"
				allowClear={true}
				label="所属角色"
				mode={'multiple'}
				readonly={readOnly}
				options={roleList}
				placeholder={'请选择所属角色'}
				rules={[{required: true, message: '请选择所属角色',}]}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
					}
				}}
			/>
			)}

			{ readOnly && (
			<ProFormTreeSelect
				disabled={loading}
				name="deptIds"
				label="所属部门"
				readonly={readOnly}
				allowClear={true}
				placeholder={'请选择所属部门'}
				rules={[{ required: true, message: '请选择所属部门' }]}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
						children: 'children'
					}
				}}
				request={async () => {
					return deptTreeList
				}}
			/>
			)}

			{ readOnly && (
				<ProFormText
					disabled={loading}
					readonly={true}
					name="createTime"
					rules={[{ required: true, message: '请输入创建时间' }]}
					label="创建时间"
				/>
			)}

		</DrawerForm>
	);
};
