import {DrawerForm, ProFormSelect, ProFormText, ProFormTreeSelect} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyAuthorityUser} from '@/services/admin/user';
import React, {useState} from "react";

interface UserAuthorityProps {
	modalModifyAuthorityVisit: boolean;
	setModalModifyAuthorityVisit: (visible: boolean) => void;
	title: string;
	dataSource: TableColumns;
	onComponent: () => void;
	deptTreeList: any[]
	roleList: any[]
}

type TableColumns = {
	id: number;
	username: string | undefined;
	deptIds: string[];
	roleIds: string[];
};



export const UserModifyAuthorityDrawer: React.FC<UserAuthorityProps> = ({ modalModifyAuthorityVisit, setModalModifyAuthorityVisit, title, dataSource, onComponent, roleList, deptTreeList }) => {

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
				const deptIds = value?.deptIds.map((item: any) => item?.value ? item?.value : item)
				const co = {
					id: value?.id,
					deptIds: deptIds,
					roleIds: value?.roleIds,
				}
				modifyAuthorityUser({co: co}).then(res => {
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
				tooltip={"密码登录【不允许重复，不允许修改】"}
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

			<ProFormTreeSelect
				disabled={loading}
				name="deptIds"
				label="所属部门"
				allowClear={true}
				placeholder={'请选择所属部门'}
				rules={[{ required: true, message: '请选择所属部门' }]}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
						children: 'children'
					},
					// 最多显示多少个 tag，响应式模式会对性能产生损耗
					maxTagCount: 6,
					// 多选
					multiple: true,
					// 显示复选框
					treeCheckable: true,
					// 展示策略
					showCheckedStrategy: 'SHOW_ALL',
					// 取消父子节点联动
					treeCheckStrictly: true,
					// 默认展示所有节点
					treeDefaultExpandAll: true,
					// 高度
					dropdownStyle: { maxHeight: 600 },
					// 不显示搜索
					showSearch: false,
					// 高度
					listHeight: 550
				}}
				request={async () => {
					return deptTreeList
				}}
			/>

		</DrawerForm>
	);
};
