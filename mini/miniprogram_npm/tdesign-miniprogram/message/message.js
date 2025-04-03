var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
import { SuperComponent, wxComponent } from '../common/src/index';
import config from '../common/config';
import { MessageType } from './message.interface';
import props from './props';
import { unitConvert } from '../common/utils';
const SHOW_DURATION = 400;
const { prefix } = config;
const name = `${prefix}-message`;
let Message = class Message extends SuperComponent {
    constructor() {
        super(...arguments);
        this.options = {
            multipleSlots: true,
        };
        this.properties = Object.assign({}, props);
        this.data = {
            prefix,
            classPrefix: name,
            messageList: [],
        };
        this.index = 0;
        this.instances = [];
        this.gap = 12;
        this.observers = {
            visible(value) {
                if (value) {
                    this.setMessage(this.properties, this.properties.theme);
                }
                else {
                    this.setData({
                        messageList: [],
                    });
                }
            },
        };
        this.pageLifetimes = {
            show() {
                this.hideAll();
            },
        };
        this.lifetimes = {
            ready() {
                this.memoInitialData();
            },
        };
    }
    memoInitialData() {
        this.initialData = Object.assign(Object.assign({}, this.properties), this.data);
    }
    setMessage(msg, theme = MessageType.info) {
        let id = `${name}_${this.index}`;
        if (msg.single) {
            id = name;
        }
        this.gap = unitConvert(msg.gap || this.gap);
        const msgObj = Object.assign(Object.assign({}, msg), { theme,
            id, gap: this.gap });
        const instanceIndex = this.instances.findIndex((x) => x.id === id);
        if (instanceIndex < 0) {
            this.addMessage(msgObj);
        }
        else {
            const instance = this.instances[instanceIndex];
            const offsetHeight = this.getOffsetHeight(instanceIndex);
            instance.resetData(() => {
                instance.setData(msgObj, instance.show.bind(instance, offsetHeight));
                instance.onHide = () => {
                    this.close(id);
                };
            });
        }
    }
    addMessage(msgObj) {
        const list = [...this.data.messageList, { id: msgObj.id }];
        this.setData({
            messageList: list,
        }, () => {
            const offsetHeight = this.getOffsetHeight();
            const instance = this.showMessageItem(msgObj, msgObj.id, offsetHeight);
            if (this.instances) {
                this.instances.push(instance);
                this.index += 1;
            }
        });
    }
    getOffsetHeight(index = -1) {
        let offsetHeight = 0;
        let len = index;
        if (len === -1 || len > this.instances.length) {
            len = this.instances.length;
        }
        for (let i = 0; i < len; i += 1) {
            const instance = this.instances[i];
            offsetHeight += instance.data.height + instance.data.gap;
        }
        return offsetHeight;
    }
    showMessageItem(options, id, offsetHeight) {
        const instance = this.selectComponent(`#${id}`);
        if (instance) {
            instance.resetData(() => {
                instance.setData(options, instance.show.bind(instance, offsetHeight));
                instance.onHide = () => {
                    this.close(id);
                };
            });
            return instance;
        }
        console.error('未找到组件,请确认 selector && context 是否正确');
    }
    close(id) {
        setTimeout(() => {
            this.removeMsg(id);
        }, SHOW_DURATION);
        this.removeInstance(id);
    }
    hide(id) {
        if (!id) {
            this.hideAll();
        }
        const instance = this.instances.find((x) => x.id === id);
        if (instance) {
            instance.hide();
        }
    }
    hideAll() {
        for (let i = 0; i < this.instances.length;) {
            const instance = this.instances[i];
            instance.hide();
        }
    }
    removeInstance(id) {
        const index = this.instances.findIndex((x) => x.id === id);
        if (index < 0)
            return;
        const instance = this.instances[index];
        const removedHeight = instance.data.height;
        this.instances.splice(index, 1);
        for (let i = index; i < this.instances.length; i += 1) {
            const instance = this.instances[i];
            instance.setData({
                wrapTop: instance.data.wrapTop - removedHeight - instance.data.gap,
            });
        }
    }
    removeMsg(id) {
        const msgIndex = this.data.messageList.findIndex((x) => x.id === id);
        if (msgIndex > -1) {
            this.data.messageList.splice(msgIndex, 1);
            this.setData({
                messageList: this.data.messageList,
            });
        }
    }
    handleClose() {
        this.triggerEvent('close-btn-click');
    }
    handleLinkClick() {
        this.triggerEvent('link-click');
    }
    handleDurationEnd() {
        this.triggerEvent('duration-end');
    }
};
Message = __decorate([
    wxComponent()
], Message);
export default Message;
