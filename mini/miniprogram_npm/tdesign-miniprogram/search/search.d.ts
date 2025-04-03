import { SuperComponent } from '../common/src/index';
export default class Search extends SuperComponent {
    externalClasses: string[];
    options: {
        multipleSlots: boolean;
    };
    properties: import("./type").TdSearchProps;
    observers: {
        resultList(val: any): void;
        'clearTrigger, clearable, disabled, readonly'(): void;
    };
    data: {
        classPrefix: string;
        prefix: string;
        isShowResultList: boolean;
        isSelected: boolean;
        showClearIcon: boolean;
    };
    updateClearIconVisible(value?: boolean): void;
    onInput(e: any): void;
    onFocus(e: any): void;
    onBlur(e: any): void;
    handleClear(): void;
    onConfirm(e: any): void;
    onActionClick(): void;
    onSelectResultItem(e: any): void;
}
