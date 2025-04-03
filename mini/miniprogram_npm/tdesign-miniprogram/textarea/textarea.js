var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import { getCharacterLength } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-textarea`;
let Textarea = class Textarea extends SuperComponent {
    constructor() {
        super(...arguments);
        this.options = {
            multipleSlots: true,
        };
        this.behaviors = ['wx://form-field'];
        this.externalClasses = [
            `${prefix}-class`,
            `${prefix}-class-textarea`,
            `${prefix}-class-label`,
            `${prefix}-class-indicator`,
        ];
        this.properties = props;
        this.data = {
            prefix,
            classPrefix: name,
            count: 0,
        };
        this.observers = {
            value(val) {
                this.updateCount(val !== null && val !== void 0 ? val : this.properties.defaultValue);
            },
        };
        this.lifetimes = {
            ready() {
                var _a;
                const { value, defaultValue } = this.properties;
                this.updateValue((_a = value !== null && value !== void 0 ? value : defaultValue) !== null && _a !== void 0 ? _a : '');
            },
        };
        this.methods = {
            updateCount(val) {
                const { maxcharacter, maxlength } = this.properties;
                const { count } = this.calculateValue(val, maxcharacter, maxlength);
                this.setData({
                    count,
                });
            },
            updateValue(val) {
                const { maxcharacter, maxlength } = this.properties;
                const { value, count } = this.calculateValue(val, maxcharacter, maxlength);
                this.setData({
                    value,
                    count,
                });
            },
            calculateValue(value, maxcharacter, maxlength) {
                const { allowInputOverMax } = this.properties;
                if (maxcharacter > 0 && !Number.isNaN(maxcharacter)) {
                    const { length, characters } = getCharacterLength('maxcharacter', value, allowInputOverMax ? Infinity : maxcharacter);
                    return {
                        value: characters,
                        count: length,
                    };
                }
                if (maxlength > 0 && !Number.isNaN(maxlength)) {
                    const { length, characters } = getCharacterLength('maxlength', value, allowInputOverMax ? Infinity : maxlength);
                    return {
                        value: characters,
                        count: length,
                    };
                }
                return {
                    value,
                    count: value ? String(value).length : 0,
                };
            },
            onInput(event) {
                const { value, cursor } = event.detail;
                this.updateValue(value);
                this.triggerEvent('change', { value: this.data.value, cursor });
            },
            onFocus(event) {
                this.triggerEvent('focus', Object.assign({}, event.detail));
            },
            onBlur(event) {
                this.triggerEvent('blur', Object.assign({}, event.detail));
            },
            onConfirm(event) {
                this.triggerEvent('enter', Object.assign({}, event.detail));
            },
            onLineChange(event) {
                this.triggerEvent('line-change', Object.assign({}, event.detail));
            },
            onKeyboardHeightChange(e) {
                this.triggerEvent('keyboardheightchange', e.detail);
            },
        };
    }
};
Textarea = __decorate([
    wxComponent()
], Textarea);
export default Textarea;
