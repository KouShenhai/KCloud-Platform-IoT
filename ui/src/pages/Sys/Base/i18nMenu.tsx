import {
	getI18nMenuById,
	pageI18nMenu,
	removeI18nMenu,
} from '@/services/admin/i18nMenu';
import {trim} from '@/utils/format';
import {useAccess, useIntl} from '@@/exports';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {Button, message, Modal} from 'antd';
import {useRef, useState} from 'react';

export default () => {
	type TableColumns = {
		id: number | undefined;
		code: string | undefined;
		name: string | undefined;
		createTime: string | undefined;
	};
	const access = useAccess();
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({id}, values);

	const actionRef = useRef<ActionType | null>(null);
	const [readOnly, setReadOnly] = useState(false);
	const [modalVisit, setModalVisit] = useState(false);
	const [dataSource, setDataSource] = useState<any>({});
	const [ids, setIds] = useState<number[]>([]);
	const [title, setTitle] = useState('');

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			code: trim(params?.code),
			name: trim(params?.name),
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

	const columns: ProColumns<TableColumns>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 85,
		},
		{
			title: t('sys.i18nMenu.code'),
			dataIndex: 'code',
			ellipsis: true,
			width: 120,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.i18nMenu.placeholder.code'),
			},
		},
		{
			title: t('sys.i18nMenu.name'),
			dataIndex: 'name',
			ellipsis: true,
			width: 120,
			fieldProps: {
				placeholder: t('sys.i18nMenu.placeholder.name'),
			},
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
			title: t('common.operation'),
			valueType: 'option',
			key: 'option',
			render: (_, record) => [
				access.canI18nMenuGetDetail && (
					<a
						key="get"
						onClick={() => {
							getI18nMenuById({id: record?.id as number}).then((res) => {
								if (res.code === 'OK') {
									setTitle(t('sys.i18nMenu.view'));
									setModalVisit(true);
									setReadOnly(true);
									setDataSource(res?.data);
								}
							});
						}}
					>
						{t('common.view')}
					</a>
				),
				access.canI18nMenuModify && (
					<a
						key="modify"
						onClick={() => {
							getI18nMenuById({id: record?.id as number}).then((res) => {
								if (res.code === 'OK') {
									setTitle(t('sys.i18nMenu.modify'));
									setModalVisit(true);
									setReadOnly(false);
									setDataSource(res?.data);
								}
							});
						}}
					>
						{t('common.modify')}
					</a>
				),
				access.canI18nMenuRemove && (
					<a
						key="remove"
						onClick={() => {
							Modal.confirm({
								title: t('confirm.deleteTitle'),
								content: t('confirm.deleteContent'),
								okText: t('common.ok'),
								cancelText: t('common.cancel'),
								onOk: () => {
									removeI18nMenu([record?.id as number]).then((res) => {
										if (res.code === 'OK') {
											message.success(t('toast.deleteSuccess')).then();
											// @ts-ignore
											actionRef?.current?.reload();
										}
									});
								},
							});
						}}
					>
						{t('common.delete')}
					</a>
				),
			],
			width: 190,
		},
	];

	return (
		<>
			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					return pageI18nMenu(getPageQueryParam(params)).then((res) => {
						return Promise.resolve({
							data: res?.data?.records,
							total: parseInt(res?.data?.total || 0),
							success: true,
						});
					});
				}}
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
					access.canI18nMenuSave && (
						<Button
							key="save"
							type="primary"
							icon={<PlusOutlined/>}
							onClick={() => {
								setTitle(t('sys.i18nMenu.insert'));
								setReadOnly(false);
								setModalVisit(true);
								setDataSource({
									id: undefined,
									code: '',
									name: '',
								});
							}}
						>
							{t('common.insert')}
						</Button>
					),
				]}
				dateFormatter="string"
				toolbar={{
					title: t('menu.sys.base.i18n-menu'),
					tooltip: t('menu.sys.base.i18n-menu'),
				}}
			/>
		</>
	);
};
