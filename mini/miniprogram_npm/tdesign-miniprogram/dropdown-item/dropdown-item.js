var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import menuProps from '../dropdown-menu/props';
import { getRect } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-dropdown-item`;
let DropdownMenuItem = class DropdownMenuItem extends SuperComponent {
    constructor() {
        super(...arguments);
        this.options = {
            multipleSlots: true,
        };
        this.externalClasses = [
            `${prefix}-class`,
            `${prefix}-class-content`,
            `${prefix}-class-column`,
            `${prefix}-class-column-item`,
            `${prefix}-class-column-item-label`,
            `${prefix}-class-footer`,
        ];
        this.properties = props;
        this.data = {
            prefix,
            classPrefix: name,
            show: false,
            top: 0,
            maskHeight: 0,
            initValue: null,
            hasChanged: false,
            duration: menuProps.duration.value,
            zIndex: menuProps.zIndex.value,
            overlay: menuProps.showOverlay.value,
            labelAlias: 'label',
            valueAlias: 'value',
            computedLabel: '',
            firstCheckedValue: '',
        };
        this.relations = {
            '../dropdown-menu/dropdown-menu': {
                type: 'parent',
                linked(target) {
                    const { zIndex, duration, showOverlay } = target.properties;
                    this.setData({
                        zIndex,
                        duration,
                        showOverlay,
                    });
                },
            },
        };
        this.controlledProps = [
            {
                key: 'value',
                event: 'change',
            },
        ];
        this.observers = {
            keys(obj) {
                this.setData({
                    labelAlias: obj.label || 'label',
                    valueAlias: obj.value || 'value',
                });
            },
            value(v) {
                const { options, labelAlias, valueAlias } = this.data;
                if (this.data.multiple) {
                    if (!Array.isArray(v))
                        throw TypeError('应传入数组类型的 value');
                }
                const target = options.find((item) => item[valueAlias] === v);
                if (target) {
                    this.setData({
                        computedLabel: target[labelAlias],
                    });
                }
            },
            'label, computedLabel'() {
                var _a;
                (_a = this.$parent) === null || _a === void 0 ? void 0 : _a.getAllItems();
            },
            show(visible) {
                if (visible) {
                    this.getParentBottom(() => {
                        this.setData({ wrapperVisible: true });
                    });
                }
            },
        };
        this.methods = {
            closeDropdown() {
                var _a;
                (_a = this.$parent) === null || _a === void 0 ? void 0 : _a.setData({
                    activeIdx: -1,
                });
                this.setData({
                    show: false,
                });
                this.triggerEvent('close');
            },
            getParentBottom(cb) {
                getRect(this.$parent, `#${prefix}-bar`).then((rect) => {
                    this.setData({
                        top: rect.bottom,
                        maskHeight: rect.top,
                    }, cb);
                });
            },
            handleTreeClick(e) {
                const { level, value: itemValue } = e.currentTarget.dataset;
                const { value } = this.data;
                value[level] = itemValue;
                this._trigger('change', { value });
            },
            handleRadioChange(e) {
                const { value } = e.detail;
                this._trigger('change', { value });
                if (!this.data.multiple) {
                    this.closeDropdown();
                }
                else {
                    const firstChecked = this.data.options.find((item) => value.includes(item.value));
                    if (firstChecked) {
                        this.data.firstCheckedValue = firstChecked.value;
                    }
                }
            },
            handleMaskClick() {
                var _a;
                if ((_a = this.$parent) === null || _a === void 0 ? void 0 : _a.properties.closeOnClickOverlay) {
                    this.closeDropdown();
                }
            },
            handleReset() {
                this._trigger('change', { value: [] });
                this._trigger('reset');
            },
            handleConfirm() {
                this._trigger('confirm', { value: this.data.value });
                this.closeDropdown();
                this.setData({ firstCheckedValue: this.data.firstCheckedValue });
            },
            onLeaved() {
                this.setData({ wrapperVisible: false });
            },
        };
    }
};
DropdownMenuItem = __decorate([
    wxComponent()
], DropdownMenuItem);
export default DropdownMenuItem;
