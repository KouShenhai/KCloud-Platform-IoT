var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import props from './props';
import config from '../common/config';
import { calcIcon } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-result`;
const THEME_ICON = {
    default: 'error-circle',
    success: 'check-circle',
    warning: 'error-circle',
    error: 'close-circle',
};
let default_1 = class extends SuperComponent {
    constructor() {
        super(...arguments);
        this.options = {
            multipleSlots: true,
        };
        this.externalClasses = [
            `${prefix}-class`,
            `${prefix}-class-image`,
            `${prefix}-class-title`,
            `${prefix}-class-description`,
        ];
        this.properties = props;
        this.data = {
            prefix,
            classPrefix: name,
        };
        this.lifetimes = {
            ready() {
                this.initIcon();
            },
        };
        this.observers = {
            'icon, theme'() {
                this.initIcon();
            },
        };
        this.methods = {
            initIcon() {
                const { icon, theme } = this.properties;
                this.setData({
                    _icon: calcIcon(icon, THEME_ICON[theme]),
                });
            },
        };
    }
};
default_1 = __decorate([
    wxComponent()
], default_1);
export default default_1;
