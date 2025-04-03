var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import avatarGroupProps from './props';
const { prefix } = config;
const name = `${prefix}-avatar-group`;
let AvatarGroup = class AvatarGroup extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`, `${prefix}-class-content`, `${prefix}-class-image`];
        this.properties = avatarGroupProps;
        this.data = {
            prefix,
            classPrefix: name,
            hasChild: true,
            length: 0,
            className: '',
        };
        this.options = {
            multipleSlots: true,
        };
        this.relations = {
            '../avatar/avatar': {
                type: 'descendant',
            },
        };
        this.lifetimes = {
            attached() {
                this.setClass();
            },
            ready() {
                this.setData({
                    length: this.$children.length,
                });
                this.handleMax();
            },
        };
        this.observers = {
            'cascading, size'() {
                this.setClass();
            },
        };
        this.methods = {
            setClass() {
                const { cascading, size } = this.properties;
                const direction = cascading.split('-')[0];
                const classList = [
                    name,
                    `${prefix}-class`,
                    `${name}-offset-${direction}`,
                    `${name}-offset-${direction}-${size.indexOf('px') > -1 ? 'medium' : size || 'medium'}`,
                ];
                this.setData({
                    className: classList.join(' '),
                });
            },
            handleMax() {
                const { max } = this.data;
                const len = this.$children.length;
                if (!max || max > len)
                    return;
                const restAvatars = this.$children.splice(max, len - max);
                restAvatars.forEach((child) => {
                    child.hide();
                });
            },
            onCollapsedItemClick(e) {
                this.triggerEvent('collapsed-item-click', e.detail);
            },
        };
    }
};
AvatarGroup = __decorate([
    wxComponent()
], AvatarGroup);
export default AvatarGroup;
