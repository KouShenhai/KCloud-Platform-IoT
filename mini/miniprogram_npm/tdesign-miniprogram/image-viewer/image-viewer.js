var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { styles, calcIcon, systemInfo } from '../common/utils';
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
const { prefix } = config;
const name = `${prefix}-image-viewer`;
let ImageViewer = class ImageViewer extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`];
        this.properties = Object.assign({}, props);
        this.data = {
            prefix,
            classPrefix: name,
            currentSwiperIndex: 0,
            windowHeight: 0,
            windowWidth: 0,
            swiperStyle: {},
            imagesStyle: {},
            maskTop: 0,
        };
        this.options = {
            multipleSlots: true,
        };
        this.controlledProps = [
            {
                key: 'visible',
                event: 'close',
            },
        ];
        this.observers = {
            'visible,initialIndex,images'(visible, initialIndex, images) {
                if (visible && (images === null || images === void 0 ? void 0 : images.length)) {
                    this.setData({
                        currentSwiperIndex: initialIndex >= images.length ? images.length - 1 : initialIndex,
                    });
                }
            },
            closeBtn(v) {
                this.setData({
                    _closeBtn: calcIcon(v, 'close'),
                });
            },
            deleteBtn(v) {
                this.setData({
                    _deleteBtn: calcIcon(v, 'delete'),
                });
            },
        };
        this.methods = {
            calcMaskTop() {
                if (this.data.usingCustomNavbar) {
                    const rect = (wx === null || wx === void 0 ? void 0 : wx.getMenuButtonBoundingClientRect()) || null;
                    const { statusBarHeight } = systemInfo;
                    if (rect && statusBarHeight) {
                        this.setData({
                            maskTop: rect.top - statusBarHeight + rect.bottom,
                        });
                    }
                }
            },
            saveScreenSize() {
                const { windowHeight, windowWidth } = systemInfo;
                this.setData({
                    windowHeight,
                    windowWidth,
                });
            },
            calcImageDisplayStyle(imageWidth, imageHeight) {
                const { windowWidth, windowHeight } = this.data;
                const ratio = imageWidth / imageHeight;
                if (imageWidth < windowWidth && imageHeight < windowHeight) {
                    return {
                        styleObj: {
                            width: `${imageWidth * 2}rpx`,
                            height: `${imageHeight * 2}rpx`,
                            left: '50%',
                            transform: 'translate(-50%, -50%)',
                        },
                    };
                }
                if (ratio >= 1) {
                    return {
                        styleObj: {
                            width: '100vw',
                            height: `${(windowWidth / ratio) * 2}rpx`,
                        },
                    };
                }
                const scaledHeight = ratio * windowHeight * 2;
                if (scaledHeight < windowWidth) {
                    return {
                        styleObj: {
                            width: `${scaledHeight}rpx`,
                            height: '100vh',
                            left: '50%',
                            transform: 'translate(-50%, -50%)',
                        },
                    };
                }
                return {
                    styleObj: {
                        width: '100vw',
                        height: `${(windowWidth / imageWidth) * imageHeight * 2}rpx`,
                    },
                };
            },
            onImageLoadSuccess(e) {
                const { detail: { width, height }, currentTarget: { dataset: { index }, }, } = e;
                const { mode, styleObj } = this.calcImageDisplayStyle(width, height);
                const originImagesStyle = this.data.imagesStyle;
                const originSwiperStyle = this.data.swiperStyle;
                this.setData({
                    swiperStyle: Object.assign(Object.assign({}, originSwiperStyle), { [index]: {
                            style: `height: ${styleObj.height}`,
                        } }),
                    imagesStyle: Object.assign(Object.assign({}, originImagesStyle), { [index]: {
                            mode,
                            style: styles(Object.assign({}, styleObj)),
                        } }),
                });
            },
            onSwiperChange(e) {
                const { detail: { current }, } = e;
                this.setData({
                    currentSwiperIndex: current,
                });
                this._trigger('change', { index: current });
            },
            onClose(e) {
                const { source } = e.currentTarget.dataset;
                this._trigger('close', { visible: false, trigger: source || 'button', index: this.data.currentSwiperIndex });
            },
            onDelete() {
                this._trigger('delete', { index: this.data.currentSwiperIndex });
            },
        };
    }
    ready() {
        this.saveScreenSize();
        this.calcMaskTop();
    }
};
ImageViewer = __decorate([
    wxComponent()
], ImageViewer);
export default ImageViewer;
