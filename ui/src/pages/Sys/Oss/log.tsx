import type {ProColumns} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {pageOssLog} from "@/services/admin/ossLog";
import {useRef} from "react";
import {Space, Tag} from "antd";

export default () => {

	type TableColumns = {
		id: number | undefined;
		name: string | undefined;
		md5: number | undefined;
		url: string | undefined;
		size: number | undefined;
		contentType: string | undefined;
		format: string | undefined;
		ossId: number | undefined;
		type: string | undefined;
		createTime: string | undefined;
	};

	const actionRef = useRef();

	const getPageQueryParam = (params: any) => {
		return  {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
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
			title: '文件名称',
			dataIndex: 'name',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true
		},
		{
			title: 'MD5',
			dataIndex: 'md5',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true
		},
		{
			title: '文件大小【单位/字节】',
			dataIndex: 'size',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true
		},
		{
			title: '文件类型',
			dataIndex: 'contentType',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true
		},
		{
			title: '类型',
			dataIndex: 'type',
			ellipsis: true,
			valueType: 'text',
			renderFormItem: (_, { defaultRender }) => {
				return defaultRender(_);
			},
			render: (_, record) => (
				<Space>
					{record?.type === 'image' && (
						<Tag color={'rgb(51 114 253)'} key={'image'}>
							图片
						</Tag>
					)}
					{record?.type === 'video' && (
						<Tag color={'#fd5251'} key={'video'}>
							视频
						</Tag>
					)}
					{record?.type === 'audio' && (
						<Tag color={'#ffa500'} key={'audio'}>
							音频
						</Tag>
					)}
				</Space>
			),
		},
		{
			title: '文件格式',
			dataIndex: 'format',
			ellipsis: true,
			valueType: 'text',
			hideInSearch: true
		},
		{
			title: '创建日期',
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
			ellipsis: true
		},
		{
			title: '创建日期',
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
				return pageOssLog(getPageQueryParam(params)).then(res => {
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
