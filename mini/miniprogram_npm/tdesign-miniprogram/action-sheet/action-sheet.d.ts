import { SuperComponent } from '../common/src/index';
export default class ActionSheet extends SuperComponent {
    static show: (options: import("./show").ActionSheetShowOption) => WechatMiniprogram.Component.TrivialInstance;
    behaviors: string[];
    externalClasses: string[];
    properties: {
        align?: {
            type: StringConstructor;
            value?: "center" | "left";
        };
        cancelText?: {
            type: StringConstructor;
            value?: string;
        };
        count?: {
            type: NumberConstructor;
            value?: number;
        };
        description?: {
            type: StringConstructor;
            value?: string;
        };
        items: {
            type: ArrayConstructor;
            value?: (string | import("./type").ActionSheetItem)[];
            required?: boolean;
        };
        popupProps?: {
            type: ObjectConstructor;
            value?: import("../popup").TdPopupProps;
        };
        showCancel?: {
            type: BooleanConstructor;
            value?: boolean;
        };
        showOverlay?: {
            type: BooleanConstructor;
            value?: boolean;
        };
        theme?: {
            type: StringConstructor;
            value?: "list" | "grid";
        };
        usingCustomNavbar?: {
            type: BooleanConstructor;
            value?: boolean;
        };
        visible: {
            type: BooleanConstructor;
            value?: boolean;
            required?: boolean;
        };
        defaultVisible: {
            type: BooleanConstructor;
            value?: boolean;
            required?: boolean;
        };
    };
    data: {
        prefix: string;
        classPrefix: string;
        gridThemeItems: any[];
        currentSwiperIndex: number;
        defaultPopUpProps: {};
        defaultPopUpzIndex: number;
    };
    controlledProps: {
        key: string;
        event: string;
    }[];
    observers: {
        'visible, items'(visible: boolean): void;
    };
    methods: {
        init(): void;
        memoInitialData(): void;
        splitGridThemeActions(): void;
        show(options: any): void;
        close(): void;
        onPopupVisibleChange({ detail }: {
            detail: any;
        }): void;
        onSwiperChange(e: WechatMiniprogram.TouchEvent): void;
        onSelect(event: WechatMiniprogram.TouchEvent): void;
        onCancel(): void;
    };
}
