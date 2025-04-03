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
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import props from './props';
import { getRect } from '../common/utils';
const { prefix } = config;
const name = `${prefix}-cascader`;
function parseOptions(options, keys) {
    var _a, _b;
    const label = (_a = keys === null || keys === void 0 ? void 0 : keys.label) !== null && _a !== void 0 ? _a : 'label';
    const value = (_b = keys === null || keys === void 0 ? void 0 : keys.value) !== null && _b !== void 0 ? _b : 'value';
    return options.map((item) => {
        return {
            [label]: item[label],
            [value]: item[value],
        };
    });
}
const defaultState = {
    contentHeight: 0,
    stepHeight: 0,
    tabsHeight: 0,
    subTitlesHeight: 0,
    stepsInitHeight: 0,
};
let Cascader = class Cascader extends SuperComponent {
    constructor() {
        super(...arguments);
        this.externalClasses = [`${prefix}-class`];
        this.options = {
            multipleSlots: true,
            pureDataPattern: /^options$/,
        };
        this.properties = props;
        this.controlledProps = [
            {
                key: 'value',
                event: 'change',
            },
        ];
        this.state = Object.assign({}, defaultState);
        this.data = {
            prefix,
            name,
            stepIndex: 0,
            selectedIndexes: [],
            selectedValue: [],
            scrollTopList: [],
            steps: [],
            _optionsHeight: 0,
        };
        this.observers = {
            visible(v) {
                if (v) {
                    const $tabs = this.selectComponent('#tabs');
                    $tabs === null || $tabs === void 0 ? void 0 : $tabs.setTrack();
                    $tabs === null || $tabs === void 0 ? void 0 : $tabs.getTabHeight().then((res) => {
                        this.state.tabsHeight = res.height;
                    });
                    this.initOptionsHeight(this.data.steps.length);
                    this.updateScrollTop();
                    this.initWithValue();
                }
                else {
                    this.state = Object.assign({}, defaultState);
                }
            },
            value() {
                this.initWithValue();
            },
            options() {
                const { selectedValue, steps, items } = this.genItems();
                this.setData({
                    steps,
                    items,
                    selectedValue,
                    stepIndex: items.length - 1,
                });
            },
            selectedIndexes() {
                const { visible, theme } = this.properties;
                const { selectedValue, steps, items } = this.genItems();
                const setData = {
                    steps,
                    selectedValue,
                    stepIndex: items.length - 1,
                };
                if (JSON.stringify(items) !== JSON.stringify(this.data.items)) {
                    Object.assign(setData, { items });
                }
                this.setData(setData);
                if (visible && theme === 'step') {
                    this.updateOptionsHeight(steps.length);
                }
            },
            stepIndex() {
                return __awaiter(this, void 0, void 0, function* () {
                    const { visible } = this.data;
                    if (visible) {
                        this.updateScrollTop();
                    }
                });
            },
        };
        this.methods = {
            updateOptionsHeight(steps) {
                const { contentHeight, stepsInitHeight, stepHeight, subTitlesHeight } = this.state;
                this.setData({
                    _optionsHeight: contentHeight - stepsInitHeight - subTitlesHeight - (steps - 1) * stepHeight,
                });
            },
            initOptionsHeight(steps) {
                return __awaiter(this, void 0, void 0, function* () {
                    const { theme, subTitles } = this.properties;
                    const { height } = yield getRect(this, `.${name}__content`);
                    this.state.contentHeight = height;
                    if (theme === 'step') {
                        yield Promise.all([getRect(this, `.${name}__steps`), getRect(this, `.${name}__step`)]).then(([stepsRect, stepRect]) => {
                            this.state.stepsInitHeight = stepsRect.height - (steps - 1) * stepRect.height;
                            this.state.stepHeight = stepRect.height;
                        });
                    }
                    if (subTitles.length > 0) {
                        const { height } = yield getRect(this, `.${name}__options-title`);
                        this.state.subTitlesHeight = height;
                    }
                    const optionsInitHeight = this.state.contentHeight - this.state.subTitlesHeight;
                    this.setData({
                        _optionsHeight: theme === 'step'
                            ? optionsInitHeight - this.state.stepsInitHeight - (steps - 1) * this.state.stepHeight
                            : optionsInitHeight - this.state.tabsHeight,
                    });
                });
            },
            initWithValue() {
                if (this.data.value != null && this.data.value !== '') {
                    const selectedIndexes = this.getIndexesByValue(this.data.options, this.data.value);
                    if (selectedIndexes) {
                        this.setData({ selectedIndexes });
                    }
                }
                else {
                    this.setData({ selectedIndexes: [] });
                }
            },
            getIndexesByValue(options, value) {
                var _a, _b, _c;
                const { keys } = this.data;
                for (let i = 0, size = options.length; i < size; i += 1) {
                    const opt = options[i];
                    if (opt[(_a = keys === null || keys === void 0 ? void 0 : keys.value) !== null && _a !== void 0 ? _a : 'value'] === value) {
                        return [i];
                    }
                    if (opt[(_b = keys === null || keys === void 0 ? void 0 : keys.children) !== null && _b !== void 0 ? _b : 'children']) {
                        const res = this.getIndexesByValue(opt[(_c = keys === null || keys === void 0 ? void 0 : keys.children) !== null && _c !== void 0 ? _c : 'children'], value);
                        if (res) {
                            return [i, ...res];
                        }
                    }
                }
            },
            updateScrollTop() {
                const { visible, items, selectedIndexes, stepIndex } = this.data;
                if (visible) {
                    getRect(this, '.cascader-radio-group-0').then((rect) => {
                        var _a;
                        const eachRadioHeight = rect.height / ((_a = items[0]) === null || _a === void 0 ? void 0 : _a.length);
                        this.setData({
                            [`scrollTopList[${stepIndex}]`]: eachRadioHeight * selectedIndexes[stepIndex],
                        });
                    });
                }
            },
            hide(trigger) {
                this.setData({ visible: false });
                this.triggerEvent('close', { trigger: trigger });
            },
            onVisibleChange() {
                this.hide('overlay');
            },
            onClose() {
                if (this.data.checkStrictly) {
                    this.triggerChange();
                }
                this.hide('close-btn');
            },
            onStepClick(e) {
                const { index } = e.currentTarget.dataset;
                this.setData({ stepIndex: index });
            },
            onTabChange(e) {
                const { value } = e.detail;
                this.setData({
                    stepIndex: value,
                });
            },
            genItems() {
                var _a, _b, _c, _d, _e;
                const { options, selectedIndexes, keys, placeholder } = this.data;
                const selectedValue = [];
                const steps = [];
                const items = [parseOptions(options, keys)];
                if (options.length > 0) {
                    let current = options;
                    for (let i = 0, size = selectedIndexes.length; i < size; i += 1) {
                        const index = selectedIndexes[i];
                        const next = current[index];
                        current = next[(_a = keys === null || keys === void 0 ? void 0 : keys.children) !== null && _a !== void 0 ? _a : 'children'];
                        selectedValue.push(next[(_b = keys === null || keys === void 0 ? void 0 : keys.value) !== null && _b !== void 0 ? _b : 'value']);
                        steps.push(next[(_c = keys === null || keys === void 0 ? void 0 : keys.label) !== null && _c !== void 0 ? _c : 'label']);
                        if (next[(_d = keys === null || keys === void 0 ? void 0 : keys.children) !== null && _d !== void 0 ? _d : 'children']) {
                            items.push(parseOptions(next[(_e = keys === null || keys === void 0 ? void 0 : keys.children) !== null && _e !== void 0 ? _e : 'children'], keys));
                        }
                    }
                }
                if (steps.length < items.length) {
                    steps.push(placeholder);
                }
                return {
                    selectedValue,
                    steps,
                    items,
                };
            },
            handleSelect(e) {
                var _a, _b, _c, _d, _e;
                const { level } = e.target.dataset;
                const { value } = e.detail;
                const { checkStrictly } = this.properties;
                const { selectedIndexes, items, keys, options, selectedValue } = this.data;
                const index = items[level].findIndex((item) => { var _a; return item[(_a = keys === null || keys === void 0 ? void 0 : keys.value) !== null && _a !== void 0 ? _a : 'value'] === value; });
                let item = selectedIndexes.slice(0, level).reduce((acc, item, index) => {
                    var _a;
                    if (index === 0) {
                        return acc[item];
                    }
                    return acc[(_a = keys === null || keys === void 0 ? void 0 : keys.children) !== null && _a !== void 0 ? _a : 'children'][item];
                }, options);
                if (level === 0) {
                    item = item[index];
                }
                else {
                    item = item[(_a = keys === null || keys === void 0 ? void 0 : keys.children) !== null && _a !== void 0 ? _a : 'children'][index];
                }
                if (item.disabled) {
                    return;
                }
                this.triggerEvent('pick', {
                    value: item[(_b = keys === null || keys === void 0 ? void 0 : keys.value) !== null && _b !== void 0 ? _b : 'value'],
                    label: item[(_c = keys === null || keys === void 0 ? void 0 : keys.label) !== null && _c !== void 0 ? _c : 'label'],
                    index,
                    level,
                });
                selectedIndexes[level] = index;
                if (checkStrictly && selectedValue.includes(String(value))) {
                    selectedIndexes.length = level;
                    this.setData({ selectedIndexes });
                    return;
                }
                selectedIndexes.length = level + 1;
                const { items: newItems } = this.genItems();
                if ((_e = item === null || item === void 0 ? void 0 : item[(_d = keys === null || keys === void 0 ? void 0 : keys.children) !== null && _d !== void 0 ? _d : 'children']) === null || _e === void 0 ? void 0 : _e.length) {
                    this.setData({
                        selectedIndexes,
                        [`items[${level + 1}]`]: newItems[level + 1],
                    });
                }
                else {
                    this.setData({
                        selectedIndexes,
                    }, this.triggerChange);
                    this.hide('finish');
                }
            },
            triggerChange() {
                var _a;
                const { items, selectedValue, selectedIndexes } = this.data;
                this._trigger('change', {
                    value: (_a = selectedValue[selectedValue.length - 1]) !== null && _a !== void 0 ? _a : '',
                    selectedOptions: items.map((item, index) => item[selectedIndexes[index]]).filter(Boolean),
                });
            },
        };
    }
};
Cascader = __decorate([
    wxComponent()
], Cascader);
export default Cascader;
