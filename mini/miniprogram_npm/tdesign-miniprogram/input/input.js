var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import { getCharacterLength, calcIcon, isDef } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-input`;
let Input = class Input extends SuperComponent {
    constructor() {
        super(...arguments);
        this.options = {
            multipleSlots: true,
        };
        this.externalClasses = [
            `${prefix}-class`,
            `${prefix}-class-prefix-icon`,
            `${prefix}-class-label`,
            `${prefix}-class-input`,
            `${prefix}-class-clearable`,
            `${prefix}-class-suffix`,
            `${prefix}-class-suffix-icon`,
            `${prefix}-class-tips`,
        ];
        this.behaviors = ['wx://form-field'];
        this.properties = props;
        this.data = {
            prefix,
            classPrefix: name,
            classBasePrefix: prefix,
            showClearIcon: true,
        };
        this.lifetimes = {
            ready() {
                var _a;
                const { value, defaultValue } = this.properties;
                this.updateValue((_a = value !== null && value !== void 0 ? value : defaultValue) !== null && _a !== void 0 ? _a : '');
            },
        };
        this.observers = {
            prefixIcon(v) {
                this.setData({
                    _prefixIcon: calcIcon(v),
                });
            },
            suffixIcon(v) {
                this.setData({
                    _suffixIcon: calcIcon(v),
                });
            },
            clearable(v) {
                this.setData({
                    _clearIcon: calcIcon(v, 'close-circle-filled'),
                });
            },
            'clearTrigger, clearable, disabled, readonly'() {
                this.updateClearIconVisible();
            },
        };
        this.methods = {
            updateValue(value) {
                const { allowInputOverMax, maxcharacter, maxlength } = this.properties;
                if (!allowInputOverMax && maxcharacter && maxcharacter > 0 && !Number.isNaN(maxcharacter)) {
                    const { length, characters } = getCharacterLength('maxcharacter', value, maxcharacter);
                    this.setData({
                        value: characters,
                        count: length,
                    });
                }
                else if (!allowInputOverMax && maxlength && maxlength > 0 && !Number.isNaN(maxlength)) {
                    const { length, characters } = getCharacterLength('maxlength', value, maxlength);
                    this.setData({
                        value: characters,
                        count: length,
                    });
                }
                else {
                    this.setData({
                        value,
                        count: isDef(value) ? String(value).length : 0,
                    });
                }
            },
            updateClearIconVisible(value = false) {
                const { clearTrigger, disabled, readonly } = this.properties;
                if (disabled || readonly) {
                    this.setData({ showClearIcon: false });
                    return;
                }
                this.setData({ showClearIcon: value || clearTrigger === 'always' });
            },
            onInput(e) {
                const { value, cursor, keyCode } = e.detail;
                this.updateValue(value);
                this.triggerEvent('change', { value: this.data.value, cursor, keyCode });
            },
            onFocus(e) {
                this.updateClearIconVisible(true);
                this.triggerEvent('focus', e.detail);
            },
            onBlur(e) {
                this.updateClearIconVisible();
                if (typeof this.properties.format === 'function') {
                    const v = this.properties.format(e.detail.value);
                    this.updateValue(v);
                    this.triggerEvent('blur', { value: this.data.value, cursor: this.data.count });
                    return;
                }
                this.triggerEvent('blur', e.detail);
            },
            onConfirm(e) {
                this.triggerEvent('enter', e.detail);
            },
            onSuffixClick() {
                this.triggerEvent('click', { trigger: 'suffix' });
            },
            onSuffixIconClick() {
                this.triggerEvent('click', { trigger: 'suffix-icon' });
            },
            clearInput(e) {
                this.triggerEvent('clear', e.detail);
                this.setData({ value: '' });
            },
            onKeyboardHeightChange(e) {
                this.triggerEvent('keyboardheightchange', e.detail);
            },
            onNickNameReview(e) {
                this.triggerEvent('nicknamereview', e.detail);
            },
        };
    }
};
Input = __decorate([
    wxComponent()
], Input);
export default Input;
