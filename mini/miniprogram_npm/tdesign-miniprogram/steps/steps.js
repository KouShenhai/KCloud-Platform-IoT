var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { wxComponent, SuperComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
const { prefix } = config;
const name = `${prefix}-steps`;
let Steps = class Steps extends SuperComponent {
    constructor() {
        super(...arguments);
        this.relations = {
            '../step-item/step-item': {
                type: 'child',
                linked(child) {
                    this.updateChildren();
                    const { readonly } = this.data;
                    child.setData({
                        readonly,
                    });
                },
                unlinked() {
                    this.updateLastChid();
                },
            },
        };
        this.externalClasses = [`${prefix}-class`];
        this.properties = props;
        this.controlledProps = [
            {
                key: 'current',
                event: 'change',
            },
        ];
        this.data = {
            prefix,
            classPrefix: name,
        };
        this.observers = {
            current() {
                this.updateChildren();
            },
        };
        this.methods = {
            updateChildren() {
                const items = this.$children;
                items.forEach((item, index) => {
                    item.updateStatus(Object.assign({ index, items }, this.data));
                });
            },
            updateLastChid() {
                const items = this.$children;
                items.forEach((child, index) => child.setData({ isLastChild: index === items.length - 1 }));
            },
            handleClick(index) {
                if (!this.data.readonly) {
                    const preIndex = this.data.current;
                    this._trigger('change', {
                        previous: preIndex,
                        current: index,
                    });
                }
            },
        };
    }
};
Steps = __decorate([
    wxComponent()
], Steps);
export default Steps;
