import type {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {pageOss} from "@/services/admin/oss";
import {trim} from "@/utils/format";
import {useRef} from "react";
import {Space, Switch, Tag} from "antd";

export default () => {

	type TableColumns = {
		id: number | undefined;
		name: string | undefined;
		status: number | undefined;
		type: string | undefined;
		createTime: string | undefined;
	};

	const actionRef = useRef();

	const getPageQueryParam = (params: any) => {
		return  {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			name: trim(params?.name),
			status: params?.statusValue,
			type: params?.typeValue,
			params: {
				startTime: params?.startDate ? `${params.startDate} 00:00:00` : undefined,
				endTime: params?.endDate ? `${params.endDate} 23:59:59` : undefined
			}
		}
	}

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '序号',
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 60,
		},
		{
			title: 'OSS名称',
			dataIndex: 'name',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入OSS名称',
			}
		},
		{
			title: 'OSS类型',
			key: 'typeValue',
			dataIndex: 'typeValue',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: '请选择OSS类型',
				options: [
					{
						value: 'amazon_s3',
						label: '亚马逊S3',
					},
					{
						value: 'local',
						label: '本地',
					},
					{
						value: 'minio',
						label: 'MinIO',
					},
				],
			},
			ellipsis: true
		},
		{
			disable: true,
			title: 'OSS类型',
			dataIndex: 'type',
			hideInSearch: true,
			renderFormItem: (_, { defaultRender }) => {
				return defaultRender(_);
			},
			render: (_, record) => (
				<Space>
					{record?.type === 'amazon_s3' && (
						<Tag color={'rgb(51 114 253)'} key={'amazon_s3'}>
							亚马逊S3
						</Tag>
					)}
					{record?.type === 'local' && (
						<Tag color={'#fd5251'} key={'local'}>
							本地
						</Tag>
					)}
					{record?.type === 'minio' && (
						<Tag color={'green-inverse'} key={'minio'}>
							MinIO
						</Tag>
					)}
				</Space>
			),
		},
		{
			title: 'OSS状态',
			key: 'statusValue',
			dataIndex: 'statusValue',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: '请选择菜单状态',
				options: [
					{
						value: 0,
						label: '启用',
					},
					{
						value: 1,
						label: '禁用',
					},
				]
			},
			ellipsis: true
		},
		{
			title: 'OSS状态',
			dataIndex: 'status',
			hideInSearch: true,
			render: (_, record) => (
				<Switch checkedChildren="启用" unCheckedChildren="禁用" disabled={true} checked={record?.status === 0} />
			),
		},
		{
			title: '登录日期',
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
			ellipsis: true
		},
		{
			title: '登录日期',
			dataIndex: 'createTimeValue',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: ['请选择开始日期', '请选择结束日期'],
			},
			search: {
				transform: (value) => {
					return {
						startDate: value[0],
						endDate: value[1],
					};
				},
			}
		}
	];

	return (
		<ProTable<TableColumns>
			actionRef={actionRef}
			columns={columns}
			request={async (params) => {
				// 表单搜索项会从 params 传入，传递给后端接口。
				return pageOss(getPageQueryParam(params)).then(res => {
					return Promise.resolve({
						data: res?.data?.records,
						total: parseInt(res?.data?.total || 0),
						success: true,
					});
				})
			}}
			rowKey="id"
			pagination={{
				showQuickJumper: true,
				showSizeChanger: false,
				pageSize: 10
			}}
			search={{
				layout: 'vertical',
				defaultCollapsed: true,
			}}
			dateFormatter="string"
			toolbar={{
				title: '对象存储配置',
				tooltip: '对象存储配置',
			}}
		/>
	);
};
