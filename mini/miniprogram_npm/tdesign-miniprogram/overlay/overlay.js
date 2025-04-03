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
const { prefix } = config;
const name = `${prefix}-overlay`;
let Overlay = class Overlay extends SuperComponent {
    constructor() {
        super(...arguments);
        this.properties = props;
        this.behaviors = [transition(), useCustomNavbar];
        this.data = {
            prefix,
            classPrefix: name,
            computedStyle: '',
            _zIndex: 11000,
        };
        this.observers = {
            backgroundColor(v) {
                this.setData({
                    computedStyle: v ? `background-color: ${v};` : '',
                });
            },
            zIndex(v) {
                if (v !== 0) {
                    this.setData({
                        _zIndex: v,
                    });
                }
            },
        };
        this.methods = {
            handleClick() {
                this.triggerEvent('click', { visible: !this.properties.visible });
            },
            noop() { },
        };
    }
};
Overlay = __decorate([
    wxComponent()
], Overlay);
export default Overlay;
