var __rest = (this && this.__rest) || function (s, e) {
    var t = {};
    for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0)
        t[p] = s[p];
    if (s != null && typeof Object.getOwnPropertySymbols === "function")
        for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
            if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i]))
                t[p[i]] = s[p[i]];
        }
    return t;
};
import { getInstance } from '../common/utils';
export var ActionSheetTheme;
(function (ActionSheetTheme) {
    ActionSheetTheme["List"] = "list";
    ActionSheetTheme["Grid"] = "grid";
})(ActionSheetTheme || (ActionSheetTheme = {}));
export const show = function (options) {
    const _a = Object.assign({}, options), { context, selector = '#t-action-sheet' } = _a, otherOptions = __rest(_a, ["context", "selector"]);
    const instance = getInstance(context, selector);
    if (instance) {
        instance.show(Object.assign({}, otherOptions));
        return instance;
    }
    console.error('未找到组件,请确认 selector && context 是否正确');
};
export const close = function (options) {
    const { context, selector = '#t-action-sheet' } = Object.assign({}, options);
    const instance = getInstance(context, selector);
    if (instance) {
        instance.close();
    }
};
