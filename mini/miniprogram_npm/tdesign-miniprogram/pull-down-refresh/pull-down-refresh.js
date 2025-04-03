var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import { unitConvert, systemInfo } from '../common/utils';
import useCustomNavbar from '../mixins/using-custom-navbar';
const { prefix } = config;
const name = `${prefix}-pull-down-refresh`;
let PullDownRefresh = class PullDownRefresh extends SuperComponent {
    constructor() {
        super(...arguments);
        this.pixelRatio = 1;
        this.startPoint = null;
        this.isPulling = false;
        this.maxRefreshAnimateTimeFlag = 0;
        this.closingAnimateTimeFlag = 0;
        this.refreshStatusTimer = null;
        this.externalClasses = [`${prefix}-class`, `${prefix}-class-loading`, `${prefix}-class-text`, `${prefix}-class-indicator`];
        this.options = {
            multipleSlots: true,
            pureDataPattern: /^_/,
        };
        this.relations = {
            '../back-top/back-top': {
                type: 'descendant',
            },
        };
        this.properties = props;
        this.behaviors = [useCustomNavbar];
        this.data = {
            prefix,
            classPrefix: name,
            barHeight: 0,
            tipsHeight: 0,
            refreshStatus: -1,
            loosing: false,
            enableToRefresh: true,
            scrollTop: 0,
            _maxBarHeight: 0,
            _loadingBarHeight: 0,
        };
        this.lifetimes = {
            attached() {
                const { screenWidth } = systemInfo;
                const { loadingTexts, maxBarHeight, loadingBarHeight } = this.properties;
                this.setData({
                    _maxBarHeight: unitConvert(maxBarHeight),
                    _loadingBarHeight: unitConvert(loadingBarHeight),
                    loadingTexts: Array.isArray(loadingTexts) && loadingTexts.length >= 4
                        ? loadingTexts
                        : ['下拉刷新', '松手刷新', '正在刷新', '刷新完成'],
                });
                this.pixelRatio = 750 / screenWidth;
            },
            detached() {
                clearTimeout(this.maxRefreshAnimateTimeFlag);
                clearTimeout(this.closingAnimateTimeFlag);
                this.resetTimer();
            },
        };
        this.observers = {
            value(val) {
                if (!val) {
                    clearTimeout(this.maxRefreshAnimateTimeFlag);
                    if (this.data.refreshStatus > 0) {
                        this.setData({
                            refreshStatus: 3,
                        });
                    }
                    this.setData({ barHeight: 0 });
                }
                else {
                    this.doRefresh();
                }
            },
            barHeight(val) {
                this.resetTimer();
                if (val === 0 && this.data.refreshStatus !== -1) {
                    this.refreshStatusTimer = setTimeout(() => {
                        this.setData({ refreshStatus: -1 });
                    }, 240);
                }
                this.setData({ tipsHeight: Math.min(val, this.data._loadingBarHeight) });
            },
            maxBarHeight(v) {
                this.setData({ _maxBarHeight: unitConvert(v) });
            },
            loadingBarHeight(v) {
                this.setData({ _loadingBarHeight: unitConvert(v) });
            },
        };
        this.methods = {
            resetTimer() {
                if (this.refreshStatusTimer) {
                    clearTimeout(this.refreshStatusTimer);
                    this.refreshStatusTimer = null;
                }
            },
            onScrollToBottom() {
                this.triggerEvent('scrolltolower');
            },
            onScrollToTop() {
                this.setData({
                    enableToRefresh: true,
                });
            },
            onScroll(e) {
                const { scrollTop } = e.detail;
                this.setData({
                    enableToRefresh: scrollTop === 0,
                });
                this.triggerEvent('scroll', { scrollTop });
            },
            onTouchStart(e) {
                if (this.isPulling || !this.data.enableToRefresh || this.properties.disabled)
                    return;
                const { touches } = e;
                if (touches.length !== 1)
                    return;
                const { pageX, pageY } = touches[0];
                this.setData({ loosing: false });
                this.startPoint = { pageX, pageY };
                this.isPulling = true;
            },
            onTouchMove(e) {
                if (!this.startPoint || this.properties.disabled)
                    return;
                const { touches } = e;
                if (touches.length !== 1)
                    return;
                const { pageY } = touches[0];
                const offset = pageY - this.startPoint.pageY;
                if (offset > 0) {
                    this.setRefreshBarHeight(offset);
                }
            },
            onTouchEnd(e) {
                if (!this.startPoint || this.properties.disabled)
                    return;
                const { changedTouches } = e;
                if (changedTouches.length !== 1)
                    return;
                const { pageY } = changedTouches[0];
                const barHeight = pageY - this.startPoint.pageY;
                this.startPoint = null;
                this.isPulling = false;
                this.setData({ loosing: true });
                if (barHeight > this.data._loadingBarHeight) {
                    this._trigger('change', { value: true });
                    this.triggerEvent('refresh');
                }
                else {
                    this.setData({ barHeight: 0 });
                }
            },
            onDragStart(e) {
                const { scrollTop, scrollLeft } = e.detail;
                this.triggerEvent('dragstart', { scrollTop, scrollLeft });
            },
            onDragging(e) {
                const { scrollTop, scrollLeft } = e.detail;
                this.triggerEvent('dragging', { scrollTop, scrollLeft });
            },
            onDragEnd(e) {
                const { scrollTop, scrollLeft } = e.detail;
                this.triggerEvent('dragend', { scrollTop, scrollLeft });
            },
            doRefresh() {
                if (this.properties.disabled)
                    return;
                this.setData({
                    barHeight: this.data._loadingBarHeight,
                    refreshStatus: 2,
                    loosing: true,
                });
                this.maxRefreshAnimateTimeFlag = setTimeout(() => {
                    this.maxRefreshAnimateTimeFlag = null;
                    if (this.data.refreshStatus === 2) {
                        this.triggerEvent('timeout');
                        this._trigger('change', { value: false });
                    }
                }, this.properties.refreshTimeout);
            },
            setRefreshBarHeight(value) {
                const barHeight = Math.min(value, this.data._maxBarHeight);
                const data = { barHeight };
                if (barHeight >= this.data._loadingBarHeight) {
                    data.refreshStatus = 1;
                }
                else {
                    data.refreshStatus = 0;
                }
                return new Promise((resolve) => {
                    this.setData(data, () => resolve(barHeight));
                });
            },
            setScrollTop(scrollTop) {
                this.setData({ scrollTop });
            },
            scrollToTop() {
                this.setScrollTop(0);
            },
        };
    }
};
PullDownRefresh = __decorate([
    wxComponent()
], PullDownRefresh);
export default PullDownRefresh;
