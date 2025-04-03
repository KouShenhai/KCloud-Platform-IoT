var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import { getRect } from '../common/utils';
import { getObserver } from '../common/wechat';
let ARRAY = [];
const { prefix } = config;
const name = `${prefix}-swipe-cell`;
const ContainerClass = `.${name}`;
let SwiperCell = class SwiperCell extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`];
        this.options = {
            multipleSlots: true,
        };
        this.properties = props;
        this.data = {
            prefix,
            wrapperStyle: '',
            closed: true,
            classPrefix: name,
            skipMove: false,
        };
        this.observers = {
            'left, right'() {
                this.setSwipeWidth();
            },
        };
        this.lifetimes = {
            attached() {
                ARRAY.push(this);
            },
            ready() {
                this.setSwipeWidth();
            },
            detached() {
                ARRAY = ARRAY.filter((item) => item !== this);
            },
        };
    }
    setSwipeWidth() {
        Promise.all([getRect(this, `${ContainerClass}__left`), getRect(this, `${ContainerClass}__right`)]).then(([leftRect, rightRect]) => {
            if (leftRect.width === 0 && rightRect.width === 0 && !this._hasObserved) {
                this._hasObserved = true;
                getObserver(this, `.${name}`).then(() => {
                    this.setSwipeWidth();
                });
            }
            this.setData({
                leftWidth: leftRect.width,
                rightWidth: rightRect.width,
            });
        });
    }
    skipMove() {
        if (!this.data.skipMove) {
            this.setData({ skipMove: true });
        }
    }
    catchMove() {
        if (this.data.skipMove) {
            this.setData({ skipMove: false });
        }
    }
    open() {
        this.setData({ opened: true });
    }
    close() {
        this.setData({ opened: false });
    }
    closeOther() {
        ARRAY.filter((item) => item !== this).forEach((item) => item.close());
    }
    onTap() {
        this.close();
    }
    onActionTap(event) {
        const { currentTarget: { dataset: { action }, }, } = event;
        this.triggerEvent('click', action);
    }
};
SwiperCell = __decorate([
    wxComponent()
], SwiperCell);
export default SwiperCell;
