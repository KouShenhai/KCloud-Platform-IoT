import React from "react";
// @ts-ignore
import { ProFormText, ProFormTextProps } from "@ant-design/pro-components";

export type NumberType = "int" | "long";

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
		min: BigInt("-100000000"),
		max: BigInt("100000000"),
	},
	long: {
		min: BigInt("-10000000000000000"),
		max: BigInt("10000000000000000"),
	}
} as const;

export default function ProFormNumber({
	                                      label,
										  name,
										  type,
										  readonly,
										  disabled,
										  precision = 0,
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

							default:
								return Promise.resolve();
						}
					},
				},
			]}
		/>
	);
}
