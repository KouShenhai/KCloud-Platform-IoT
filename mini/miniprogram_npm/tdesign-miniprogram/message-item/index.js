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
import { MessageType } from '../message/message.interface';
import { getInstance } from '../common/utils';
const showMessage = function (options, theme = MessageType.info) {
    const { context, selector = '#t-message' } = options, otherOptions = __rest(options, ["context", "selector"]);
    const instance = getInstance(context, selector);
    if (instance) {
        instance.resetData(() => {
            instance.setData(Object.assign({ theme }, otherOptions), instance.show.bind(instance));
        });
        return instance;
    }
    console.error('未找到组件,请确认 selector && context 是否正确');
};
export default {
    info(options) {
        return showMessage(options, MessageType.info);
    },
    success(options) {
        return showMessage(options, MessageType.success);
    },
    warning(options) {
        return showMessage(options, MessageType.warning);
    },
    error(options) {
        return showMessage(options, MessageType.error);
    },
    hide(options) {
        const { context, selector = '#t-message' } = Object.assign({}, options);
        const instance = getInstance(context, selector);
        if (!instance) {
            return;
        }
        instance.hide();
    },
};
