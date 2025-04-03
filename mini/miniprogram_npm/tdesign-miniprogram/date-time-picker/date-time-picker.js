var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var _a, _b;
import dayjs from 'dayjs';
import localeData from 'dayjs/plugin/localeData';
import config from '../common/config';
import { SuperComponent, wxComponent } from '../common/src/index';
import props from './props';
import dayjsLocaleMap from './locale/dayjs';
dayjs.extend(localeData);
dayjs.locale('zh-cn');
const defaultLocale = ((_a = dayjsLocaleMap[dayjs.locale()]) === null || _a === void 0 ? void 0 : _a.key) || ((_b = dayjsLocaleMap.default) === null || _b === void 0 ? void 0 : _b.key);
const { prefix } = config;
const name = `${prefix}-date-time-picker`;
var ModeItem;
(function (ModeItem) {
    ModeItem["YEAR"] = "year";
    ModeItem["MONTH"] = "month";
    ModeItem["DATE"] = "date";
    ModeItem["HOUR"] = "hour";
    ModeItem["MINUTE"] = "minute";
    ModeItem["SECOND"] = "second";
})(ModeItem || (ModeItem = {}));
const DATE_MODES = ['year', 'month', 'date'];
const TIME_MODES = ['hour', 'minute', 'second'];
const FULL_MODES = [...DATE_MODES, ...TIME_MODES];
const DEFAULT_MIN_DATE = dayjs('2000-01-01 00:00:00');
const DEFAULT_MAX_DATE = dayjs('2030-12-31 23:59:59');
let DateTimePicker = class DateTimePicker extends SuperComponent {
    constructor() {
        super(...arguments);
        this.properties = props;
        this.externalClasses = [`${prefix}-class`, `${prefix}-class-confirm`, `${prefix}-class-cancel`, `${prefix}-class-title`];
        this.options = {
            multipleSlots: true,
        };
        this.observers = {
            'start, end, value': function () {
                this.updateColumns();
            },
            customLocale(v) {
                if (!v || !dayjsLocaleMap[v].key)
                    return;
                this.setData({
                    locale: dayjsLocaleMap[v].i18n,
                    dayjsLocale: dayjsLocaleMap[v].key,
                });
            },
            mode(m) {
                const fullModes = this.getFullModeArray(m);
                this.setData({
                    fullModes,
                });
                this.updateColumns();
            },
        };
        this.date = null;
        this.data = {
            prefix,
            classPrefix: name,
            columns: [],
            columnsValue: [],
            fullModes: [],
            locale: dayjsLocaleMap[defaultLocale].i18n,
            dayjsLocale: dayjsLocaleMap[defaultLocale].key,
        };
        this.controlledProps = [
            {
                key: 'value',
                event: 'change',
            },
        ];
        this.methods = {
            updateColumns() {
                this.date = this.getParseDate();
                const { columns, columnsValue } = this.getValueCols();
                this.setData({
                    columns,
                    columnsValue,
                });
            },
            getParseDate() {
                const { value, defaultValue } = this.properties;
                const minDate = this.getMinDate();
                const isTimeMode = this.isTimeMode();
                let currentValue = value || defaultValue;
                if (isTimeMode) {
                    const dateStr = dayjs(minDate).format('YYYY-MM-DD');
                    currentValue = dayjs(`${dateStr} ${currentValue}`);
                }
                const parseDate = dayjs(currentValue || minDate);
                const isDateValid = parseDate.isValid();
                return isDateValid ? parseDate : minDate;
            },
            getMinDate() {
                const { start } = this.properties;
                return start ? dayjs(start) : DEFAULT_MIN_DATE;
            },
            getMaxDate() {
                const { end } = this.properties;
                return end ? dayjs(end) : DEFAULT_MAX_DATE;
            },
            getDateRect(type = 'default') {
                const map = {
                    min: 'getMinDate',
                    max: 'getMaxDate',
                    default: 'getDate',
                };
                const date = this[map[type]]();
                const keys = ['year', 'month', 'date', 'hour', 'minute', 'second'];
                return keys.map((k) => { var _a; return (_a = date[k]) === null || _a === void 0 ? void 0 : _a.call(date); });
            },
            getDate() {
                return this.clipDate((this === null || this === void 0 ? void 0 : this.date) || DEFAULT_MIN_DATE);
            },
            clipDate(date) {
                const minDate = this.getMinDate();
                const maxDate = this.getMaxDate();
                return dayjs(Math.min(Math.max(minDate.valueOf(), date.valueOf()), maxDate.valueOf()));
            },
            setYear(date, year) {
                const beforeMonthDays = date.date();
                const afterMonthDays = date.year(year).daysInMonth();
                const tempDate = date.date(Math.min(beforeMonthDays.valueOf(), afterMonthDays.valueOf()));
                return tempDate.year(year);
            },
            setMonth(date, month) {
                const beforeMonthDays = date.date();
                const afterMonthDays = date.month(month).daysInMonth();
                const tempDate = date.date(Math.min(beforeMonthDays.valueOf(), afterMonthDays.valueOf()));
                return tempDate.month(month);
            },
            getColumnOptions() {
                const { fullModes, filter } = this.data;
                const columnOptions = [];
                fullModes === null || fullModes === void 0 ? void 0 : fullModes.forEach((mode) => {
                    const columnOption = this.getOptionByType(mode);
                    if (typeof filter === 'function') {
                        columnOptions.push(filter(mode, columnOption));
                    }
                    else {
                        columnOptions.push(columnOption);
                    }
                });
                return columnOptions;
            },
            getOptionByType(type) {
                var _a;
                const { locale, steps } = this.data;
                const options = [];
                const minEdge = this.getOptionEdge('min', type);
                const maxEdge = this.getOptionEdge('max', type);
                const step = (_a = steps === null || steps === void 0 ? void 0 : steps[type]) !== null && _a !== void 0 ? _a : 1;
                const dayjsMonthsShort = dayjs().locale(this.data.dayjsLocale).localeData().monthsShort();
                for (let i = minEdge; i <= maxEdge; i += step) {
                    options.push({
                        value: `${i}`,
                        label: type === 'month' ? dayjsMonthsShort[i] : `${i + locale[type]}`,
                    });
                }
                return options;
            },
            getYearOptions(dateParams) {
                const { locale } = this.data;
                const { minDateYear, maxDateYear } = dateParams;
                const years = [];
                for (let i = minDateYear; i <= maxDateYear; i += 1) {
                    years.push({
                        value: `${i}`,
                        label: `${i + locale.year}`,
                    });
                }
                return years;
            },
            getOptionEdge(minOrMax, type) {
                const selDateArray = this.getDateRect();
                const compareArray = this.getDateRect(minOrMax);
                const edge = {
                    month: [0, 11],
                    date: [1, this.getDate().daysInMonth()],
                    hour: [0, 23],
                    minute: [0, 59],
                    second: [0, 59],
                };
                const types = ['year', 'month', 'date', 'hour', 'minute', 'second'];
                for (let i = 0, size = selDateArray.length; i < size; i += 1) {
                    if (types[i] === type)
                        return compareArray[i];
                    if (compareArray[i] !== selDateArray[i])
                        return edge[type][minOrMax === 'min' ? 0 : 1];
                }
                return edge[type][minOrMax === 'min' ? 0 : 1];
            },
            getMonthOptions() {
                const months = [];
                const minMonth = this.getOptionEdge('min', 'month');
                const maxMonth = this.getOptionEdge('max', 'month');
                const dayjsMonthsShort = dayjs.monthsShort();
                for (let i = minMonth; i <= maxMonth; i += 1) {
                    months.push({
                        value: `${i}`,
                        label: dayjsMonthsShort[i],
                    });
                }
                return months;
            },
            getDayOptions() {
                const { locale } = this.data;
                const days = [];
                const minDay = this.getOptionEdge('min', 'date');
                const maxDay = this.getOptionEdge('max', 'date');
                for (let i = minDay; i <= maxDay; i += 1) {
                    days.push({
                        value: `${i}`,
                        label: `${i + locale.day}`,
                    });
                }
                return days;
            },
            getHourOptions() {
                const { locale } = this.data;
                const hours = [];
                const minHour = this.getOptionEdge('min', 'hour');
                const maxHour = this.getOptionEdge('max', 'hour');
                for (let i = minHour; i <= maxHour; i += 1) {
                    hours.push({
                        value: `${i}`,
                        label: `${i + locale.hour}`,
                    });
                }
                return hours;
            },
            getMinuteOptions() {
                const { locale } = this.data;
                const minutes = [];
                const minMinute = this.getOptionEdge('min', 'minute');
                const maxMinute = this.getOptionEdge('max', 'minute');
                for (let i = minMinute; i <= maxMinute; i += 1) {
                    minutes.push({
                        value: `${i}`,
                        label: `${i + locale.minute}`,
                    });
                }
                return minutes;
            },
            getValueCols() {
                return {
                    columns: this.getColumnOptions(),
                    columnsValue: this.getColumnsValue(),
                };
            },
            getColumnsValue() {
                const { fullModes } = this.data;
                const date = this.getDate();
                const columnsValue = [];
                fullModes === null || fullModes === void 0 ? void 0 : fullModes.forEach((mode) => {
                    columnsValue.push(`${date[mode]()}`);
                });
                return columnsValue;
            },
            getNewDate(value, type) {
                let newValue = this.getDate();
                switch (type) {
                    case ModeItem.YEAR:
                        newValue = this.setYear(newValue, value);
                        break;
                    case ModeItem.MONTH:
                        newValue = this.setMonth(newValue, value);
                        break;
                    case ModeItem.DATE:
                        newValue = newValue.date(value);
                        break;
                    case ModeItem.HOUR:
                        newValue = newValue.hour(value);
                        break;
                    case ModeItem.MINUTE:
                        newValue = newValue.minute(value);
                        break;
                    case ModeItem.SECOND:
                        newValue = newValue.second(value);
                        break;
                    default:
                        break;
                }
                return this.clipDate(newValue);
            },
            onColumnChange(e) {
                const { value, column } = e === null || e === void 0 ? void 0 : e.detail;
                const { fullModes, format } = this.data;
                const columnValue = value === null || value === void 0 ? void 0 : value[column];
                const columnType = fullModes === null || fullModes === void 0 ? void 0 : fullModes[column];
                const newValue = this.getNewDate(parseInt(columnValue, 10), columnType);
                this.date = newValue;
                const { columns, columnsValue } = this.getValueCols();
                this.setData({
                    columns,
                    columnsValue,
                });
                const date = this.getDate();
                const pickValue = format ? date.format(format) : date.valueOf();
                this.triggerEvent('pick', { value: pickValue });
            },
            onConfirm() {
                const { format } = this.properties;
                const date = this.getDate();
                const value = format ? date.format(format) : date.valueOf();
                this._trigger('change', { value });
                this.triggerEvent('confirm', { value });
                this.resetColumns();
            },
            onCancel() {
                this.resetColumns();
                this.triggerEvent('cancel');
            },
            onVisibleChange(e) {
                if (!e.detail.visible) {
                    this.resetColumns();
                }
            },
            onClose(e) {
                const { trigger } = e.detail;
                this.triggerEvent('close', { trigger });
            },
            resetColumns() {
                const parseDate = this.getParseDate();
                this.date = parseDate;
                const { columns, columnsValue } = this.getValueCols();
                this.setData({
                    columns,
                    columnsValue,
                });
            },
        };
    }
    getFullModeArray(mode) {
        if (typeof mode === 'string' || mode instanceof String) {
            return this.getFullModeByModeString(mode, FULL_MODES);
        }
        if (Array.isArray(mode)) {
            if ((mode === null || mode === void 0 ? void 0 : mode.length) === 1) {
                return this.getFullModeByModeString(mode[0], FULL_MODES);
            }
            if ((mode === null || mode === void 0 ? void 0 : mode.length) === 2) {
                const dateModes = this.getFullModeByModeString(mode[0], DATE_MODES);
                const timeModes = this.getFullModeByModeString(mode[1], TIME_MODES);
                return [...dateModes, ...timeModes];
            }
        }
    }
    getFullModeByModeString(modeString, matchModes) {
        if (!modeString) {
            return [];
        }
        const endIndex = matchModes === null || matchModes === void 0 ? void 0 : matchModes.findIndex((mode) => modeString === mode);
        return matchModes === null || matchModes === void 0 ? void 0 : matchModes.slice(0, endIndex + 1);
    }
    isTimeMode() {
        const { fullModes } = this.data;
        return fullModes[0] === ModeItem.HOUR;
    }
};
DateTimePicker = __decorate([
    wxComponent()
], DateTimePicker);
export default DateTimePicker;
