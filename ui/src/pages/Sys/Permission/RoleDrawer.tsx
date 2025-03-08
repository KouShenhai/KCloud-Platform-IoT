import {
	DrawerForm,
	ProFormDigit,
	ProFormSelect,
	ProFormText,
	ProFormTreeSelect
} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyV3, saveV3} from '@/services/admin/role';
import {v7 as uuidV7} from "uuid";

interface RoleDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	menuTreeList: any[]
}

type TableColumns = {
	id: number;
	name: string | undefined;
	createTime: string | undefined;
	sort: number | undefined;
	dataScope: string | undefined;
	menuIds: string[];
	deptIds: string[];
};



export const RoleDrawer: React.FC<RoleDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, menuTreeList }) => {

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
					id: value.id,
					sort: value.sort,
					name: value.name
				}
				if (value.id === undefined) {
					saveV3({co: co}, uuidV7()).then(res => {
						if (res.code === 'OK') {
							message.success("新增成功").then()
							setModalVisit(false)
							onComponent()
						}
					})
				} else {
					modifyV3({co: co}).then(res => {
						if (res.code === 'OK') {
							message.success("修改成功").then()
							setModalVisit(false)
							onComponent()
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
				name="name"
				label="名称"
				readonly={readOnly}
				placeholder={'请输入名称'}
				rules={[{ required: true, message: '请输入名称' }]}
			/>

			<ProFormDigit
				name="sort"
				label="排序"
				readonly={readOnly}
				placeholder={'请输入排序'}
				min={1}
				max={99999}
				rules={[{ required: true, message: '请输入排序' }]}
			/>

			<ProFormSelect
				name="dataScope"
				label="数据范围"
				readonly={readOnly}
				hidden={!readOnly}
				options={[
					{value: 'all', label: '全部'},
					{value: 'custom', label: '自定义'},
					{value: 'dept_self', label: '仅本部门'},
					{value: 'dept', label: '部门及以下'},
					{value: 'self', label: '仅本人'},
				]}
			/>

			<ProFormTreeSelect
				name="menuIds"
				label="菜单权限"
				readonly={readOnly}
				hidden={!readOnly}
				allowClear={true}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
						children: 'children'
					}
				}}
				request={async () => {
					return menuTreeList
				}}
			/>

			<ProFormText
				readonly={true}
				hidden={!readOnly}
				name="createTime"
				label="创建时间"
			/>

		</DrawerForm>
	);
};
