import { SuperComponent } from '../common/src/index';
export default class ImageViewer extends SuperComponent {
    externalClasses: string[];
    properties: {
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
            value?: string[];
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
    };
    data: {
        prefix: string;
        classPrefix: string;
        currentSwiperIndex: number;
        windowHeight: number;
        windowWidth: number;
        swiperStyle: {};
        imagesStyle: {};
        maskTop: number;
    };
    options: {
        multipleSlots: boolean;
    };
    controlledProps: {
        key: string;
        event: string;
    }[];
    ready(): void;
    observers: {
        'visible,initialIndex,images'(visible: any, initialIndex: any, images: any): void;
        closeBtn(v: any): void;
        deleteBtn(v: any): void;
    };
    methods: {
        calcMaskTop(): void;
        saveScreenSize(): void;
        calcImageDisplayStyle(imageWidth: any, imageHeight: any): {
            styleObj: {
                width: string;
                height: string;
                left: string;
                transform: string;
            };
        } | {
            styleObj: {
                width: string;
                height: string;
                left?: undefined;
                transform?: undefined;
            };
        };
        onImageLoadSuccess(e: WechatMiniprogram.TouchEvent): void;
        onSwiperChange(e: WechatMiniprogram.TouchEvent): void;
        onClose(e: any): void;
        onDelete(): void;
    };
}
