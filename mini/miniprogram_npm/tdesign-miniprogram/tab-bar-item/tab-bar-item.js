var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { wxComponent, SuperComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import { getRect, calcIcon } from '../common/utils';
const { prefix } = config;
const classPrefix = `${prefix}-tab-bar-item`;
let TabBarItem = class TabBarItem extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`];
        this.parent = null;
        this.relations = {
            '../tab-bar/tab-bar': {
                type: 'ancestor',
                linked(parent) {
                    const { theme, split, shape } = parent.data;
                    this.setData({
                        theme,
                        split,
                        shape,
                        currentName: this.properties.value ? this.properties.value : parent.initName(),
                    });
                    parent.updateChildren();
                },
            },
        };
        this.options = {
            multipleSlots: true,
        };
        this.data = {
            prefix,
            classPrefix,
            isSpread: false,
            isChecked: false,
            hasChildren: false,
            currentName: '',
            split: true,
            iconOnly: false,
            theme: '',
            crowded: false,
            shape: 'normal',
        };
        this.properties = props;
        this.observers = {
            subTabBar(value) {
                this.setData({
                    hasChildren: value.length > 0,
                });
            },
            icon(v) {
                this.setData({
                    _icon: calcIcon(v),
                });
            },
        };
        this.lifetimes = {
            attached() {
                return __awaiter(this, void 0, void 0, function* () {
                    const res = yield getRect(this, `.${classPrefix}__text`);
                    this.setData({ iconOnly: res.height === 0 });
                });
            },
        };
        this.methods = {
            showSpread() {
                this.setData({
                    isSpread: true,
                });
            },
            toggle() {
                const { currentName, hasChildren, isSpread } = this.data;
                if (hasChildren) {
                    this.setData({
                        isSpread: !isSpread,
                    });
                }
                this.$parent.updateValue(currentName);
                this.$parent.changeOtherSpread(currentName);
            },
            selectChild(event) {
                const { value } = event.target.dataset;
                this.$parent.updateValue(value);
                this.setData({
                    isSpread: false,
                });
            },
            checkActive(value) {
                const { currentName, subTabBar } = this.data;
                const isChecked = (subTabBar === null || subTabBar === void 0 ? void 0 : subTabBar.some((item) => item.value === value)) || currentName === value;
                this.setData({
                    isChecked,
                });
            },
            closeSpread() {
                this.setData({
                    isSpread: false,
                });
            },
        };
    }
};
TabBarItem = __decorate([
    wxComponent()
], TabBarItem);
export default TabBarItem;
