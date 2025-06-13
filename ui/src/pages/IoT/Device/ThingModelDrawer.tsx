import {DrawerForm, ProFormDigit, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import {Col, message, Row} from 'antd';
import React, {useState} from "react";
import {ProFormTextArea} from "@ant-design/pro-form";
import {v7 as uuidV7} from "uuid";
import {modifyThingModel, saveThingModel} from "@/services/iot/thingModel";

interface ThingModelDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	dataType: string;
	setDataType: (type: string) => void;
	requestId: string
	setRequestId: (requestId: string) => void
}

type TableColumns = {
	id: number;
	code: string | undefined;
	name: string | undefined;
	sort: number | undefined;
	dataType: string | undefined;
	category: number | undefined;
	type: string | undefined;
	specs: string | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

export const ThingModelDrawer: React.FC<ThingModelDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent,dataType, setDataType, requestId, setRequestId }) => {

	const [loading, setLoading] = useState(false)

	const getSpecs = (value: any) => {
		switch (value.dataType) {
			case 'integer': return {
				length: value.length,
				unit: value.unit,
			}
			case 'decimal': return {
				integerLength: value.integerLength,
				decimalLength: value.decimalLength,
				unit: value.unit,
			}
			case 'boolean': return {
				trueText: value?.trueText,
				falseText: value?.falseText,
			}
			case 'string': return {
				length: value.length,
			}
			default: return {}
		}
	}

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
					disabled: loading,
					style: {
						display: readOnly ? 'none' : 'inline-block',
					},
				}
			}}
			onFinish={ async (value) => {
				setLoading(true)
				value.specs = JSON.stringify(getSpecs(value))
				// @ts-ignore
				value.type = value.type.join(',')
				if (value.id === undefined) {
					saveThingModel({co: value}, requestId).then(res => {
						if (res.code === 'OK') {
							message.success("新增成功").then()
							setModalVisit(false)
							onComponent()
						}
					}).finally(() => {
						setRequestId(uuidV7())
						setLoading(false)
					})
				} else {
					modifyThingModel({co: value}).then(res => {
						if (res.code === 'OK') {
							message.success("修改成功").then()
							setModalVisit(false)
							onComponent()
						}
					}).finally(() => {
						setLoading(false)
					})
				}
			}}>

			<ProFormText
				disabled={loading}
				name="id"
				label="ID"
				hidden={true}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="code"
				label="物模型编码"
				rules={[{ required: true, message: '请输入物模型编码' }]}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="name"
				label="物模型名称"
				rules={[{ required: true, message: '请输入物模型名称' }]}
			/>

			<ProFormSelect
				disabled={loading}
				name="category"
				label="物模型类别"
				readonly={readOnly}
				placeholder={'请选择模型类别'}
				rules={[{ required: true, message: '请选择物模型类别' }]}
				options={[
					{value: 1, label: '属性'},
					{value: 2, label: '事件'}
				]}
			/>

			<ProFormSelect
				disabled={loading}
				name="type"
				label="物模型类型"
				mode={'multiple'}
				readonly={readOnly}
				placeholder={'请选择物模型类型'}
				rules={[{ required: true, message: '请选择物模型类型' }]}
				options={[
					{value: 'read', label: '读'},
					{value: 'write', label: '写'},
					{value: 'report', label: '上报'}
				]}
			/>

			<ProFormDigit
				disabled={loading}
				name="sort"
				label="物模型排序"
				readonly={readOnly}
				placeholder={'请输入物模型排序'}
				min={1}
				max={99999}
				rules={[{ required: true, message: '请输入排序' }]}
			/>

			<ProFormSelect
				disabled={loading}
				name="dataType"
				label="物模型数据类型"
				readonly={readOnly}
				placeholder={'请选择数据类型'}
				rules={[{ required: true, message: '请选择物模型数据类型' }]}
				options={[
					{value: 'integer', label: '整数型'},
					{value: 'decimal', label: '小数型'},
					{value: 'boolean', label: '布尔型'},
					{value: 'string', label: '字符串型'},
				]}
				onChange={setDataType}
			/>

			{ dataType === 'string' && (
				<ProFormText
					disabled={loading}
					readonly={readOnly}
					name="length"
					label="长度"
					rules={[{ required: true, message: '请输入长度' },
						{ pattern: /^(2000|1\d{3}|[1-9]\d{0,2})$/, message:"长度必须为1-2000的整数" },
					]}
				/>
			)}

			{ dataType === 'boolean' && (
				<Row gutter={24}>
					<Col span={12}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="trueText"
							label="1对应文本"
							rules={[{ required: true, message: '请输入1对应文本' }]}
						/>
					</Col>
					<Col span={12}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="falseText"
							label="0对应文本"
							rules={[{ required: true, message: '请输入0对应文本' }]}
						/>
					</Col>
				</Row>
			)}

			{ dataType === 'integer' && (
				<Row gutter={24}>
					<Col span={12}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="length"
							label="长度"
							rules={[
								{ required: true, message: '请输入长度' },
								{
									pattern: /^(8|16|32|64)$/,
									message: '长度必须为8、16、32、64的整数'
								}
							]}
						/>
					</Col>
					<Col span={12}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="unit"
							label="单位"
						/>
					</Col>
				</Row>
			)}

			{ dataType === 'decimal' && (
				<Row gutter={24}>
					<Col span={8}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="integerLength"
							label="整数位长度"
							rules={[
								{ required: true, message: '请输入整数位长度' },
								{
									pattern: /^([1-9]|[1-5][0-9]|6[0-4])$/,
									message: '整数位长度必须为1-64的整数'
								}
							]}
						/>
					</Col>
					<Col span={8}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="decimalLength"
							label="小数位长度"
							rules={[
								{ required: true, message: '请输入小数位长度' },
								{
									pattern: /^[1-4]$/,
									message: '小数位长度必须为1-4的整数'
								}
							]}
						/>
					</Col>
					<Col span={8}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="unit"
							label="单位"
						/>
					</Col>
				</Row>
			)}

			<ProFormTextArea
				disabled={loading}
				readonly={readOnly}
				name="remark"
				label="物模型备注"
			/>

			{ readOnly && (
				<ProFormText
					disabled={loading}
					readonly={true}
					name="createTime"
					rules={[{ required: true, message: '请输入创建时间' }]}
					label="创建时间"
				/>
			)}

		</DrawerForm>
	);
};
