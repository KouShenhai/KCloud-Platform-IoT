var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import TCalendar from '../common/shared/calendar/index';
import useCustomNavbar from '../mixins/using-custom-navbar';
import { getPrevMonth, getPrevYear, getNextMonth, getNextYear } from './utils';
const { prefix } = config;
const name = `${prefix}-calendar`;
const defaultLocaleText = {
    title: '请选择日期',
    weekdays: ['日', '一', '二', '三', '四', '五', '六'],
    monthTitle: '{year} 年 {month}',
    months: ['1 月', '2 月', '3 月', '4 月', '5 月', '6 月', '7 月', '8 月', '9 月', '10 月', '11 月', '12 月'],
    confirm: '确认',
};
let Calendar = class Calendar extends SuperComponent {
    constructor() {
        super(...arguments);
        this.behaviors = [useCustomNavbar];
        this.externalClasses = [`${prefix}-class`];
        this.options = {
            multipleSlots: true,
        };
        this.properties = props;
        this.data = {
            prefix,
            classPrefix: name,
            months: [],
            scrollIntoView: '',
            innerConfirmBtn: {},
            realLocalText: {},
            currentMonth: {},
            actionButtons: {
                preYearBtnDisable: false,
                prevMonthBtnDisable: false,
                nextMonthBtnDisable: false,
                nextYearBtnDisable: false,
            },
        };
        this.controlledProps = [
            {
                key: 'value',
                event: 'confirm',
            },
            {
                key: 'value',
                event: 'change',
            },
        ];
        this.lifetimes = {
            created() {
                this.base = new TCalendar(this.properties);
            },
            ready() {
                const realLocalText = Object.assign(Object.assign({}, defaultLocaleText), this.properties.localeText);
                this.initialValue();
                this.setData({
                    days: this.base.getDays(realLocalText.weekdays),
                    realLocalText,
                });
                this.calcMonths();
                if (this.data.switchMode !== 'none') {
                    this.calcCurrentMonth();
                }
                if (!this.data.usePopup) {
                    this.scrollIntoView();
                }
            },
        };
        this.observers = {
            type(v) {
                this.base.type = v;
            },
            confirmBtn(v) {
                if (typeof v === 'string') {
                    this.setData({ innerConfirmBtn: v === 'slot' ? 'slot' : { content: v } });
                }
                else if (typeof v === 'object') {
                    this.setData({ innerConfirmBtn: v });
                }
            },
            'firstDayOfWeek,minDate,maxDate'(firstDayOfWeek, minDate, maxDate) {
                firstDayOfWeek && (this.base.firstDayOfWeek = firstDayOfWeek);
                minDate && (this.base.minDate = minDate);
                maxDate && (this.base.maxDate = maxDate);
                this.calcMonths();
            },
            value(v) {
                this.base.value = v;
                this.calcMonths();
            },
            visible(v) {
                if (v) {
                    this.scrollIntoView();
                    this.base.value = this.data.value;
                    this.calcMonths();
                }
            },
            format(v) {
                const { usePopup, visible } = this.data;
                this.base.format = v;
                if (!usePopup || visible) {
                    this.calcMonths();
                }
            },
        };
        this.methods = {
            initialValue() {
                const { value, type, minDate } = this.data;
                if (!value) {
                    const today = new Date();
                    const now = minDate || new Date(today.getFullYear(), today.getMonth(), today.getDate()).getTime();
                    const initialValue = type === 'single' ? now : [now];
                    if (type === 'range') {
                        initialValue[1] = now + 24 * 3600 * 1000;
                    }
                    this.setData({
                        value: initialValue,
                    });
                    this.base.value = initialValue;
                }
            },
            scrollIntoView() {
                const { value } = this.data;
                if (!value)
                    return;
                const date = new Date(Array.isArray(value) ? value[0] : value);
                if (date) {
                    this.setData({
                        scrollIntoView: `year_${date.getFullYear()}_month_${date.getMonth()}`,
                    });
                }
            },
            getCurrentYearAndMonth(v) {
                const date = new Date(v);
                return { year: date.getFullYear(), month: date.getMonth() };
            },
            updateActionButton(value) {
                const _min = this.getCurrentYearAndMonth(this.base.minDate);
                const _max = this.getCurrentYearAndMonth(this.base.maxDate);
                const _minTimestamp = new Date(_min.year, _min.month, 1).getTime();
                const _maxTimestamp = new Date(_max.year, _max.month, 1).getTime();
                const _prevYearTimestamp = getPrevYear(value).getTime();
                const _prevMonthTimestamp = getPrevMonth(value).getTime();
                const _nextMonthTimestamp = getNextMonth(value).getTime();
                const _nextYearTimestamp = getNextYear(value).getTime();
                const preYearBtnDisable = _prevYearTimestamp < _minTimestamp || _prevMonthTimestamp < _minTimestamp;
                const prevMonthBtnDisable = _prevMonthTimestamp < _minTimestamp;
                const nextYearBtnDisable = _nextMonthTimestamp > _maxTimestamp || _nextYearTimestamp > _maxTimestamp;
                const nextMonthBtnDisable = _nextMonthTimestamp > _maxTimestamp;
                this.setData({
                    actionButtons: {
                        preYearBtnDisable,
                        prevMonthBtnDisable,
                        nextYearBtnDisable,
                        nextMonthBtnDisable,
                    },
                });
            },
            calcCurrentMonth(newValue) {
                const date = newValue || this.getCurrentDate();
                const { year, month } = this.getCurrentYearAndMonth(date);
                const currentMonth = this.data.months.filter((item) => item.year === year && item.month === month);
                this.updateActionButton(date);
                this.setData({
                    currentMonth: currentMonth.length > 0 ? currentMonth : [this.data.months[0]],
                });
            },
            calcMonths() {
                const months = this.base.getMonths();
                this.setData({
                    months,
                });
            },
            close(trigger) {
                if (this.data.autoClose) {
                    this.setData({ visible: false });
                }
                this.triggerEvent('close', { trigger });
            },
            onVisibleChange() {
                this.close('overlay');
            },
            handleClose() {
                this.close('close-btn');
            },
            handleSelect(e) {
                const { date, year, month } = e.currentTarget.dataset;
                if (date.type === 'disabled')
                    return;
                const rawValue = this.base.select({ cellType: date.type, year, month, date: date.day });
                const value = this.toTime(rawValue);
                this.calcMonths();
                if (this.data.switchMode !== 'none') {
                    const date = this.getCurrentDate();
                    this.calcCurrentMonth(date);
                }
                if (this.data.confirmBtn == null) {
                    if (this.data.type === 'single' || rawValue.length === 2) {
                        this.setData({ visible: false });
                        this._trigger('change', { value });
                    }
                }
                this.triggerEvent('select', { value });
            },
            onTplButtonTap() {
                const rawValue = this.base.getTrimValue();
                const value = this.toTime(rawValue);
                this.close('confirm-btn');
                this._trigger('confirm', { value });
            },
            toTime(val) {
                if (Array.isArray(val)) {
                    return val.map((item) => item.getTime());
                }
                return val.getTime();
            },
            onScroll(e) {
                this.triggerEvent('scroll', e.detail);
            },
            getCurrentDate() {
                var _a, _b;
                let time = Array.isArray(this.base.value) ? this.base.value[0] : this.base.value;
                if (this.data.currentMonth.length > 0) {
                    const year = (_a = this.data.currentMonth[0]) === null || _a === void 0 ? void 0 : _a.year;
                    const month = (_b = this.data.currentMonth[0]) === null || _b === void 0 ? void 0 : _b.month;
                    time = new Date(year, month, 1).getTime();
                }
                return time;
            },
            handleSwitchModeChange(e) {
                const { type, disabled } = e.currentTarget.dataset;
                if (disabled)
                    return;
                const date = this.getCurrentDate();
                const funcMap = {
                    'pre-year': () => getPrevYear(date),
                    'pre-month': () => getPrevMonth(date),
                    'next-month': () => getNextMonth(date),
                    'next-year': () => getNextYear(date),
                };
                const newValue = funcMap[type]();
                if (!newValue)
                    return;
                const { year, month } = this.getCurrentYearAndMonth(newValue);
                this.triggerEvent('panel-change', { year, month: month + 1 });
                this.calcCurrentMonth(newValue);
            },
        };
    }
};
Calendar = __decorate([
    wxComponent()
], Calendar);
export default Calendar;
