import { SuperComponent } from '../common/src/index';
import { SkeletonRowColObj } from './type';
import { ClassName, Styles } from '../common/common';
export default class Skeleton extends SuperComponent {
    externalClasses: string[];
    properties: import("./type").TdSkeletonProps;
    data: {
        prefix: string;
        classPrefix: string;
        parsedRowcols: any[];
    };
    observers: {
        rowCol(): void;
        'loading, delay'(): void;
    };
    lifetimes: {
        attached(): void;
    };
    methods: {
        init(): void;
        getColItemClass(obj: SkeletonRowColObj): ClassName;
        getColItemStyle(obj: SkeletonRowColObj): Styles;
        isShowSkeleton(): void;
    };
}
