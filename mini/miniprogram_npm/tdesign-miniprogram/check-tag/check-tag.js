var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { wxComponent, SuperComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import { classNames, calcIcon } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-tag`;
let CheckTag = class CheckTag extends SuperComponent {
    constructor() {
        super(...arguments);
        this.data = {
            prefix,
            classPrefix: name,
            className: '',
        };
        this.properties = props;
        this.externalClasses = [`${prefix}-class`];
        this.controlledProps = [
            {
                key: 'checked',
                event: 'change',
            },
        ];
        this.options = {
            multipleSlots: true,
        };
        this.lifetimes = {
            attached() {
                this.setClass();
            },
        };
        this.observers = {
            'size, disabled, checked'() {
                this.setClass();
            },
            icon(v) {
                this.setData({
                    _icon: calcIcon(v),
                });
            },
        };
        this.methods = {
            setClass() {
                const { classPrefix } = this.data;
                const { size, variant, disabled, checked, shape } = this.properties;
                const tagClass = [
                    classPrefix,
                    `${classPrefix}--checkable`,
                    disabled ? `${classPrefix}--disabled` : '',
                    checked ? `${classPrefix}--checked` : '',
                    `${classPrefix}--${checked ? 'primary' : 'default'}`,
                    `${classPrefix}--${size}`,
                    `${classPrefix}--${variant}`,
                    `${classPrefix}--${shape}`,
                ];
                const className = classNames(tagClass);
                this.setData({
                    className,
                });
            },
            onClick() {
                if (this.data.disabled)
                    return;
                const { checked } = this.data;
                this._trigger('click');
                this._trigger('change', { checked: !checked });
            },
            onClose(e) {
                if (this.data.disabled)
                    return;
                this._trigger('close', e);
            },
        };
    }
};
CheckTag = __decorate([
    wxComponent()
], CheckTag);
export default CheckTag;
