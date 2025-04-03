var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import { isObject, toCamel } from '../common/utils';
import useCustomNavbar from '../mixins/using-custom-navbar';
const { prefix } = config;
const name = `${prefix}-dialog`;
let Dialog = class Dialog extends SuperComponent {
    constructor() {
        super(...arguments);
        this.behaviors = [useCustomNavbar];
        this.options = {
            multipleSlots: true,
        };
        this.externalClasses = [
            `${prefix}-class`,
            `${prefix}-class-content`,
            `${prefix}-class-confirm`,
            `${prefix}-class-cancel`,
            `${prefix}-class-action`,
        ];
        this.properties = props;
        this.data = {
            prefix,
            classPrefix: name,
            buttonVariant: 'text',
        };
        this.observers = {
            'confirmBtn, cancelBtn'(confirm, cancel) {
                const { prefix, classPrefix, buttonLayout } = this.data;
                const rect = { buttonVariant: 'text' };
                const useBaseVariant = [confirm, cancel].some((item) => isObject(item) && item.variant && item.variant !== 'text');
                const buttonMap = { confirm, cancel };
                const cls = [`${classPrefix}__button`];
                const externalCls = [];
                if (useBaseVariant) {
                    rect.buttonVariant = 'base';
                    cls.push(`${classPrefix}__button--${buttonLayout}`);
                }
                else {
                    cls.push(`${classPrefix}__button--text`);
                    externalCls.push(`${classPrefix}-button`);
                }
                Object.keys(buttonMap).forEach((key) => {
                    const btn = buttonMap[key];
                    const base = {
                        block: true,
                        rootClass: [...cls, `${classPrefix}__button--${key}`],
                        tClass: [...externalCls, `${prefix}-class-${key}`],
                        variant: rect.buttonVariant,
                        openType: '',
                    };
                    if (key === 'cancel' && rect.buttonVariant === 'base') {
                        base.theme = 'light';
                    }
                    if (typeof btn === 'string') {
                        rect[`_${key}`] = Object.assign(Object.assign({}, base), { content: btn });
                    }
                    else if (btn && typeof btn === 'object') {
                        rect[`_${key}`] = Object.assign(Object.assign({}, base), btn);
                    }
                    else {
                        rect[`_${key}`] = null;
                    }
                });
                this.setData(Object.assign({}, rect));
            },
        };
        this.methods = {
            onTplButtonTap(e) {
                var _a, _b, _c;
                const evtType = e.type;
                const { type, extra } = e.target.dataset;
                const button = this.data[`_${type}`];
                const cbName = `bind${evtType}`;
                if (type === 'action') {
                    this.onActionTap(extra);
                    return;
                }
                if (typeof button[cbName] === 'function') {
                    const closeFlag = button[cbName](e);
                    if (closeFlag) {
                        this.close();
                    }
                }
                const hasOpenType = !!button.openType;
                if (!hasOpenType && ['confirm', 'cancel'].includes(type)) {
                    (_a = this[toCamel(`on-${type}`)]) === null || _a === void 0 ? void 0 : _a.call(this, type);
                }
                if (evtType !== 'tap') {
                    const success = ((_c = (_b = e.detail) === null || _b === void 0 ? void 0 : _b.errMsg) === null || _c === void 0 ? void 0 : _c.indexOf('ok')) > -1;
                    this.triggerEvent(success ? 'open-type-event' : 'open-type-error-event', e.detail);
                }
            },
            onConfirm() {
                this.triggerEvent('confirm');
                if (this._onConfirm) {
                    this._onConfirm();
                    this.close();
                }
            },
            onCancel() {
                this.triggerEvent('close', { trigger: 'cancel' });
                this.triggerEvent('cancel');
                if (this._onCancel) {
                    this._onCancel();
                    this.close();
                }
            },
            onClose() {
                this.triggerEvent('close', { trigger: 'close-btn' });
                this.close();
            },
            close() {
                this.setData({ visible: false });
            },
            overlayClick() {
                if (this.properties.closeOnOverlayClick) {
                    this.triggerEvent('close', { trigger: 'overlay' });
                    this.close();
                }
                this.triggerEvent('overlay-click');
            },
            onActionTap(index) {
                this.triggerEvent('action', { index });
                if (this._onAction) {
                    this._onAction({ index });
                    this.close();
                }
            },
            openValueCBHandle(e) {
                this.triggerEvent('open-type-event', e.detail);
            },
            openValueErrCBHandle(e) {
                this.triggerEvent('open-type-error-event', e.detail);
            },
        };
    }
};
Dialog = __decorate([
    wxComponent()
], Dialog);
export default Dialog;
