import { SuperComponent } from '../common/src/index';
import { UploadFile } from './type';
export default class Upload extends SuperComponent {
    externalClasses: string[];
    options: {
        multipleSlots: boolean;
    };
    data: {
        classPrefix: string;
        prefix: string;
        current: boolean;
        proofs: any[];
        customFiles: UploadFile[];
        customLimit: number;
        column: number;
        dragBaseData: {};
        rows: number;
        dragWrapStyle: string;
        dragList: any[];
        dragging: boolean;
        dragLayout: boolean;
    };
    properties: import("./type").TdUploadProps;
    controlledProps: {
        key: string;
        event: string;
    }[];
    observers: {
        'files, max, draggable'(files: UploadFile, max: number): void;
        gridConfig(): void;
    };
    lifetimes: {
        ready(): void;
    };
    onProofTap(e: any): void;
    handleLimit(customFiles: UploadFile[], max: number): void;
    triggerSuccessEvent(files: any): void;
    triggerFailEvent(err: any): void;
    onFileClick(e: any): void;
    getFileType(mediaType: string[], tempFilePath: string, fileType?: string): string;
    getRandFileName(filePath: any): string;
    onDelete(e: any): void;
    deleteHandle(index: number): void;
    updateGrid(): void;
    initDragLayout(): void;
    initDragList(): void;
    initDragBaseData(): void;
    methods: {
        uploadFiles(files: UploadFile[]): Promise<unknown>;
        startUpload(files: UploadFile[]): any;
        onAddTap(): void;
        chooseMedia(mediaType: any): void;
        chooseMessageFile(mediaType: any): void;
        afterSelect(files: any): void;
        dragVibrate(e: any): void;
        dragStatusChange(e: any): void;
        dragEnd(e: any): void;
        triggerDropEvent(files: any): void;
    };
}
