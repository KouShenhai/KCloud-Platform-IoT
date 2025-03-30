import {DrawerForm, ProFormDigit, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import {Col, message, Row} from 'antd';
import React from "react";
import CodeMirror from '@uiw/react-codemirror';
import { javascript } from '@codemirror/lang-javascript';
import {ProFormItem, ProFormTextArea} from "@ant-design/pro-form";
import {v7 as uuidV7} from "uuid";
import {modifyV3, saveV3} from "@/services/iot/thingModel";

interface ThingModelDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	value: string;
	setValue: (value: string) => void;
	flag: number;
	setFlag: (flag: number) => void;
}

type TableColumns = {
	id: number;
	code: string | undefined;
	name: string | undefined;
	sort: number | undefined;
	dataType: string | undefined;
	category: number | undefined;
	type: string | undefined;
	expression: string | undefined;
	expressionFlag: number;
	specs: string | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

export const ThingModelDrawer: React.FC<ThingModelDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, value, setValue, flag, setFlag }) => {

	const onChange = React.useCallback((val: any) => {
		dataSource.expression = val;
		setValue(val);
	}, []);

	const getSpecs = (value: any) => {
		switch (value.dataType) {
			case 'integer': return {
				min: value.min,
				max: value.max,
				length: value.length,
				unit: value.unit,
			}
			case 'decimal': return {}
			case 'boolean': return {}
			case 'string': return {}
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
					style: {
						display: readOnly ? 'none' : 'inline-block',
					},
				}
			}}
			onFinish={ async (value) => {
				value.specs = JSON.stringify(getSpecs(value))
				// @ts-ignore
				value.type = value.type.join(',')
				if (value.id === undefined) {
					saveV3({co: value}, uuidV7()).then(res => {
						if (res.code === 'OK') {
							message.success("新增成功").then()
							setModalVisit(false)
							onComponent()
						}
					})
				} else {
					modifyV3({co: value}).then(res => {
						if (res.code === 'OK') {
							message.success("修改成功").then()
							setModalVisit(false)
							onComponent()
						}
					})
				}
			}}>
			<ProFormText
				readonly={readOnly}
				name="code"
				label="编码"
				rules={[{ required: true, message: '请输入编码' }]}
			/>

			<ProFormText
				readonly={readOnly}
				name="name"
				label="名称"
				rules={[{ required: true, message: '请输入名称' }]}
			/>

			<ProFormSelect
				name="category"
				label="模型类别"
				placeholder={'请选择模型类别'}
				rules={[{ required: true, message: '请选择模型类别' }]}
				options={[
					{value: 1, label: '属性'},
					{value: 2, label: '事件'}
				]}
			/>

			<ProFormSelect
				name="type"
				label="模型类型"
				mode={'multiple'}
				placeholder={'请选择模型类型'}
				rules={[{ required: true, message: '请选择模型类型' }]}
				options={[
					{value: 'read', label: '读'},
					{value: 'write', label: '写'},
					{value: 'report', label: '上报'}
				]}
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
				name="expressionFlag"
				label="是否使用表达式"
				readonly={readOnly}
				rules={[{required: true, message: '请选择是否使用表达式',}]}
				options={[
					{label:"是",value: 1 },
					{label:"否",value: 0 }
				]}
				onChange={setFlag}
			/>

			{flag === 1 && (
				<ProFormItem
					label="表达式"
					tooltip={"JS脚本，参数【inputMap】"}
				>
					<CodeMirror
						readOnly={readOnly}
						value={value}
						height="400px"
						extensions={[javascript({ jsx: true, typescript: true })]}
						onChange={onChange}
					/>
				</ProFormItem>
			)}

			<ProFormSelect
				name="dataType"
				label="数据类型"
				placeholder={'请选择数据类型'}
				rules={[{ required: true, message: '请选择数据类型' }]}
				options={[
					{value: 'integer', label: '整数型'},
					{value: 'decimal', label: '小数型'},
					{value: 'boolean', label: '布尔型'},
					{value: 'string', label: '字符串型'},
				]}
			/>

			<Row gutter={24}>
				<Col span={12}>
					<ProFormText
						readonly={readOnly}
						name="min"
						label="最小值"
						rules={[{ required: true, message: '请输入最小值' }]}/>
				</Col>
				<Col span={12}>
					<ProFormText
						readonly={readOnly}
						name="max"
						label="最大值"
						rules={[{ required: true, message: '请输入最大值' }]}/>
				</Col>
				<Col span={12}>
					<ProFormText
						readonly={readOnly}
						name="length"
						label="长度"
						rules={[{ required: true, message: '请输入长度' }]}/>
				</Col>
				<Col span={12}>
					<ProFormText
						readonly={readOnly}
						name="unit"
						label="单位"/>
				</Col>
			</Row>

			<ProFormTextArea
				readonly={readOnly}
				name="remark"
				label="备注"
			/>

		</DrawerForm>
	);
};
