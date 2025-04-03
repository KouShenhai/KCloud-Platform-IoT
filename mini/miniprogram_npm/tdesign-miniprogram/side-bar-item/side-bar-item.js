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
const name = `${prefix}-side-bar-item`;
let SideBarItem = class SideBarItem extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`];
        this.properties = Object.assign(Object.assign({}, props), { tId: {
                type: String,
            } });
        this.relations = {
            '../side-bar/side-bar': {
                type: 'parent',
                linked(parent) {
                    this.parent = parent;
                    this.updateActive(parent.data.value);
                },
            },
        };
        this.observers = {
            icon(v) {
                this.setData({ _icon: typeof v === 'string' ? { name: v } : v });
            },
        };
        this.data = {
            classPrefix: name,
            prefix,
            active: false,
            isPre: false,
            isNext: false,
        };
        this.methods = {
            updateActive(value) {
                const active = value === this.data.value;
                this.setData({
                    active,
                });
            },
            handleClick() {
                var _a;
                if (this.data.disabled)
                    return;
                const { value, label } = this.data;
                (_a = this.parent) === null || _a === void 0 ? void 0 : _a.doChange({ value, label });
            },
        };
    }
};
SideBarItem = __decorate([
    wxComponent()
], SideBarItem);
export default SideBarItem;
