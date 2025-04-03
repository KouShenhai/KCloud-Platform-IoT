import { SuperComponent } from '../common/src/index';
import type { TreeOptionData } from '../common/common';
export default class TreeSelect extends SuperComponent {
    externalClasses: string[];
    options: {
        multipleSlots: boolean;
    };
    data: {
        prefix: string;
        classPrefix: string;
        scrollIntoView: any;
    };
    properties: {
        customValue: {
            type: any;
            value: any;
        };
        height?: {
            type: null;
            value?: string | number;
        };
        keys?: {
            type: ObjectConstructor;
            value?: import("../common/common").KeysType;
        };
        multiple?: {
            type: BooleanConstructor;
            value?: boolean;
        };
        options?: {
            type: ArrayConstructor;
            value?: TreeOptionData<string | number>[];
        };
        value?: {
            type: null;
            value?: import("./type").TreeSelectValue;
        };
        defaultValue?: {
            type: null;
            value?: import("./type").TreeSelectValue;
        };
    };
    controlledProps: {
        key: string;
        event: string;
    }[];
    observers: {
        'value, customValue, options, keys, multiple'(): void;
    };
    lifetimes: {
        ready(): void;
    };
    methods: {
        buildTreeOptions(): void;
        getScrollIntoView(status: string): void;
        onRootChange(e: any): void;
        handleTreeClick(e: any): void;
        handleRadioChange(e: any): void;
    };
}
