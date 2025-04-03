var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
const { prefix } = config;
const name = `${prefix}-stepper`;
let Stepper = class Stepper extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`, `${prefix}-class-input`, `${prefix}-class-minus`, `${prefix}-class-plus`];
        this.properties = Object.assign({}, props);
        this.controlledProps = [
            {
                key: 'value',
                event: 'change',
            },
        ];
        this.observers = {
            value(v) {
                this.preValue = Number(v);
                this.setData({
                    currentValue: this.format(Number(v)),
                });
            },
        };
        this.data = {
            currentValue: 0,
            classPrefix: name,
            prefix,
        };
        this.lifetimes = {
            attached() {
                const { value, min } = this.properties;
                this.setData({
                    currentValue: value ? Number(value) : min,
                });
            },
        };
        this.methods = {
            isDisabled(type) {
                const { min, max, disabled } = this.properties;
                const { currentValue } = this.data;
                if (disabled) {
                    return true;
                }
                if (type === 'minus' && currentValue <= min) {
                    return true;
                }
                if (type === 'plus' && currentValue >= max) {
                    return true;
                }
                return false;
            },
            getLen(num) {
                const numStr = num.toString();
                return numStr.indexOf('.') === -1 ? 0 : numStr.split('.')[1].length;
            },
            add(a, b) {
                const maxLen = Math.max(this.getLen(a), this.getLen(b));
                const base = Math.pow(10, maxLen);
                return Math.round(a * base + b * base) / base;
            },
            format(value) {
                const { min, max, step } = this.properties;
                const len = Math.max(this.getLen(step), this.getLen(value));
                return Math.max(Math.min(max, value, Number.MAX_SAFE_INTEGER), min, Number.MIN_SAFE_INTEGER).toFixed(len);
            },
            setValue(value) {
                value = this.format(value);
                if (this.preValue === value)
                    return;
                this.preValue = value;
                this._trigger('change', { value: Number(value) });
            },
            minusValue() {
                if (this.isDisabled('minus')) {
                    this.triggerEvent('overlimit', { type: 'minus' });
                    return false;
                }
                const { currentValue, step } = this.data;
                this.setValue(this.add(currentValue, -step));
            },
            plusValue() {
                if (this.isDisabled('plus')) {
                    this.triggerEvent('overlimit', { type: 'plus' });
                    return false;
                }
                const { currentValue, step } = this.data;
                this.setValue(this.add(currentValue, step));
            },
            filterIllegalChar(value) {
                const v = String(value).replace(/[^0-9.]/g, '');
                const indexOfDot = v.indexOf('.');
                if (this.properties.integer && indexOfDot !== -1) {
                    return v.split('.')[0];
                }
                if (!this.properties.integer && indexOfDot !== -1 && indexOfDot !== v.lastIndexOf('.')) {
                    return v.split('.', 2).join('.');
                }
                return v;
            },
            handleFocus(e) {
                const { value } = e.detail;
                this.triggerEvent('focus', { value });
            },
            handleInput(e) {
                const { value } = e.detail;
                if (value === '') {
                    return;
                }
                const formatted = this.filterIllegalChar(value);
                this.setData({
                    currentValue: formatted,
                });
                this.triggerEvent('input', { value: formatted });
            },
            handleBlur(e) {
                const { value: rawValue } = e.detail;
                const value = this.format(rawValue);
                this.setValue(value);
                this.triggerEvent('blur', { value });
            },
        };
    }
};
Stepper = __decorate([
    wxComponent()
], Stepper);
export default Stepper;
