import {DrawerForm} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyV3, saveV3} from "@/services/iot/thingModel";
import {v7 as uuidV7} from "uuid";
import React from "react";
import CodeMirror from '@uiw/react-codemirror';
import { javascript } from '@codemirror/lang-javascript';
import {ProFormItem} from "@ant-design/pro-form";

interface ThingModelDrawerProps {
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
	specs: string | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

export const ThingModelDrawer: React.FC<ThingModelDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, value, setValue }) => {

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
			<ProFormItem
				label="表达式"
				tooltip={"JS脚本，参数【inputMap】"}
			>
				<CodeMirror
					value={value}
					height="400px"
					extensions={[javascript({ jsx: true, typescript: true })]}
					onChange={onChange}
				/>
			</ProFormItem>
		</DrawerForm>
	);
};
