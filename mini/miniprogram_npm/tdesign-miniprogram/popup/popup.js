var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import transition from '../mixins/transition';
import useCustomNavbar from '../mixins/using-custom-navbar';
delete props.visible;
const { prefix } = config;
const name = `${prefix}-popup`;
let Popup = class Popup extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`, `${prefix}-class-content`];
        this.behaviors = [transition(), useCustomNavbar];
        this.options = {
            multipleSlots: true,
        };
        this.properties = props;
        this.data = {
            prefix,
            classPrefix: name,
        };
        this.methods = {
            handleOverlayClick() {
                const { closeOnOverlayClick } = this.properties;
                if (closeOnOverlayClick) {
                    this.triggerEvent('visible-change', { visible: false, trigger: 'overlay' });
                }
            },
            handleClose() {
                this.triggerEvent('visible-change', { visible: false, trigger: 'close-btn' });
            },
        };
    }
};
Popup = __decorate([
    wxComponent()
], Popup);
export default Popup;
