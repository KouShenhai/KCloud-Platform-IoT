import {DrawerForm, ProFormSelect, ProFormText, ProFormTreeSelect} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyV3, saveV3} from '@/services/admin/user';
import {v7 as uuidV7} from "uuid";
import {treeListV3} from "@/services/admin/dept";
import {pageV3 as rolePageV3} from "@/services/admin/role";
import {useEffect, useState} from "react";

interface UserCreateOrModifyProps {
	modalVisit: boolean;
	edit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
}

type TableColumns = {
	id: number;
	username: string | undefined;
	status: number | undefined;
	password: string | undefined;
	mail: string | undefined;
	mobile: string | undefined;
	createTime: string | undefined;
	superAdmin: number | undefined;
	avatar: string | undefined;
	deptIds: string[];
	roleIds: string[];
};



export const UserCreateOrModifyDrawer: React.FC<UserCreateOrModifyProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, edit }) => {

	const [deptTreeList, setDeptTreeList] = useState<any[]>([])
	const [roleList, setRoleList] = useState<any[]>([])

	const getDeptTreeList = async () => {
		treeListV3({}).then(res => {
			setDeptTreeList(res?.data)
		})
	}

	const getRoleList = async () => {
		rolePageV3({pageSize: 1000, pageNum: 1, pageIndex: 0}).then(res => {
			setRoleList(res?.data?.records)
		})
	}

	useEffect(() => {
		getDeptTreeList().catch(console.log)
		getRoleList().catch(console.log)
	}, []);

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
				const co = {
					id: value?.id,
					username: value.username,
					status: value?.status,
					mail: value?.mail,
					mobile: value?.mobile,
					avatar: value?.avatar,
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
				tooltip={"用于用户名密码登录【不允许重复，不允许修改用户名】"}
				disabled={edit}
				readonly={readOnly}
				placeholder={'请输入用户名'}
				rules={[{ required: true, message: '请输入用户名' }]}
			/>

			<ProFormText
				name="mail"
				label="邮箱"
				tooltip={"用于邮箱登录【不允许重复】"}
				readonly={readOnly}
				placeholder={'请输入邮箱'}
			/>

			<ProFormText
				name="mobile"
				label="手机号"
				tooltip={"用于手机号登录【不允许重复】"}
				readonly={readOnly}
				placeholder={'请输入手机号'}
			/>

			<ProFormSelect
				name="roleIds"
				allowClear={true}
				label="所属角色"
				mode={'multiple'}
				hidden={!readOnly}
				readonly={readOnly}
				placeholder={'请选择所属角色'}
				options={roleList}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
					},
				}}
			/>

			<ProFormTreeSelect
				name="deptIds"
				label="所属部门"
				hidden={!readOnly}
				readonly={readOnly}
				allowClear={true}
				placeholder={'请选择所属部门'}
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
					dropdownStyle: { maxHeight: 500 },
					// 不显示搜索
					showSearch: false,
				}}
				request={async () => {
					return deptTreeList
				}}
			/>

		</DrawerForm>
	);
};
