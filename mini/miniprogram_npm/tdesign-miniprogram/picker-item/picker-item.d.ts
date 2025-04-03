import { SuperComponent, RelationsOptions, ComponentsOptionsType } from '../common/src/index';
export default class PickerItem extends SuperComponent {
    relations: RelationsOptions;
    options: ComponentsOptionsType;
    externalClasses: string[];
    properties: import("./type").TdPickerItemProps;
    observers: {
        options(this: PickerItem): void;
    };
    data: {
        prefix: string;
        classPrefix: string;
        offset: number;
        duration: number;
        value: string;
        curIndex: number;
        columnIndex: number;
        labelAlias: string;
        valueAlias: string;
    };
    lifetimes: {
        created(): void;
    };
    methods: {
        onTouchStart(event: any): void;
        onTouchMove(event: any): void;
        onTouchEnd(): void;
        update(): void;
        resetOrigin(): void;
        getCount(): any;
    };
    calculateViewDeltaY(touchDeltaY: number, itemHeight: number): number;
}
