var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __rest = (this && this.__rest) || function (s, e) {
    var t = {};
    for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0)
        t[p] = s[p];
    if (s != null && typeof Object.getOwnPropertySymbols === "function")
        for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
            if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i]))
                t[p[i]] = s[p[i]];
        }
    return t;
};
import { isObject, SuperComponent, wxComponent } from '../common/src/index';
import props from './props';
import config from '../common/config';
import { isOverSize } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-upload`;
let Upload = class Upload extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`];
        this.options = {
            multipleSlots: true,
        };
        this.data = {
            classPrefix: name,
            prefix,
            current: false,
            proofs: [],
            customFiles: [],
            customLimit: 0,
            column: 4,
            dragBaseData: {},
            rows: 0,
            dragWrapStyle: '',
            dragList: [],
            dragging: true,
            dragLayout: false,
        };
        this.properties = props;
        this.controlledProps = [
            {
                key: 'files',
                event: 'success',
            },
        ];
        this.observers = {
            'files, max, draggable'(files, max) {
                this.handleLimit(files, max);
            },
            gridConfig() {
                this.updateGrid();
            },
        };
        this.lifetimes = {
            ready() {
                this.handleLimit(this.data.customFiles, this.data.max);
                this.updateGrid();
            },
        };
        this.methods = {
            uploadFiles(files) {
                return new Promise((resolve) => {
                    const task = this.data.requestMethod(files);
                    if (task instanceof Promise) {
                        return task;
                    }
                    resolve({});
                });
            },
            startUpload(files) {
                if (typeof this.data.requestMethod === 'function') {
                    return this.uploadFiles(files)
                        .then(() => {
                        files.forEach((file) => {
                            file.percent = 100;
                        });
                        this.triggerSuccessEvent(files);
                    })
                        .catch((err) => {
                        this.triggerFailEvent(err);
                    });
                }
                this.triggerSuccessEvent(files);
                this.handleLimit(this.data.customFiles, this.data.max);
                return Promise.resolve();
            },
            onAddTap() {
                const { disabled, mediaType, source } = this.properties;
                if (disabled)
                    return;
                if (source === 'media') {
                    this.chooseMedia(mediaType);
                }
                else {
                    this.chooseMessageFile(mediaType);
                }
            },
            chooseMedia(mediaType) {
                const { config, sizeLimit, customLimit } = this.data;
                wx.chooseMedia(Object.assign(Object.assign({ count: customLimit, mediaType }, config), { success: (res) => {
                        const files = [];
                        res.tempFiles.forEach((temp) => {
                            const { size, fileType, tempFilePath, width, height, duration, thumbTempFilePath } = temp, res = __rest(temp, ["size", "fileType", "tempFilePath", "width", "height", "duration", "thumbTempFilePath"]);
                            if (isOverSize(size, sizeLimit)) {
                                let title = `${fileType === 'image' ? '图片' : '视频'}大小超过限制`;
                                if (typeof sizeLimit !== 'number') {
                                    title = sizeLimit.message.replace('{sizeLimit}', sizeLimit === null || sizeLimit === void 0 ? void 0 : sizeLimit.size);
                                }
                                wx.showToast({ icon: 'none', title });
                                return;
                            }
                            const name = this.getRandFileName(tempFilePath);
                            files.push(Object.assign({ name, type: this.getFileType(mediaType, tempFilePath, fileType), url: tempFilePath, size: size, width: width, height: height, duration: duration, thumb: thumbTempFilePath, percent: 0 }, res));
                        });
                        this.afterSelect(files);
                    }, fail: (err) => {
                        this.triggerFailEvent(err);
                    }, complete: (res) => {
                        this.triggerEvent('complete', res);
                    } }));
            },
            chooseMessageFile(mediaType) {
                const { max, config, sizeLimit } = this.properties;
                wx.chooseMessageFile(Object.assign(Object.assign({ count: max, type: Array.isArray(mediaType) ? 'all' : mediaType }, config), { success: (res) => {
                        const files = [];
                        res.tempFiles.forEach((temp) => {
                            const { size, type: fileType, path: tempFilePath } = temp, res = __rest(temp, ["size", "type", "path"]);
                            if (isOverSize(size, sizeLimit)) {
                                let title = `${fileType === 'image' ? '图片' : '视频'}大小超过限制`;
                                if (typeof sizeLimit !== 'number') {
                                    title = sizeLimit.message.replace('{sizeLimit}', sizeLimit === null || sizeLimit === void 0 ? void 0 : sizeLimit.size);
                                }
                                wx.showToast({ icon: 'none', title });
                                return;
                            }
                            const name = this.getRandFileName(tempFilePath);
                            files.push(Object.assign({ name, type: this.getFileType(mediaType, tempFilePath, fileType), url: tempFilePath, size: size, percent: 0 }, res));
                        });
                        this.afterSelect(files);
                    }, fail: (err) => this.triggerFailEvent(err), complete: (res) => this.triggerEvent('complete', res) }));
            },
            afterSelect(files) {
                this._trigger('select-change', {
                    files: [...this.data.customFiles],
                    currentSelectedFiles: [files],
                });
                this._trigger('add', { files });
                this.startUpload(files);
            },
            dragVibrate(e) {
                var _a;
                const { vibrateType } = e;
                const { draggable } = this.data;
                const dragVibrate = (_a = draggable === null || draggable === void 0 ? void 0 : draggable.vibrate) !== null && _a !== void 0 ? _a : true;
                const dragCollisionVibrate = draggable === null || draggable === void 0 ? void 0 : draggable.collisionVibrate;
                if ((dragVibrate && vibrateType === 'longPress') || (dragCollisionVibrate && vibrateType === 'touchMove')) {
                    wx.vibrateShort({
                        type: 'light',
                    });
                }
            },
            dragStatusChange(e) {
                const { dragging } = e;
                this.setData({ dragging });
            },
            dragEnd(e) {
                const { dragCollisionList } = e;
                let files = [];
                if (dragCollisionList.length === 0) {
                    files = this.data.customFiles;
                }
                else {
                    files = dragCollisionList.reduce((list, item) => {
                        const { realKey, data, fixed } = item;
                        if (!fixed) {
                            list[realKey] = Object.assign({}, data);
                        }
                        return list;
                    }, []);
                }
                this.triggerDropEvent(files);
            },
            triggerDropEvent(files) {
                const { transition } = this.properties;
                if (transition.backTransition) {
                    const timer = setTimeout(() => {
                        this.triggerEvent('drop', { files });
                        clearTimeout(timer);
                    }, transition.duration);
                }
                else {
                    this.triggerEvent('drop', { files });
                }
            },
        };
    }
    onProofTap(e) {
        var _a;
        this.onFileClick(e);
        const { index } = e.currentTarget.dataset;
        wx.previewImage({
            urls: this.data.customFiles.filter((file) => file.percent !== -1).map((file) => file.url),
            current: (_a = this.data.customFiles[index]) === null || _a === void 0 ? void 0 : _a.url,
        });
    }
    handleLimit(customFiles, max) {
        if (max === 0) {
            max = 20;
        }
        this.setData({
            customFiles: customFiles.length > max ? customFiles.slice(0, max) : customFiles,
            customLimit: max - customFiles.length,
            dragging: true,
        });
        this.initDragLayout();
    }
    triggerSuccessEvent(files) {
        this._trigger('success', { files: [...this.data.customFiles, ...files] });
    }
    triggerFailEvent(err) {
        this.triggerEvent('fail', err);
    }
    onFileClick(e) {
        const { file } = e.currentTarget.dataset;
        this.triggerEvent('click', { file });
    }
    getFileType(mediaType, tempFilePath, fileType) {
        if (fileType)
            return fileType;
        if (mediaType.length === 1) {
            return mediaType[0];
        }
        const videoType = ['avi', 'wmv', 'mkv', 'mp4', 'mov', 'rm', '3gp', 'flv', 'mpg', 'rmvb'];
        const temp = tempFilePath.split('.');
        const postfix = temp[temp.length - 1];
        if (videoType.includes(postfix.toLocaleLowerCase())) {
            return 'video';
        }
        return 'image';
    }
    getRandFileName(filePath) {
        const extIndex = filePath.lastIndexOf('.');
        const extName = extIndex === -1 ? '' : filePath.substr(extIndex);
        return parseInt(`${Date.now()}${Math.floor(Math.random() * 900 + 100)}`, 10).toString(36) + extName;
    }
    onDelete(e) {
        const { index } = e.currentTarget.dataset;
        this.deleteHandle(index);
    }
    deleteHandle(index) {
        const { customFiles } = this.data;
        const delFile = customFiles[index];
        this.triggerEvent('remove', { index, file: delFile });
    }
    updateGrid() {
        let { gridConfig = {} } = this.properties;
        if (!isObject(gridConfig))
            gridConfig = {};
        const { column = 4, width = 160, height = 160 } = gridConfig;
        this.setData({
            gridItemStyle: `width:${width}rpx;height:${height}rpx`,
            column: column,
        });
    }
    initDragLayout() {
        const { draggable, disabled } = this.properties;
        if (!draggable || disabled)
            return;
        this.initDragList();
        this.initDragBaseData();
    }
    initDragList() {
        let i = 0;
        const { column, customFiles, customLimit } = this.data;
        const dragList = [];
        customFiles.forEach((item, index) => {
            dragList.push({
                realKey: i,
                sortKey: index,
                tranX: `${(index % column) * 100}%`,
                tranY: `${Math.floor(index / column) * 100}%`,
                data: Object.assign({}, item),
            });
            i += 1;
        });
        if (customLimit > 0) {
            const listLength = dragList.length;
            dragList.push({
                realKey: listLength,
                sortKey: listLength,
                tranX: `${(listLength % column) * 100}%`,
                tranY: `${Math.floor(listLength / column) * 100}%`,
                fixed: true,
            });
        }
        this.data.rows = Math.ceil(dragList.length / column);
        this.setData({
            dragList,
        });
    }
    initDragBaseData() {
        const { classPrefix, rows, column, customFiles } = this.data;
        if (customFiles.length === 0) {
            this.setData({
                dragBaseData: {},
                dragWrapStyle: '',
                dragLayout: false,
            });
            return;
        }
        const query = this.createSelectorQuery();
        const selectorGridItem = `.${classPrefix} >>> .t-grid-item`;
        const selectorGrid = `.${classPrefix} >>> .t-grid`;
        query.select(selectorGridItem).boundingClientRect();
        query.select(selectorGrid).boundingClientRect();
        query.selectViewport().scrollOffset();
        query.exec((res) => {
            const [{ width, height }, { left, top }, { scrollTop }] = res;
            const dragBaseData = {
                rows,
                classPrefix,
                itemWidth: width,
                itemHeight: height,
                wrapLeft: left,
                wrapTop: top + scrollTop,
                columns: column,
            };
            const dragWrapStyle = `height: ${rows * height}px`;
            this.setData({
                dragBaseData,
                dragWrapStyle,
                dragLayout: true,
            }, () => {
                const timer = setTimeout(() => {
                    this.setData({ dragging: false });
                    clearTimeout(timer);
                }, 0);
            });
        });
    }
};
Upload = __decorate([
    wxComponent()
], Upload);
export default Upload;
