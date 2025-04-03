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
const name = `${prefix}-side-bar`;
const relationsPath = '../side-bar-item/side-bar-item';
let SideBar = class SideBar extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`];
        this.children = [];
        this.relations = {
            [relationsPath]: {
                type: 'child',
                linked(child) {
                    this.children.push(child);
                },
                unlinked(child) {
                    const index = this.children.findIndex((item) => item === child);
                    this.children.splice(index, 1);
                },
            },
        };
        this.controlledProps = [
            {
                key: 'value',
                event: 'change',
            },
        ];
        this.properties = props;
        this.observers = {
            value(v) {
                this.$children.forEach((item) => {
                    item.updateActive(v);
                });
            },
        };
        this.data = {
            classPrefix: name,
            prefix,
        };
        this.methods = {
            doChange({ value, label }) {
                this._trigger('change', { value, label });
            },
        };
    }
};
SideBar = __decorate([
    wxComponent()
], SideBar);
export default SideBar;
