import { PopupProps } from '../popup/index';
export interface TdColorPickerProps {
    autoClose?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    enableAlpha?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    fixed?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    format?: {
        type: StringConstructor;
        value?: 'RGB' | 'RGBA' | 'HSL' | 'HSLA' | 'HSB' | 'HSV' | 'HSVA' | 'HEX' | 'CMYK' | 'CSS';
    };
    popupProps?: {
        type: ObjectConstructor;
        value?: PopupProps;
    };
    swatchColors?: {
        type: ArrayConstructor;
        value?: Array<string> | null;
    };
    type?: {
        type: StringConstructor;
        value?: TypeEnum;
    };
    usePopup?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    value?: {
        type: StringConstructor;
        value?: string;
    };
    defaultValue?: {
        type: StringConstructor;
        value?: string;
    };
    visible?: {
        type: BooleanConstructor;
        value?: boolean;
    };
}
export declare type TypeEnum = 'base' | 'multiple';
