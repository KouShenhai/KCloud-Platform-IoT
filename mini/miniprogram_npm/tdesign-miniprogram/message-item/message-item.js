var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from '../message/props';
import { getRect, unitConvert, calcIcon, isObject } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-message`;
const SHOW_DURATION = 500;
const THEME_ICON = {
    info: 'info-circle-filled',
    success: 'check-circle-filled',
    warning: 'info-circle-filled',
    error: 'error-circle-filled',
};
let Message = class Message extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [
            `${prefix}-class`,
            `${prefix}-class-content`,
            `${prefix}-class-icon`,
            `${prefix}-class-link`,
            `${prefix}-class-close-btn`,
        ];
        this.options = {
            multipleSlots: true,
        };
        this.properties = Object.assign({}, props);
        this.data = {
            prefix,
            classPrefix: name,
            loop: -1,
            animation: [],
            showAnimation: [],
            wrapTop: -999,
            fadeClass: '',
        };
        this.closeTimeoutContext = 0;
        this.nextAnimationContext = 0;
        this.resetAnimation = wx.createAnimation({
            duration: 0,
            timingFunction: 'linear',
        });
        this.observers = {
            marquee(val) {
                if (JSON.stringify(val) === '{}' || JSON.stringify(val) === 'true') {
                    this.setData({
                        marquee: {
                            speed: 50,
                            loop: -1,
                            delay: 0,
                        },
                    });
                }
            },
            'icon, theme'(icon, theme) {
                this.setData({
                    _icon: calcIcon(icon, THEME_ICON[theme]),
                });
            },
            link(v) {
                const _link = isObject(v) ? Object.assign({}, v) : { content: v };
                this.setData({ _link });
            },
            closeBtn(v) {
                this.setData({
                    _closeBtn: calcIcon(v, 'close'),
                });
            },
        };
        this.lifetimes = {
            ready() {
                this.memoInitialData();
            },
            detached() {
                this.clearMessageAnimation();
            },
        };
    }
    memoInitialData() {
        this.initialData = Object.assign(Object.assign({}, this.properties), this.data);
    }
    resetData(cb) {
        this.setData(Object.assign({}, this.initialData), cb);
    }
    checkAnimation() {
        const { marquee } = this.properties;
        if (!marquee || marquee.loop === 0) {
            return;
        }
        const speeding = marquee.speed;
        if (this.data.loop > 0) {
            this.data.loop -= 1;
        }
        else if (this.data.loop === 0) {
            this.setData({ animation: this.resetAnimation.translateX(0).step().export() });
            return;
        }
        if (this.nextAnimationContext) {
            this.clearMessageAnimation();
        }
        const warpID = `#${name}__text-wrap`;
        const nodeID = `#${name}__text`;
        Promise.all([getRect(this, nodeID), getRect(this, warpID)]).then(([nodeRect, wrapRect]) => {
            this.setData({
                animation: this.resetAnimation.translateX(wrapRect.width).step().export(),
            }, () => {
                const durationTime = ((nodeRect.width + wrapRect.width) / speeding) * 1000;
                const nextAnimation = wx
                    .createAnimation({
                    duration: durationTime,
                })
                    .translateX(-nodeRect.width)
                    .step()
                    .export();
                setTimeout(() => {
                    this.nextAnimationContext = setTimeout(this.checkAnimation.bind(this), durationTime);
                    this.setData({ animation: nextAnimation });
                }, 20);
            });
        });
    }
    clearMessageAnimation() {
        clearTimeout(this.nextAnimationContext);
        this.nextAnimationContext = 0;
    }
    show(offsetHeight = 0) {
        const { duration, marquee, offset, id } = this.properties;
        this.setData({
            visible: true,
            loop: marquee.loop || this.data.loop,
            fadeClass: `${name}__fade`,
            wrapTop: unitConvert(offset[0]) + offsetHeight,
        });
        this.reset();
        this.checkAnimation();
        if (duration && duration > 0) {
            this.closeTimeoutContext = setTimeout(() => {
                this.hide();
                this.triggerEvent('duration-end', { self: this });
            }, duration);
        }
        const wrapID = id ? `#${id}` : `#${name}`;
        getRect(this, wrapID).then((wrapRect) => {
            this.setData({ height: wrapRect.height }, () => {
                this.setData({
                    fadeClass: ``,
                });
            });
        });
    }
    hide() {
        this.reset();
        this.setData({
            fadeClass: `${name}__fade`,
        });
        setTimeout(() => {
            this.setData({ visible: false, animation: [] });
        }, SHOW_DURATION);
        if (typeof this.onHide === 'function') {
            this.onHide();
        }
    }
    reset() {
        if (this.nextAnimationContext) {
            this.clearMessageAnimation();
        }
        clearTimeout(this.closeTimeoutContext);
        this.closeTimeoutContext = 0;
    }
    handleClose() {
        this.hide();
        this.triggerEvent('close-btn-click');
    }
    handleLinkClick() {
        this.triggerEvent('link-click');
    }
};
Message = __decorate([
    wxComponent()
], Message);
export default Message;
