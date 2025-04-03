var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { chunk } from '../common/utils';
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import { ActionSheetTheme, show } from './show';
import props from './props';
import useCustomNavbar from '../mixins/using-custom-navbar';
const { prefix } = config;
const name = `${prefix}-action-sheet`;
let ActionSheet = class ActionSheet extends SuperComponent {
    constructor() {
        super(...arguments);
        this.behaviors = [useCustomNavbar];
        this.externalClasses = [`${prefix}-class`, `${prefix}-class-content`, `${prefix}-class-cancel`];
        this.properties = Object.assign({}, props);
        this.data = {
            prefix,
            classPrefix: name,
            gridThemeItems: [],
            currentSwiperIndex: 0,
            defaultPopUpProps: {},
            defaultPopUpzIndex: 11500,
        };
        this.controlledProps = [
            {
                key: 'visible',
                event: 'visible-change',
            },
        ];
        this.observers = {
            'visible, items'(visible) {
                if (!visible)
                    return;
                this.init();
            },
        };
        this.methods = {
            init() {
                this.memoInitialData();
                this.splitGridThemeActions();
            },
            memoInitialData() {
                this.initialData = Object.assign(Object.assign({}, this.properties), this.data);
            },
            splitGridThemeActions() {
                if (this.data.theme !== ActionSheetTheme.Grid)
                    return;
                this.setData({
                    gridThemeItems: chunk(this.data.items, this.data.count),
                });
            },
            show(options) {
                this.setData(Object.assign(Object.assign(Object.assign({}, this.initialData), options), { visible: true }));
                this.splitGridThemeActions();
                this.autoClose = true;
                this._trigger('visible-change', { visible: true });
            },
            close() {
                this.triggerEvent('close', { trigger: 'command' });
                this._trigger('visible-change', { visible: false });
            },
            onPopupVisibleChange({ detail }) {
                if (!detail.visible) {
                    this.triggerEvent('close', { trigger: 'overlay' });
                    this._trigger('visible-change', { visible: false });
                }
                if (this.autoClose) {
                    this.setData({ visible: false });
                    this.autoClose = false;
                }
            },
            onSwiperChange(e) {
                const { current } = e.detail;
                this.setData({
                    currentSwiperIndex: current,
                });
            },
            onSelect(event) {
                const { currentSwiperIndex, items, gridThemeItems, count, theme } = this.data;
                const { index } = event.currentTarget.dataset;
                const isSwiperMode = theme === ActionSheetTheme.Grid;
                const item = isSwiperMode ? gridThemeItems[currentSwiperIndex][index] : items[index];
                const realIndex = isSwiperMode ? index + currentSwiperIndex * count : index;
                if (item) {
                    this.triggerEvent('selected', { selected: item, index: realIndex });
                    if (!item.disabled) {
                        this.triggerEvent('close', { trigger: 'select' });
                        this._trigger('visible-change', { visible: false });
                    }
                }
            },
            onCancel() {
                this.triggerEvent('cancel');
                if (this.autoClose) {
                    this.setData({ visible: false });
                    this.autoClose = false;
                }
            },
        };
    }
};
ActionSheet.show = show;
ActionSheet = __decorate([
    wxComponent()
], ActionSheet);
export default ActionSheet;
