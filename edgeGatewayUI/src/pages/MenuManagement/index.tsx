import {
	createMenu,
	deleteMenu,
	listMenus,
	Menu,
	MenuMutationRequest,
	MenuStatus,
	updateMenu,
	updateMenuStatus,
} from '@/services/menu';
import { PlusOutlined } from '@ant-design/icons';
import {
	ActionType,
	ModalForm,
	PageContainer,
	ProColumns,
	ProFormDigit,
	ProFormSelect,
	ProFormText,
	ProTable,
} from '@ant-design/pro-components';
import { Badge, Button, message, Popconfirm, Space, Tag } from 'antd';
import React, { useMemo, useRef, useState } from 'react';

const statusOptions = [
	{ label: 'enabled', value: 'enabled' },
	{ label: 'disabled', value: 'disabled' },
];

const flattenMenuOptions = (menus: Menu[], excludeID?: string, level = 0) => {
	return menus.flatMap((menu) => {
		if (menu.id === excludeID) {
			return [];
		}
		const children = flattenMenuOptions(
			menu.children || [],
			excludeID,
			level + 1,
		);
		return [
			{
				label: `${'  '.repeat(level)}${menu.name}`,
				value: menu.id,
			},
			...children,
		];
	});
};

const normalizePayload = (
	values: MenuMutationRequest,
): MenuMutationRequest => ({
	name: values.name.trim(),
	path: values.path.trim(),
	icon: values.icon?.trim() || '',
	parentId: values.parentId || '',
	sort: values.sort,
	status: values.status,
});

const MenuManagement: React.FC = () => {
	const actionRef = useRef<ActionType>();
	const [createOpen, setCreateOpen] = useState(false);
	const [editMenu, setEditMenu] = useState<Menu>();
	const [menuTree, setMenuTree] = useState<Menu[]>([]);

	const createParentOptions = useMemo(
		() => flattenMenuOptions(menuTree),
		[menuTree],
	);
	const editParentOptions = useMemo(
		() => flattenMenuOptions(menuTree, editMenu?.id),
		[editMenu?.id, menuTree],
	);

	const columns: ProColumns<Menu>[] = [
		{
			title: 'Name',
			dataIndex: 'name',
			formItemProps: {
				rules: [
					{ required: true, message: 'Please enter a menu name' },
				],
			},
		},
		{
			title: 'Path',
			dataIndex: 'path',
			search: false,
			render: (_, record) => <Tag>{record.path}</Tag>,
		},
		{
			title: 'Icon',
			dataIndex: 'icon',
			search: false,
			render: (_, record) => record.icon || '-',
		},
		{
			title: 'Sort',
			dataIndex: 'sort',
			search: false,
			sorter: (a, b) => a.sort - b.sort,
			width: 90,
		},
		{
			title: 'Status',
			dataIndex: 'status',
			search: false,
			render: (_, record) => (
				<Badge
					status={record.status === 'enabled' ? 'success' : 'default'}
					text={record.status}
				/>
			),
		},
		{
			title: 'Created',
			dataIndex: 'createdAt',
			valueType: 'dateTime',
			search: false,
		},
		{
			title: 'Actions',
			valueType: 'option',
			width: 210,
			render: (_, record) => {
				const nextStatus: MenuStatus =
					record.status === 'enabled' ? 'disabled' : 'enabled';
				return (
					<Space>
						<a onClick={() => setEditMenu(record)}>Edit</a>
						<a
							onClick={async () => {
								await updateMenuStatus(record.id, nextStatus);
								message.success('Updated');
								actionRef.current?.reload();
							}}
						>
							{nextStatus === 'enabled' ? 'Enable' : 'Disable'}
						</a>
						<Popconfirm
							title="Delete menu?"
							onConfirm={async () => {
								await deleteMenu(record.id);
								message.success('Deleted');
								actionRef.current?.reload();
							}}
						>
							<a>Delete</a>
						</Popconfirm>
					</Space>
				);
			},
		},
	];

	return (
		<PageContainer header={{ title: 'Menu Management' }}>
			<ProTable<Menu>
				actionRef={actionRef}
				rowKey="id"
				columns={columns}
				pagination={false}
				search={{ labelWidth: 90 }}
				expandable={{ defaultExpandAllRows: true }}
				toolBarRender={() => [
					<Button
						key="create"
						type="primary"
						icon={<PlusOutlined />}
						onClick={() => setCreateOpen(true)}
					>
						New Menu
					</Button>,
				]}
				request={async (params) => {
					const res = await listMenus({
						keyword: params.name as string,
						tree: true,
					});
					setMenuTree(res.data.list);
					return {
						data: res.data.list,
						total: res.data.total,
						success: res.code === 200,
					};
				}}
			/>

			<ModalForm<MenuMutationRequest>
				title="New Menu"
				open={createOpen}
				initialValues={{ sort: 1, status: 'enabled' }}
				modalProps={{
					destroyOnClose: true,
					onCancel: () => setCreateOpen(false),
				}}
				onFinish={async (values) => {
					await createMenu(normalizePayload(values));
					message.success('Created');
					setCreateOpen(false);
					actionRef.current?.reload();
					return true;
				}}
			>
				<ProFormText
					name="name"
					label="Name"
					rules={[
						{ required: true, message: 'Please enter a menu name' },
					]}
				/>
				<ProFormText
					name="path"
					label="Path"
					rules={[
						{ required: true, message: 'Please enter a path' },
						{ pattern: /^\//, message: 'Path must start with /' },
					]}
				/>
				<ProFormText name="icon" label="Icon" />
				<ProFormSelect
					name="parentId"
					label="Parent"
					allowClear
					options={createParentOptions}
				/>
				<ProFormDigit
					name="sort"
					label="Sort"
					min={0}
					rules={[
						{ required: true, message: 'Please enter sort order' },
					]}
				/>
				<ProFormSelect
					name="status"
					label="Status"
					options={statusOptions}
					rules={[
						{ required: true, message: 'Please select a status' },
					]}
				/>
			</ModalForm>

			<ModalForm<MenuMutationRequest>
				title="Edit Menu"
				open={!!editMenu}
				initialValues={
					editMenu
						? {
								name: editMenu.name,
								path: editMenu.path,
								icon: editMenu.icon,
								parentId: editMenu.parentId || undefined,
								sort: editMenu.sort,
								status: editMenu.status,
						  }
						: undefined
				}
				modalProps={{
					destroyOnClose: true,
					onCancel: () => setEditMenu(undefined),
				}}
				onFinish={async (values) => {
					if (!editMenu) {
						return false;
					}
					await updateMenu(editMenu.id, normalizePayload(values));
					message.success('Updated');
					setEditMenu(undefined);
					actionRef.current?.reload();
					return true;
				}}
			>
				<ProFormText
					name="name"
					label="Name"
					rules={[
						{ required: true, message: 'Please enter a menu name' },
					]}
				/>
				<ProFormText
					name="path"
					label="Path"
					rules={[
						{ required: true, message: 'Please enter a path' },
						{ pattern: /^\//, message: 'Path must start with /' },
					]}
				/>
				<ProFormText name="icon" label="Icon" />
				<ProFormSelect
					name="parentId"
					label="Parent"
					allowClear
					options={editParentOptions}
				/>
				<ProFormDigit
					name="sort"
					label="Sort"
					min={0}
					rules={[
						{ required: true, message: 'Please enter sort order' },
					]}
				/>
				<ProFormSelect
					name="status"
					label="Status"
					options={statusOptions}
					rules={[
						{ required: true, message: 'Please select a status' },
					]}
				/>
			</ModalForm>
		</PageContainer>
	);
};

export default MenuManagement;
