import React from "react";
// @ts-ignore
import { ProFormText, ProFormTextProps } from "@ant-design/pro-components";

export type NumberType = "int" | "long" | "float" | "double";

interface Props extends ProFormTextProps {
	type: NumberType;
	precision?: number;
	rules?: any[];
	fieldProps?: any;
	label?: string;
	name?: string;
	disabled?: boolean;
	readonly?: boolean;
}

const RANGE = {
	int: {
		min: BigInt("-1000000"),
		max: BigInt("1000000"),
	},
	long: {
		min: BigInt("-1000000000000"),
		max: BigInt("1000000000000"),
	},
	float: {
		min: -1000000.000,
		max: 1000000.000,
	},
	double: {
		min: -1000000000.000000,
		max: 1000000000.000000,
	},
} as const;

export default function ProFormNumber({
	                                      label,
										  name,
										  type,
										  readonly,
										  disabled,
										  precision = 3,
										  rules = [],
										  fieldProps,
											  ...rest
										  }: Props) {
	return (
		<ProFormText
			{...rest}
			readonly={readonly}
			disabled={disabled}
			name={name}
			label={label}
			fieldProps={{
				inputMode: "decimal",
				...fieldProps,
			}}
			rules={[
				...rules,
				{
					validator(_, value) {
						if (value === undefined || value === null || value === "") {
							return Promise.resolve();
						}

						// eslint-disable-next-line no-param-reassign
						value = value.trim();

						switch (type) {
							case "int": {
								if (!/^-?\d+$/.test(value)) {
									return Promise.reject(new Error("请输入整数"));
								}

								const v = BigInt(value);

								if (v < RANGE.int.min || v > RANGE.int.max) {
									return Promise.reject(
										new Error(
											`请输入 ${RANGE.int.min} ~ ${RANGE.int.max} 之间的整数`
										)
									);
								}

								return Promise.resolve();
							}

							case "long": {
								if (!/^-?\d+$/.test(value)) {
									return Promise.reject(new Error("请输入整数"));
								}

								const v = BigInt(value);

								if (v < RANGE.long.min || v > RANGE.long.max) {
									return Promise.reject(
										new Error(
											`请输入 ${RANGE.long.min} ~ ${RANGE.long.max} 之间的整数`
										)
									);
								}

								return Promise.resolve();
							}

							case "float":
							case "double": {
								const regex = new RegExp(
									`^-?\\d+(\\.\\d{1,${precision}})?$`
								);
								if (!regex.test(value)) {
									return Promise.reject(new Error(`请输入正确的 ${type}，最多保留 ${precision} 位小数`));
								}
								const num = Number(value);
								if (Number.isNaN(num)) {
									return Promise.reject(new Error("请输入数字"));
								}
								const floatValue = Math.fround(num);
								if (!Number.isFinite(floatValue)) {
									return Promise.reject(new Error(`超出 ${type} 范围`));
								}
								const range = RANGE[type];
								if (num < range.min || num > range.max) {
									return Promise.reject(
										new Error(`超出 ${type} 范围`)
									);
								}
								return Promise.resolve();
							}

							default:
								return Promise.resolve();
						}
					},
				},
			]}
		/>
	);
}
