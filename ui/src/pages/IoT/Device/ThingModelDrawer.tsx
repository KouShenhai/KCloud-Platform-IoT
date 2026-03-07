import { modifyThingModel, saveThingModel } from '@/services/iot/thingModel';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormDigit,
	ProFormSelect,
	ProFormText,
} from '@ant-design/pro-components';
import { ProFormTextArea } from '@ant-design/pro-form';
import { Col, message, Row } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface ThingModelDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	dataType: string;
	setDataType: (type: string) => void;
	requestId: string;
	setRequestId: (requestId: string) => void;
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

export const ThingModelDrawer: React.FC<ThingModelDrawerProps> = ({
	modalVisit,
	setModalVisit,
	title,
	readOnly,
	dataSource,
	onComponent,
	dataType,
	setDataType,
	requestId,
	setRequestId,
}) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [loading, setLoading] = useState(false);

	const getSpecs = (value: any) => {
		switch (value.dataType) {
			case 'integer':
				return {
					length: value.length,
					unit: value.unit,
				};
			case 'decimal':
				return {
					integerLength: value.integerLength,
					decimalLength: value.decimalLength,
					unit: value.unit,
				};
			case 'boolean':
				return {
					trueText: value?.trueText,
					falseText: value?.falseText,
				};
			case 'string':
				return {
					length: value.length,
				};
			default:
				return {};
		}
	};

	return (
		<DrawerForm<TableColumns>
			open={modalVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
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
				},
			}}
			onFinish={async (value) => {
				setLoading(true);
				value.specs = JSON.stringify(getSpecs(value));
				// @ts-ignore
				value.type = value.type.join(',');
				if (value.id === undefined) {
					saveThingModel({ co: value }, requestId)
						.then((res) => {
							if (res.code === 'OK') {
								message.success(t('toast.saveSuccess')).then();
								setModalVisit(false);
								onComponent();
							}
						})
						.finally(() => {
							setRequestId(uuidV7());
							setLoading(false);
						});
				} else {
					modifyThingModel({ co: value })
						.then((res) => {
							if (res.code === 'OK') {
								message.success(t('toast.modifySuccess')).then();
								setModalVisit(false);
								onComponent();
							}
						})
						.finally(() => {
							setLoading(false);
						});
				}
			}}
		>
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
				label={t('iot.thingModel.code')}
				rules={[
					{ required: true, message: t('iot.thingModel.required.code') },
				]}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="name"
				label={t('iot.thingModel.name')}
				rules={[
					{ required: true, message: t('iot.thingModel.required.name') },
				]}
			/>

			<ProFormSelect
				disabled={loading}
				name="category"
				label={t('iot.thingModel.category')}
				readonly={readOnly}
				placeholder={t('iot.thingModel.placeholder.category')}
				rules={[
					{
						required: true,
						message: t('iot.thingModel.required.category'),
					},
				]}
				options={[
					{
						value: 1,
						label: t('iot.thingModel.category.property'),
					},
					{
						value: 2,
						label: t('iot.thingModel.category.event'),
					},
				]}
			/>

			<ProFormSelect
				disabled={loading}
				name="type"
				label={t('iot.thingModel.type')}
				mode={'multiple'}
				readonly={readOnly}
				placeholder={t('iot.thingModel.placeholder.type')}
				rules={[
					{ required: true, message: t('iot.thingModel.required.type') },
				]}
				options={[
					{ value: 'read', label: t('iot.thingModel.type.read') },
					{ value: 'write', label: t('iot.thingModel.type.write') },
					{ value: 'report', label: t('iot.thingModel.type.report') },
				]}
			/>

			<ProFormDigit
				disabled={loading}
				name="sort"
				label={t('iot.thingModel.sort')}
				readonly={readOnly}
				placeholder={t('iot.thingModel.placeholder.sort')}
				min={1}
				max={99999}
				rules={[
					{ required: true, message: t('iot.thingModel.required.sort') },
				]}
			/>

			<ProFormSelect
				disabled={loading}
				name="dataType"
				label={t('iot.thingModel.dataType')}
				readonly={readOnly}
				placeholder={t('iot.thingModel.placeholder.dataType')}
				rules={[
					{
						required: true,
						message: t('iot.thingModel.required.dataType'),
					},
				]}
				options={[
					{
						value: 'integer',
						label: t('iot.thingModel.dataType.integer'),
					},
					{
						value: 'decimal',
						label: t('iot.thingModel.dataType.decimal'),
					},
					{
						value: 'boolean',
						label: t('iot.thingModel.dataType.boolean'),
					},
					{
						value: 'string',
						label: t('iot.thingModel.dataType.string'),
					},
				]}
				onChange={setDataType}
			/>

			{dataType === 'string' && (
				<ProFormText
					disabled={loading}
					readonly={readOnly}
					name="length"
					label={t('iot.thingModel.length')}
					rules={[
						{
							required: true,
							message: t('iot.thingModel.required.length'),
						},
						{
							pattern: /^(2000|1\d{3}|[1-9]\d{0,2})$/,
							message: t('iot.thingModel.validate.length1To2000'),
						},
					]}
				/>
			)}

			{dataType === 'boolean' && (
				<Row gutter={24}>
					<Col span={12}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="trueText"
							label={t('iot.thingModel.trueText')}
							rules={[
								{
									required: true,
									message: t('iot.thingModel.required.trueText'),
								},
							]}
						/>
					</Col>
					<Col span={12}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="falseText"
							label={t('iot.thingModel.falseText')}
							rules={[
								{
									required: true,
									message: t('iot.thingModel.required.falseText'),
								},
							]}
						/>
					</Col>
				</Row>
			)}

			{dataType === 'integer' && (
				<Row gutter={24}>
					<Col span={12}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="length"
							label={t('iot.thingModel.length')}
							rules={[
								{
									required: true,
									message: t('iot.thingModel.required.length'),
								},
								{
									pattern: /^(8|16|32|64)$/,
									message: t('iot.thingModel.validate.intLength'),
								},
							]}
						/>
					</Col>
					<Col span={12}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="unit"
							label={t('iot.thingModel.unit')}
						/>
					</Col>
				</Row>
			)}

			{dataType === 'decimal' && (
				<Row gutter={24}>
					<Col span={8}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="integerLength"
							label={t('iot.thingModel.integerLength')}
							rules={[
								{
									required: true,
									message: t('iot.thingModel.required.integerLength'),
								},
								{
									pattern: /^([1-9]|[1-5][0-9]|6[0-4])$/,
									message: t('iot.thingModel.validate.integerLength'),
								},
							]}
						/>
					</Col>
					<Col span={8}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="decimalLength"
							label={t('iot.thingModel.decimalLength')}
							rules={[
								{
									required: true,
									message: t('iot.thingModel.required.decimalLength'),
								},
								{
									pattern: /^[1-4]$/,
									message: t('iot.thingModel.validate.decimalLength'),
								},
							]}
						/>
					</Col>
					<Col span={8}>
						<ProFormText
							disabled={loading}
							readonly={readOnly}
							name="unit"
							label={t('iot.thingModel.unit')}
						/>
					</Col>
				</Row>
			)}

			<ProFormTextArea
				disabled={loading}
				readonly={readOnly}
				name="remark"
				label={t('iot.thingModel.remark')}
			/>

			{readOnly && (
				<ProFormText
					disabled={loading}
					readonly={true}
					name="createTime"
					rules={[
						{
							required: true,
							message: t('role.validate.createTimeRequired'),
						},
					]}
					label={t('common.createTime')}
				/>
			)}
		</DrawerForm>
	);
};
