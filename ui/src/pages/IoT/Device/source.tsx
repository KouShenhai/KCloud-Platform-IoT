import {SourceDrawer} from './SourceDrawer';
import {pageSource, removeSource, getSourceById} from '@/services/iot/source';
import { useAccess, useIntl } from '@@/exports';
import {DeleteOutlined, PlusOutlined,} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {Button, message, Modal} from 'antd';
import type {TableRowSelection} from 'antd/es/table/interface';
import {useEffect, useRef, useState} from 'react';
import {listDictItem} from "@/services/admin/dictItem";
import {v7 as uuidV7} from "uuid";

export default () => {
	const access = useAccess();
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const actionRef = useRef<ActionType | null>(null);
	const [readOnly, setReadOnly] = useState(false);
	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<API.SourceCO>({});
	const [title, setTitle] = useState('');
	const [ids, setIds] = useState<number[]>([]);
	const [sourceTypeOptions, setSourceTypeOptions] = useState<any>([]);
	const [requestId, setRequestId] = useState('');

	const reload = () => {
		actionRef?.current?.reload();
	};

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: params?.name,
			type: params?.type,
			params: {
				startTime: params?.startDate
					? `${params.startDate} 00:00:00`
					: undefined,
				endTime: params?.endDate
					? `${params.endDate} 23:59:59`
					: undefined,
			},
		};
	};

	const getSourceType = async () => {
		const  res = await listDictItem({dictCode: 'source_type'});
		setSourceTypeOptions(res?.data?.map((item: any) => ({
			label: item.name,
			value: item.code
		})))
	}

	const rowSelection: TableRowSelection<API.SourceCO> = {
		onChange: (selectedRowKeys) => {
			const selectedIds: number[] = [];
			selectedRowKeys.forEach((item) => {
				selectedIds.push(item as number);
			});
			setIds(selectedIds);
		},
	};

	useEffect(() => {
		getSourceType().catch(console.log)
	}, []);

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
			title: '数据源类型',
			key: 'type',
			dataIndex: 'type',
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: '请选择数据源类型',
				options: sourceTypeOptions,
			},
			ellipsis: true,
		},
		{
			title: t('common.createTime'),
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
			ellipsis: true,
		},
		{
			title: t('common.createTime'),
			dataIndex: 'createTimeValue',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: [t('common.selectStartTime'), t('common.selectEndTime')],
			},
			search: {
				transform: (value) => {
					return {
						startDate: value[0],
						endDate: value[1],
					};
				},
			},
		},
		{
			title: '操作',
			valueType: 'option',
			key: 'option',
			width: 180,
			render: (_, record) => [
				access.canSourceGetDetail && (
					<a
						key="get"
						onClick={() => {
							// @ts-ignore
							getSourceById({ id: record?.id }).then(
								(res: any) => {
									setTitle('查看数据源');
									setDataSource(res?.data);
									setModalVisit(true);
									setReadOnly(true);
								},
							);
						}}
					>
						查看
					</a>
				),
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
				sourceOptions={sourceTypeOptions}
				setRequestId={setRequestId}
				requestId={requestId}
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
								setRequestId(uuidV7());
								setTitle('新增数据源');
								setReadOnly(false);
								setModalVisit(true);
								setDataSource({
									id: undefined,
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
					tooltip: '数据源',
				}}
			/>
		</>
	);
};
