var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent, isObject } from '../common/src/index';
import config from '../common/config';
import props from './props';
import { uniqueFactory, setIcon } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-grid-item`;
const getUniqueID = uniqueFactory('grid_item');
var LinkTypes;
(function (LinkTypes) {
    LinkTypes["redirect-to"] = "redirectTo";
    LinkTypes["switch-tab"] = "switchTab";
    LinkTypes["relaunch"] = "reLaunch";
    LinkTypes["navigate-to"] = "navigateTo";
})(LinkTypes || (LinkTypes = {}));
let GridItem = class GridItem extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [
            `${prefix}-class`,
            `${prefix}-class-content`,
            `${prefix}-class-image`,
            `${prefix}-class-text`,
            `${prefix}-class-description`,
        ];
        this.options = {
            multipleSlots: true,
        };
        this.relations = {
            '../grid/grid': {
                type: 'ancestor',
                linked(target) {
                    this.parent = target;
                    this.updateStyle();
                    this.setData({
                        column: target.data.column,
                    });
                },
            },
        };
        this.properties = props;
        this.data = {
            prefix,
            classPrefix: name,
            gridItemStyle: '',
            gridItemWrapperStyle: '',
            gridItemContentStyle: '',
            align: 'center',
            column: 0,
            describedbyID: '',
        };
        this.observers = {
            icon(icon) {
                const obj = setIcon('icon', icon, '');
                this.setData(Object.assign({}, obj));
            },
        };
        this.lifetimes = {
            ready() {
                this.setData({
                    describedbyID: getUniqueID(),
                });
            },
        };
    }
    updateStyle() {
        const { hover, align } = this.parent.properties;
        const gridItemStyles = [];
        const gridItemWrapperStyles = [];
        const gridItemContentStyles = [];
        const widthStyle = this.getWidthStyle();
        const paddingStyle = this.getPaddingStyle();
        const borderStyle = this.getBorderStyle();
        widthStyle && gridItemStyles.push(widthStyle);
        paddingStyle && gridItemWrapperStyles.push(paddingStyle);
        borderStyle && gridItemContentStyles.push(borderStyle);
        this.setData({
            gridItemStyle: `${gridItemStyles.join(';')}`,
            gridItemWrapperStyle: gridItemWrapperStyles.join(';'),
            gridItemContentStyle: gridItemContentStyles.join(';'),
            hover,
            layout: this.properties.layout,
            align: align,
        });
    }
    getWidthStyle() {
        const { column } = this.parent.properties;
        return column > 0 ? `width:${(1 / column) * 100}%` : '';
    }
    getPaddingStyle() {
        const { gutter } = this.parent.properties;
        if (gutter)
            return `padding-left:${gutter}rpx;padding-top:${gutter}rpx`;
        return '';
    }
    getBorderStyle() {
        const { gutter } = this.parent.properties;
        let { border } = this.parent.properties;
        if (!border)
            return '';
        if (!isObject(border))
            border = {};
        const { color = '#266FE8', width = 2, style = 'solid' } = border;
        if (gutter)
            return `border:${width}rpx ${style} ${color}`;
        return `border-top:${width}rpx ${style} ${color};border-left:${width}rpx ${style} ${color}`;
    }
    onClick(e) {
        const { item } = e.currentTarget.dataset;
        this.triggerEvent('click', item);
        this.jumpLink();
    }
    jumpLink() {
        const { url, jumpType } = this.properties;
        if (url && jumpType) {
            if (LinkTypes[jumpType]) {
                wx[LinkTypes[jumpType]]({ url });
            }
        }
    }
};
GridItem = __decorate([
    wxComponent()
], GridItem);
export default GridItem;
