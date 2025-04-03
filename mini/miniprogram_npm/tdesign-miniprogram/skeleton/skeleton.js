var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import { isNumber, classNames } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-skeleton`;
const ThemeMap = {
    avatar: [{ type: 'circle', size: '96rpx' }],
    image: [{ type: 'rect', size: '144rpx' }],
    text: [
        [
            { width: '24%', height: '32rpx', marginRight: '32rpx' },
            { width: '76%', height: '32rpx' },
        ],
        1,
    ],
    paragraph: [1, 1, 1, { width: '55%' }],
};
let Skeleton = class Skeleton extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`, `${prefix}-class-col`, `${prefix}-class-row`];
        this.properties = props;
        this.data = {
            prefix,
            classPrefix: name,
            parsedRowcols: [],
        };
        this.observers = {
            rowCol() {
                this.init();
            },
            'loading, delay'() {
                this.isShowSkeleton();
            },
        };
        this.lifetimes = {
            attached() {
                this.init();
                this.isShowSkeleton();
            },
        };
        this.methods = {
            init() {
                const { theme, rowCol } = this.properties;
                const rowCols = [];
                if (rowCol.length) {
                    rowCols.push(...rowCol);
                }
                else {
                    rowCols.push(...ThemeMap[theme || 'text']);
                }
                const parsedRowcols = rowCols.map((item) => {
                    if (isNumber(item)) {
                        return [
                            {
                                class: this.getColItemClass({ type: 'text' }),
                                style: {},
                            },
                        ];
                    }
                    if (Array.isArray(item)) {
                        return item.map((col) => {
                            return Object.assign(Object.assign({}, col), { class: this.getColItemClass(col), style: this.getColItemStyle(col) });
                        });
                    }
                    const nItem = item;
                    return [
                        Object.assign(Object.assign({}, nItem), { class: this.getColItemClass(nItem), style: this.getColItemStyle(nItem) }),
                    ];
                });
                this.setData({
                    parsedRowcols,
                });
            },
            getColItemClass(obj) {
                return classNames([
                    `${name}__col`,
                    `${name}--type-${obj.type || 'text'}`,
                    `${name}--animation-${this.properties.animation}`,
                ]);
            },
            getColItemStyle(obj) {
                const styleName = [
                    'width',
                    'height',
                    'marginRight',
                    'marginLeft',
                    'margin',
                    'size',
                    'background',
                    'backgroundColor',
                    'borderRadius',
                ];
                const style = {};
                styleName.forEach((name) => {
                    if (name in obj) {
                        const px = isNumber(obj[name]) ? `${obj[name]}px` : obj[name];
                        if (name === 'size') {
                            [style.width, style.height] = [px, px];
                        }
                        else {
                            style[name] = px;
                        }
                    }
                });
                return style;
            },
            isShowSkeleton() {
                const { loading, delay } = this.properties;
                if (!loading || delay === 0) {
                    this.setData({
                        isShow: loading,
                    });
                    return;
                }
                setTimeout(() => {
                    this.setData({
                        isShow: loading,
                    });
                }, delay);
            },
        };
    }
};
Skeleton = __decorate([
    wxComponent()
], Skeleton);
export default Skeleton;
