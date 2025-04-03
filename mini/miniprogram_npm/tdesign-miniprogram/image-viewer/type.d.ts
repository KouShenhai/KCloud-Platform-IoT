export interface TdImageViewerProps {
    backgroundColor?: {
        type: StringConstructor;
        value?: string;
    };
    closeBtn?: {
        type: null;
        value?: string | boolean | object;
    };
    deleteBtn?: {
        type: null;
        value?: string | boolean | object;
    };
    images?: {
        type: ArrayConstructor;
        value?: Array<string>;
    };
    initialIndex?: {
        type: NumberConstructor;
        value?: Number;
    };
    showIndex?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    usingCustomNavbar?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    visible?: {
        type: BooleanConstructor;
        value?: boolean;
    };
    defaultVisible?: {
        type: BooleanConstructor;
        value?: boolean;
    };
}
