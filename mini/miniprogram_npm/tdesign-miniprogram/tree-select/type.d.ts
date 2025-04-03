import { TreeOptionData, KeysType } from '../common/common';
export interface TdTreeSelectProps<DataOption extends TreeOptionData = TreeOptionData> {
    height?: {
        type: null;
        value?: string | number;
    };
    keys?: {
        type: ObjectConstructor;
        value?: KeysType;
    };
    multiple?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    options?: {
        type: ArrayConstructor;
        value?: Array<DataOption>;
    };
    value?: {
        type: null;
        value?: TreeSelectValue;
    };
    defaultValue?: {
        type: null;
        value?: TreeSelectValue;
    };
}
export declare type TreeSelectValue = string | number | Array<TreeSelectValue>;
