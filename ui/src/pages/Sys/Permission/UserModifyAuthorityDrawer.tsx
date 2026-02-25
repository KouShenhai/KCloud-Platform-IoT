import {DrawerForm, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyUserAuthority} from '@/services/admin/user';
import React, {useState} from "react";

interface UserAuthorityProps {
	modalModifyAuthorityVisit: boolean;
	setModalModifyAuthorityVisit: (visible: boolean) => void;
	title: string;
	dataSource: TableColumns;
	onComponent: () => void;
	roleList: any[]
}

type TableColumns = {
	id: number;
	username: string | undefined;
	deptId: number;
	roleIds: string[];
};



export const UserModifyAuthorityDrawer: React.FC<UserAuthorityProps> = ({ modalModifyAuthorityVisit, setModalModifyAuthorityVisit, title, dataSource, onComponent, roleList }) => {

	const [loading, setLoading] = useState(false)

	return (
		<DrawerForm<TableColumns>
			open={modalModifyAuthorityVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true
			}}
			initialValues={dataSource}
			onOpenChange={setModalModifyAuthorityVisit}
			autoFocusFirstInput
			submitter={{
				submitButtonProps: {
					disabled: loading,
					style: {
						display: 'inline-block',
					},
				}
			}}
			onFinish={ async (value) => {
				setLoading(true)
				const co = {
					id: value?.id,
					deptId: value?.deptId,
					roleIds: value?.roleIds,
				}
				modifyUserAuthority({co: co}).then(res => {
					if (res.code === 'OK') {
						message.success("分配权限成功").then()
						setModalModifyAuthorityVisit(false)
						onComponent();
					}
				}).finally(() => {
					setLoading(false)
				})
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
				tooltip={"用户名【不允许重复，不允许修改】"}
				disabled={true}
				placeholder={'请输入用户名'}
				rules={[{ required: true, message: '请输入用户名' }]}
			/>

			<ProFormSelect
				disabled={loading}
				name="roleIds"
				allowClear={true}
				label="所属角色"
				mode={'multiple'}
				placeholder={'请选择所属角色'}
				rules={[{ required: true, message: '请选择所属角色' }]}
				options={roleList}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
					},
				}}
			/>

		</DrawerForm>
	);
};
