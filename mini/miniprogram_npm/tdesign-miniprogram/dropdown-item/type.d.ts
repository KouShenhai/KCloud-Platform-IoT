import { KeysType } from '../common/common';
export interface TdDropdownItemProps {
    disabled?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    externalClasses?: {
        type: ArrayConstructor;
        value?: [
            't-class',
            't-class-content',
            't-class-column',
            't-class-column-item',
            't-class-column-item-label',
            't-class-footer'
        ];
    };
    keys?: {
        type: ObjectConstructor;
        value?: KeysType;
    };
    label?: {
        type: StringConstructor;
        value?: string;
    };
    multiple?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    options?: {
        type: ArrayConstructor;
        value?: Array<DropdownOption>;
    };
    optionsColumns?: {
        type: null;
        value?: string | number;
    };
    placement?: {
        type: StringConstructor;
        value?: 'left' | 'right';
    };
    value?: {
        type: null;
        value?: DropdownValue;
    };
    defaultValue?: {
        type: null;
        value?: DropdownValue;
    };
}
export interface DropdownOption {
    label: string;
    disabled: boolean;
    value: DropdownValue;
}
export declare type DropdownValue = string | number | Array<DropdownValue>;
