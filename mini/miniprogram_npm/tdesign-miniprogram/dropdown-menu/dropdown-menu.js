var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import { calcIcon } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-dropdown-menu`;
let DropdownMenu = class DropdownMenu extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`, `${prefix}-class-item`, `${prefix}-class-label`, `${prefix}-class-icon`];
        this.properties = props;
        this.nodes = null;
        this.data = {
            prefix,
            classPrefix: name,
            menus: null,
            activeIdx: -1,
            bottom: 0,
            _arrowIcon: { name: props.arrowIcon.value },
        };
        this.relations = {
            '../dropdown-item/dropdown-item': {
                type: 'child',
            },
        };
        this.lifetimes = {
            ready() {
                this.getAllItems();
            },
        };
        this.observers = {
            arrowIcon(v) {
                this.setData({
                    _arrowIcon: calcIcon(v),
                });
            },
            activeIdx(v) {
                this.triggerEvent(v === -1 ? 'close' : 'open');
            },
        };
        this.methods = {
            toggle(index) {
                const { activeIdx, duration } = this.data;
                const prevItem = this.$children[activeIdx];
                const currItem = this.$children[index];
                if (currItem === null || currItem === void 0 ? void 0 : currItem.data.disabled)
                    return;
                if (activeIdx !== -1) {
                    prevItem.triggerEvent('close');
                    prevItem.setData({
                        show: false,
                    }, () => {
                        setTimeout(() => {
                            prevItem.triggerEvent('closed');
                        }, duration);
                    });
                }
                if (index == null || activeIdx === index) {
                    this.setData({
                        activeIdx: -1,
                    });
                }
                else {
                    currItem.triggerEvent('open');
                    this.setData({
                        activeIdx: index,
                    });
                    currItem.setData({
                        show: true,
                    }, () => {
                        setTimeout(() => {
                            currItem.triggerEvent('opened');
                        }, duration);
                    });
                }
            },
            getAllItems() {
                const menus = this.$children.map(({ data }) => ({
                    label: data.label || data.computedLabel,
                    disabled: data.disabled,
                }));
                this.setData({
                    menus,
                });
            },
            handleToggle(e) {
                const { index } = e.currentTarget.dataset;
                this.toggle(index);
            },
            noop() { },
        };
    }
};
DropdownMenu = __decorate([
    wxComponent()
], DropdownMenu);
export default DropdownMenu;
