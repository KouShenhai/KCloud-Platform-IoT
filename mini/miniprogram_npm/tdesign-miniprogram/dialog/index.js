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
import props from './props';
import { getInstance } from '../common/utils';
const defaultOptions = {
    actions: [],
    buttonLayout: props.buttonLayout.value,
    cancelBtn: props.cancelBtn.value,
    closeOnOverlayClick: props.closeOnOverlayClick.value,
    confirmBtn: props.confirmBtn.value,
    content: '',
    preventScrollThrough: props.preventScrollThrough.value,
    showOverlay: props.showOverlay.value,
    title: '',
    visible: props.visible.value,
};
export default {
    alert(options) {
        const _a = Object.assign(Object.assign({}, defaultOptions), options), { context, selector = '#t-dialog' } = _a, otherOptions = __rest(_a, ["context", "selector"]);
        const instance = getInstance(context, selector);
        if (!instance)
            return Promise.reject();
        return new Promise((resolve) => {
            instance.setData(Object.assign(Object.assign({ cancelBtn: '' }, otherOptions), { visible: true }));
            instance._onConfirm = resolve;
        });
    },
    confirm(options) {
        const _a = Object.assign(Object.assign({}, defaultOptions), options), { context, selector = '#t-dialog' } = _a, otherOptions = __rest(_a, ["context", "selector"]);
        const instance = getInstance(context, selector);
        if (!instance)
            return Promise.reject();
        return new Promise((resolve, reject) => {
            instance.setData(Object.assign(Object.assign({}, otherOptions), { visible: true }));
            instance._onConfirm = resolve;
            instance._onCancel = reject;
        });
    },
    close(options) {
        const { context, selector = '#t-dialog' } = Object.assign({}, options);
        const instance = getInstance(context, selector);
        if (instance) {
            instance.close();
            return Promise.resolve();
        }
        return Promise.reject();
    },
    action(options) {
        const _a = Object.assign(Object.assign({}, defaultOptions), options), { context, selector = '#t-dialog', actions } = _a, otherOptions = __rest(_a, ["context", "selector", "actions"]);
        const instance = getInstance(context, selector);
        if (!instance)
            return Promise.reject();
        const { buttonLayout = 'vertical' } = options;
        const maxLengthSuggestion = buttonLayout === 'vertical' ? 7 : 3;
        if (!actions || (typeof actions === 'object' && (actions.length === 0 || actions.length > maxLengthSuggestion))) {
            console.warn(`action 数量建议控制在1至${maxLengthSuggestion}个`);
        }
        return new Promise((resolve) => {
            instance.setData(Object.assign(Object.assign({ actions }, otherOptions), { buttonLayout, visible: true }));
            instance._onAction = resolve;
        });
    },
};
