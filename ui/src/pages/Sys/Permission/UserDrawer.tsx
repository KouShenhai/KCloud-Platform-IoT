import {DrawerForm, ProFormRadio, ProFormSelect, ProFormText, ProFormTreeSelect} from '@ant-design/pro-components';
import {Image, message, UploadFile} from 'antd';
import {modifyV3, saveV3} from '@/services/admin/user';
import {v7 as uuidV7} from "uuid";
import React, {useState} from "react";
import {UploadAvatarDrawer} from "@/pages/Sys/Permission/UploadAvatarDrawer";


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

export const UserDrawer: React.FC<UserDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, edit, roleList, deptTreeList, fileList, setFileList }) => {

	const [previewOpen, setPreviewOpen] = useState(false);
	const [previewImage, setPreviewImage] = useState('');

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
					style: {
						display: readOnly ? 'none' : 'inline-block',
					},
				}
			}}
			onFinish={ async (value) => {
				const avatar = fileList.length > 0 ? (fileList[0]?.url ? fileList[0]?.url : fileList[0]?.response?.data) : ""
				const co = {
					id: value?.id,
					username: value.username,
					status: value?.status,
					mail: value?.mail,
					mobile: value?.mobile,
					avatar: avatar ? avatar : "",
				}
				if (value.id === undefined) {
					saveV3({co: co}, uuidV7()).then(res => {
						if (res.code === 'OK') {
							message.success("新增成功").then()
							setModalVisit(false)
							onComponent();
						}
					})
				} else {
					modifyV3({co: co}).then(res => {
						if (res.code === 'OK') {
							message.success("修改成功").then()
							setModalVisit(false)
							onComponent();
						}
					})
				}
			}}>

			<ProFormText
				name="id"
				label="ID"
				hidden={true}
			/>

			<ProFormText
				name="username"
				label="名称"
				tooltip={"密码登录【不允许重复，不允许修改】"}
				disabled={edit}
				readonly={readOnly}
				placeholder={'请输入用户名'}
				rules={[{ required: true, message: '请输入用户名' }]}
			/>

			<ProFormText
				name="mail"
				label="邮箱"
				tooltip={"邮箱登录【不允许重复】"}
				readonly={readOnly}
				placeholder={'请输入邮箱'}
			/>

			<ProFormText
				name="mobile"
				label="手机号"
				tooltip={"手机号登录【不允许重复】"}
				readonly={readOnly}
				placeholder={'请输入手机号'}
			/>

			<ProFormRadio.Group
				name="status"
				label="状态"
				readonly={readOnly}
				rules={[{required: true, message: '请选择状态',}]}
				options={[
					{label:"启用",value: 0 },
					{label:"禁用",value: 1 }
				]}
			/>

			{!readOnly && (
				<UploadAvatarDrawer
					setPreviewImage={setPreviewImage}
					setPreviewOpen={setPreviewOpen}
					fileList={fileList}
					setFileList={setFileList}/>
			)}

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
					readonly={true}
					name="createTime"
					rules={[{ required: true, message: '请输入创建时间' }]}
					label="创建时间"
				/>
			)}

			{readOnly && fileList.length > 0 && (
				<Image
					width={100}
					src={fileList[0].url}
				/>
			)}

		</DrawerForm>
	);
};
