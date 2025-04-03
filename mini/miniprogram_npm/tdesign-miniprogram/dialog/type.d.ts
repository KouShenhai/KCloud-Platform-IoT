import { ButtonProps } from '../button/index';
import { TdOverlayProps as OverlayProps } from '../overlay/type';
export interface TdDialogProps {
    actions?: {
        type: ArrayConstructor;
        value?: Array<ButtonProps>;
    };
    buttonLayout?: {
        type: StringConstructor;
        value?: 'horizontal' | 'vertical';
    };
    cancelBtn?: {
        type: null;
        value?: string | ButtonProps | null;
    };
    closeBtn?: {
        type: null;
        value?: boolean | ButtonProps | null;
    };
    closeOnOverlayClick?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    confirmBtn?: {
        type: null;
        value?: string | ButtonProps | null;
    };
    content?: {
        type: StringConstructor;
        value?: string;
    };
    externalClasses?: {
        type: ArrayConstructor;
        value?: ['t-class', 't-class-content', 't-class-confirm', 't-class-cancel'];
    };
    overlayProps?: {
        type: ObjectConstructor;
        value?: OverlayProps;
    };
    preventScrollThrough?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    showOverlay?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    style?: {
        type: StringConstructor;
        value?: string;
    };
    title?: {
        type: StringConstructor;
        value?: string;
    };
    usingCustomNavbar?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    visible?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    zIndex?: {
        type: NumberConstructor;
        value?: number;
    };
}
