import React from 'react';
import { Tag } from 'antd';
import { ProSchemaValueEnumType } from '@ant-design/pro-components';
import { DefaultOptionType } from 'antd/es/select';

/* *
 *
 * @author whiteshader@163.com
 * @datetime  2023/02/10
 *
 * */

export interface DictValueEnumType extends ProSchemaValueEnumType {
    id?: string | number;
    key?: string | number;
    value: string | number;
    label: string;
    listClass?: string;
}

export interface DictOptionType extends DefaultOptionType {
    id?: string | number;
    key?: string | number;
    text: string;
    listClass?: string;
}


export type DictValueEnumObj = Record<string | number, DictValueEnumType>;

export type DictTagProps = {
    key?: string;
    value?: string | number;
    enums?: DictValueEnumObj;
    options?: DictOptionType[];
};

const DictTag: React.FC<DictTagProps> = (props) => {
    function getDictColor(type?: string) {
        switch (type) {
            case 'primary':
                return 'blue';
            case 'success':
                return 'success';
            case 'info':
                return 'green';
            case 'warning':
                return 'warning';
            case 'danger':
                return 'error';
            case 'default':
            default:
                return 'default';
        }
    }

    function getDictLabelByValue(value: string | number | undefined): string {
        if (value === undefined) {
            return '';
        }
        if (props.enums) {
            const item = props.enums[value];
            return item.label;
        }
        if (props.options) {
            if (!Array.isArray(props.options)) {
                console.log('DictTag options is no array!')
                return '';
            }
            for (const item of props.options) {
                if (item.value === value) {
                    return item.text;
                }
            }
        }
        return String(props.value);
    }

    function getDictListClassByValue(value: string | number | undefined): string {
        if (value === undefined) {
            return 'default';
        }
        if (props.enums) {
            const item = props.enums[value];
            return item.listClass || 'default';
        }
        if (props.options) {
            if (!Array.isArray(props.options)) {
                console.log('DictTag options is no array!')
                return 'default';
            }
            for (const item of props.options) {
                if (item.value === value) {
                    return item.listClass || 'default';
                }
            }
        }
        return String(props.value);
    }

    const getTagColor = () => {
        return getDictColor(getDictListClassByValue(props.value).toLowerCase());
    };

    const getTagText = (): string => {
        return getDictLabelByValue(props.value);
    };

    return (
        <Tag color={getTagColor()}>{getTagText()}</Tag>
    )
}


export default DictTag;