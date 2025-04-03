import { ComponentsOptionsType, SuperComponent } from '../common/src/index';
export default class Drawer extends SuperComponent {
    behaviors: string[];
    externalClasses: any[];
    options: ComponentsOptionsType;
    properties: import("./type").TdDrawerProps;
    data: {
        classPrefix: string;
    };
    methods: {
        visibleChange({ detail }: {
            detail: any;
        }): void;
        itemClick(detail: any): void;
    };
}
