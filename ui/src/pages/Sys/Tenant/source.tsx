import { SourceDrawer } from './SourceDrawer';
import {
	pageSource,
	removeSource,
} from '@/services/admin/source';
import { useAccess } from '@@/exports';
import {
	DeleteOutlined,
	PlusOutlined,
} from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';
import { Button, message, Modal } from 'antd';
import type { TableRowSelection } from 'antd/es/table/interface';
import { useRef, useState } from 'react';

export default () => {
	const access = useAccess();

	const actionRef = useRef<ActionType | null>(null);
	const [readOnly, setReadOnly] = useState(false);
	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<API.SourceCO>({});
	const [title, setTitle] = useState('');
	const [ids, setIds] = useState<number[]>([]);

	const reload = () => {
		actionRef?.current?.reload();
	};

	const getPageQueryParam = (params: any) => {
		const param = {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: params?.name,
		};
		return param;
	};

	const rowSelection: TableRowSelection<API.SourceCO> = {
		onChange: (selectedRowKeys) => {
			const selectedIds: number[] = [];
			selectedRowKeys.forEach((item) => {
				selectedIds.push(item as number);
			});
			setIds(selectedIds);
		},
	};

	const showRemoveConfirm = (removeIds: number[]) => {
		Modal.confirm({
			title: '确定要删除选中的数据吗？',
			content: '删除后将无法恢复',
			okText: '确定',
			cancelText: '取消',
			onOk: async () => {
				if (removeIds.length === 0) {
					message.warning('至少选择一项').then();
					return;
				}
				removeSource(removeIds).then((res) => {
					if (res.code === 'OK') {
						message.success('删除成功').then();
						setIds([]);
						reload();
					}
				});
			},
		});
	};

	const columns: ProColumns<API.SourceCO>[] = [
		{
			title: '序号',
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 85,
		},
		{
			title: '数据源名称',
			dataIndex: 'name',
			ellipsis: true,
		},
		{
			title: '驱动名称',
			dataIndex: 'driverClassName',
			ellipsis: true,
		},
		{
			title: '连接地址',
			dataIndex: 'url',
			ellipsis: true,
			hideInSearch: true,
		},
		{
			title: '用户名',
			dataIndex: 'username',
			ellipsis: true,
			hideInSearch: true,
		},
		{
			title: '操作',
			valueType: 'option',
			key: 'option',
			width: 180,
			render: (_, record) => [
				access.canSourceModify && (
					<a
						key="modify"
						onClick={() => {
                            setTitle('修改数据源');
                            setModalVisit(true);
                            setReadOnly(false);
                            setDataSource(record);
						}}
					>
						修改
					</a>
				),
				access.canSourceRemove && (
					<a
						key="remove"
						onClick={() => {
							showRemoveConfirm([record?.id as number]);
						}}
					>
						删除
					</a>
				),
			],
		},
	];

	return (
		<>
			<SourceDrawer
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				onComponent={() => {
					setIds([]);
					reload();
				}}
			/>
			<ProTable<API.SourceCO>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					return pageSource(getPageQueryParam(params)).then((res) => {
						return Promise.resolve({
							data: res?.data?.records,
							total: parseInt(res?.data?.total || 0),
							success: true,
						});
					});
				}}
				rowSelection={{ ...rowSelection }}
				rowKey="id"
				pagination={{
					showQuickJumper: true,
					showSizeChanger: false,
					pageSize: 10,
				}}
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
				toolBarRender={() => [
					access.canSourceSave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined />}
							onClick={() => {
								setTitle('新增数据源');
								setReadOnly(false);
								setModalVisit(true);
								setDataSource({
									id: undefined,
									name: '',
									driverClassName: 'com.taosdata.jdbc.rs.RestfulDriver',
									url: '',
									username: '',
									password: ''
								});
							}}
						>
							新增
						</Button>
					),
					access.canSourceRemove && (
						<Button
							key="remove"
							type="primary"
							danger
							icon={<DeleteOutlined />}
							onClick={() => {
								showRemoveConfirm(ids);
							}}
						>
							删除
						</Button>
					),
				]}
				dateFormatter="string"
				toolbar={{
					title: '数据源管理',
					tooltip: '管理系统数据源',
				}}
			/>
		</>
	);
};
