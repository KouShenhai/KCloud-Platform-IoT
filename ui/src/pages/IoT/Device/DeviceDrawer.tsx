import {DrawerForm, ProFormDigit, ProFormRadio, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyV3, saveV3} from "@/services/iot/device";
import {v7 as uuidV7} from "uuid";
import React from "react";
import CodeMirror from '@uiw/react-codemirror';
import { javascript } from '@codemirror/lang-javascript';
import {ProFormItem} from "@ant-design/pro-form";

interface DeviceDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	value: string;
	setValue: (value: string) => void;
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

export const DeviceDrawer: React.FC<DeviceDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, value, setValue }) => {

	const onChange = React.useCallback((val: any) => {
		dataSource.expression = val;
		setValue(val);
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
				console.log(value)
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
				name="dataType"
				label="数据类型"
				placeholder={'请选择数据类型'}
				rules={[{ required: true, message: '请选择数据类型' }]}
				options={[
					{value: 'integer', label: '整数型'},
					{value: 'string', label: '字符串型'},
					{value: 'decimal', label: '小数型'},
					{value: 'boolean', label: '布尔型'}
				]}
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

			<ProFormRadio.Group
				name="expressionFlag"
				label="是否使用表达式"
				readonly={readOnly}
				rules={[{required: true, message: '请选择是否使用表达式',}]}
				options={[
					{label:"否",value: 0 },
					{label:"是",value: 1 }
				]}
			/>

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


			<ProFormDigit
				name="sort"
				label="排序"
				readonly={readOnly}
				placeholder={'请输入排序'}
				min={1}
				max={99999}
				rules={[{ required: true, message: '请输入排序' }]}
			/>

		</DrawerForm>
	);
};
