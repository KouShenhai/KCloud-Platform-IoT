var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import useCustomNavbar from '../mixins/using-custom-navbar';
const { prefix } = config;
const name = `${prefix}-drawer`;
let Drawer = class Drawer extends SuperComponent {
    constructor() {
        super(...arguments);
        this.behaviors = [useCustomNavbar];
        this.externalClasses = [];
        this.options = {
            multipleSlots: true,
        };
        this.properties = props;
        this.data = {
            classPrefix: name,
        };
        this.methods = {
            visibleChange({ detail }) {
                const { visible } = detail;
                const { showOverlay } = this.data;
                this.setData({
                    visible,
                });
                if (!visible) {
                    this.triggerEvent('close', { trigger: 'overlay' });
                }
                if (showOverlay) {
                    this.triggerEvent('overlay-click', { visible: visible });
                }
            },
            itemClick(detail) {
                const { index, item } = detail.currentTarget.dataset;
                this.triggerEvent('item-click', { index, item });
            },
        };
    }
};
Drawer = __decorate([
    wxComponent()
], Drawer);
export default Drawer;
