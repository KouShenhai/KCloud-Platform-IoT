var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import isFunction from 'lodash/isFunction';
import { SuperComponent, wxComponent } from '../common/src/index';
import props from './props';
import config from '../common/config';
import { systemInfo, debounce, getRect, isNumber, rpx2px, styles, unitConvert, nextTick } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-guide`;
let Guide = class Guide extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [
            `${prefix}-class`,
            `${prefix}-class-reference`,
            `${prefix}-class-popover`,
            `${prefix}-class-tooltip`,
            `${prefix}-class-title`,
            `${prefix}-class-body`,
            `${prefix}-class-footer`,
            `${prefix}-class-skip`,
            `${prefix}-class-next`,
            `${prefix}-class-back`,
            `${prefix}-class-finish`,
        ];
        this.properties = props;
        this.options = {
            pureDataPattern: /^_/,
            multipleSlots: true,
        };
        this.data = {
            prefix,
            classPrefix: name,
            visible: false,
            _current: -1,
            _steps: [],
            buttonProps: {},
            referenceStyle: '',
            popoverStyle: '',
            title: '',
            body: '',
            nonOverlay: false,
            modeType: '',
        };
        this.controlledProps = [
            {
                key: 'current',
                event: 'change',
            },
        ];
        this.observers = {
            'steps, current, showOverlay'() {
                return __awaiter(this, void 0, void 0, function* () {
                    this._init();
                });
            },
        };
        this.lifetimes = {
            created() {
                this._init = debounce(() => this.init(), 20);
                this._getPlacement = this.getPlacement();
            },
            attached() {
                this._init();
            },
        };
        this.methods = {
            init() {
                var _a, _b, _c, _d, _e, _f, _g;
                return __awaiter(this, void 0, void 0, function* () {
                    const { steps, current } = this.properties;
                    const { _steps, _current } = this.data;
                    const step = steps[current];
                    if (!step) {
                        return this.setData({ visible: false });
                    }
                    const modeType = ((_a = step.mode) !== null && _a !== void 0 ? _a : this.data.mode) === 'dialog' ? 'dialog' : 'popover';
                    const showOverlay = (_b = step.showOverlay) !== null && _b !== void 0 ? _b : this.data.showOverlay;
                    this.setData({ nonOverlay: !showOverlay, modeType });
                    if (steps === _steps && current === _current)
                        return;
                    if (modeType === 'popover') {
                        const rect = yield step.element();
                        if (!rect)
                            return;
                        const highlightPadding = rpx2px((_c = step.highlightPadding) !== null && _c !== void 0 ? _c : this.data.highlightPadding);
                        const referenceTop = rect.top - highlightPadding;
                        const referenceRight = systemInfo.windowWidth - rect.right - highlightPadding;
                        const referenceBottom = systemInfo.windowHeight - rect.bottom - highlightPadding;
                        const referenceLeft = rect.left - highlightPadding;
                        const style = {
                            top: `${referenceTop}px`,
                            right: `${referenceRight}px`,
                            bottom: `${referenceBottom}px`,
                            left: `${referenceLeft}px`,
                        };
                        this.setData({
                            _steps: this.data.steps,
                            _current: this.data.current,
                            visible: true,
                            referenceStyle: styles(style),
                            title: (_d = step.title) !== null && _d !== void 0 ? _d : '',
                            body: (_e = step.body) !== null && _e !== void 0 ? _e : '',
                            buttonProps: this.buttonProps(step, 'popover'),
                        });
                        const popoverStyle = yield this.placementOffset(step, style);
                        this.setData({ popoverStyle });
                    }
                    else {
                        this.setData({
                            _steps: this.data.steps,
                            _current: this.data.current,
                            visible: true,
                            title: (_f = step.title) !== null && _f !== void 0 ? _f : '',
                            body: (_g = step.body) !== null && _g !== void 0 ? _g : '',
                            buttonProps: this.buttonProps(step, 'dialog'),
                        });
                    }
                });
            },
            placementOffset({ placement, offset }, place) {
                var _a, _b;
                return __awaiter(this, void 0, void 0, function* () {
                    yield nextTick();
                    const rect = yield getRect(this, `.${name}__container`);
                    const style = (_b = (_a = this._getPlacement)[placement]) === null || _b === void 0 ? void 0 : _b.call(_a, rect, place, offset);
                    return styles(Object.assign({ position: 'absolute' }, style));
                });
            },
            buttonProps(step, mode) {
                var _a, _b, _c, _d;
                let skipButton = (_a = step.skipButtonProps) !== null && _a !== void 0 ? _a : this.data.skipButtonProps;
                const size = mode === 'popover' ? 'extra-small' : 'medium';
                skipButton = Object.assign(Object.assign({ theme: 'light', content: '跳过', size }, skipButton), { tClass: `${prefix}-class-skip ${name}__button ${(skipButton === null || skipButton === void 0 ? void 0 : skipButton.class) || ''}`, type: 'skip' });
                let nextButton = (_b = step.nextButtonProps) !== null && _b !== void 0 ? _b : this.data.nextButtonProps;
                nextButton = Object.assign(Object.assign({ theme: 'primary', content: '下一步', size }, nextButton), { tClass: `${prefix}-class-next ${name}__button ${(nextButton === null || nextButton === void 0 ? void 0 : nextButton.class) || ''}`, type: 'next' });
                nextButton = Object.assign(Object.assign({}, nextButton), { content: this.buttonContent(nextButton) });
                let backButton = (_c = step.backButtonProps) !== null && _c !== void 0 ? _c : this.data.backButtonProps;
                backButton = Object.assign(Object.assign({ theme: 'light', content: '返回', size }, backButton), { tClass: `${prefix}-class-back ${name}__button ${(backButton === null || backButton === void 0 ? void 0 : backButton.class) || ''}`, type: 'back' });
                let finishButton = (_d = step.finishButtonProps) !== null && _d !== void 0 ? _d : this.data.finishButtonProps;
                finishButton = Object.assign(Object.assign({ theme: 'primary', content: '完成', size }, finishButton), { tClass: `${prefix}-class-finish ${name}__button ${(finishButton === null || finishButton === void 0 ? void 0 : finishButton.class) || ''}`, type: 'finish' });
                finishButton = Object.assign(Object.assign({}, finishButton), { content: this.buttonContent(finishButton) });
                return {
                    skipButton,
                    nextButton,
                    backButton,
                    finishButton,
                };
            },
            renderCounter() {
                const { steps, current, counter } = this.data;
                const stepsTotal = steps.length;
                const innerCurrent = current + 1;
                const popupSlotCounter = isFunction(counter) ? counter({ total: stepsTotal, current: innerCurrent }) : counter;
                return counter ? popupSlotCounter : `(${innerCurrent}/${stepsTotal})`;
            },
            buttonContent(button) {
                const { hideCounter } = this.data;
                return `${button.content.replace(/ \(.*?\)/, '')} ${hideCounter ? '' : this.renderCounter()}`;
            },
            onTplButtonTap(e) {
                const { type } = e.target.dataset;
                const parmas = { e, current: this.data.current, total: this.data.steps.length };
                switch (type) {
                    case 'next':
                        this.triggerEvent('next-step-click', Object.assign({ next: this.data.current + 1 }, parmas));
                        this.setData({ current: this.data.current + 1 });
                        break;
                    case 'skip':
                        this.triggerEvent('skip', parmas);
                        this.setData({ current: -1 });
                        break;
                    case 'back':
                        this.triggerEvent('back', parmas);
                        this.setData({ current: 0 });
                        break;
                    case 'finish':
                        this.triggerEvent('finish', parmas);
                        this.setData({ current: -1 });
                        break;
                    default:
                        break;
                }
                this.triggerEvent('change', { current: this.data.current });
            },
            getPlacement() {
                const space = rpx2px(32);
                const offsetLeft = (offset) => unitConvert(isNumber(offset === null || offset === void 0 ? void 0 : offset[0]) ? `${offset === null || offset === void 0 ? void 0 : offset[0]}rpx` : (offset === null || offset === void 0 ? void 0 : offset[0]) || 0);
                const offsetTop = (offset) => unitConvert(isNumber(offset === null || offset === void 0 ? void 0 : offset[1]) ? `${offset === null || offset === void 0 ? void 0 : offset[1]}rpx` : (offset === null || offset === void 0 ? void 0 : offset[1]) || 0);
                const bottom = (place) => parseFloat(place.bottom);
                const left = (place) => parseFloat(place.left);
                const right = (place) => parseFloat(place.right);
                const top = (place) => parseFloat(place.top);
                const height = (place) => systemInfo.windowHeight - bottom(place) - top(place);
                const width = (place) => systemInfo.windowWidth - left(place) - right(place);
                return {
                    center: (rect, place, offset) => ({
                        top: `${Math.max(height(place) + top(place) + space + offsetTop(offset), 1)}px`,
                        left: `${Math.max(width(place) / 2 + left(place) - rect.width / 2 + offsetLeft(offset), 1)}px`,
                    }),
                    bottom: (rect, place, offset) => ({
                        top: `${Math.max(height(place) + top(place) + space + offsetTop(offset), 1)}px`,
                        left: `${Math.max(width(place) / 2 + left(place) - rect.width / 2 + offsetLeft(offset), 1)}px`,
                    }),
                    'bottom-left': (rect, place, offset) => ({
                        top: `${Math.max(height(place) + top(place) + space + offsetTop(offset), 1)}px`,
                        left: `${Math.max(left(place) + offsetLeft(offset), 1)}px`,
                    }),
                    'bottom-right': (rect, place, offset) => ({
                        top: `${Math.max(height(place) + top(place) + space + offsetTop(offset), 1)}px`,
                        right: `${Math.max(right(place) - offsetLeft(offset), 1)}px`,
                    }),
                    left: (rect, place, offset) => ({
                        top: `${Math.max(height(place) / 2 + top(place) - rect.height / 2 + offsetTop(offset), 1)}px`,
                        right: `${Math.max(width(place) + right(place) + space - offsetLeft(offset), 1)}px`,
                    }),
                    'left-top': (rect, place, offset) => ({
                        top: `${Math.max(top(place) + offsetTop(offset), 1)}px`,
                        right: `${Math.max(width(place) + right(place) + space - offsetLeft(offset), 1)}px`,
                    }),
                    'left-bottom': (rect, place, offset) => ({
                        bottom: `${Math.max(bottom(place) - offsetTop(offset), 1)}px`,
                        right: `${Math.max(width(place) + right(place) + space - offsetLeft(offset), 1)}px`,
                    }),
                    right: (rect, place, offset) => ({
                        top: `${Math.max(height(place) / 2 + top(place) - rect.height / 2 + offsetTop(offset), 1)}px`,
                        left: `${Math.max(left(place) + width(place) + space + offsetLeft(offset), 1)}px`,
                    }),
                    'right-top': (rect, place, offset) => ({
                        top: `${Math.max(top(place) + offsetTop(offset), 1)}px`,
                        left: `${Math.max(left(place) + width(place) + space + offsetLeft(offset), 1)}px`,
                    }),
                    'right-bottom': (rect, place, offset) => ({
                        bottom: `${Math.max(bottom(place) - offsetTop(offset), 1)}px`,
                        left: `${Math.max(left(place) + width(place) + space + offsetLeft(offset), 1)}px`,
                    }),
                    top: (rect, place, offset) => ({
                        bottom: `${Math.max(height(place) + bottom(place) + space - offsetTop(offset), 1)}px`,
                        left: `${Math.max(width(place) / 2 + left(place) - rect.width / 2 + offsetLeft(offset), 1)}px`,
                    }),
                    'top-left': (rect, place, offset) => ({
                        bottom: `${Math.max(height(place) + bottom(place) + space - offsetTop(offset), 1)}px`,
                        left: `${Math.max(left(place) + offsetLeft(offset), 1)}px`,
                    }),
                    'top-right': (rect, place, offset) => ({
                        bottom: `${Math.max(height(place) + bottom(place) + space - offsetTop(offset), 1)}px`,
                        right: `${Math.max(right(place) - offsetLeft(offset), 1)}px`,
                    }),
                };
            },
        };
    }
};
Guide = __decorate([
    wxComponent()
], Guide);
export default Guide;
