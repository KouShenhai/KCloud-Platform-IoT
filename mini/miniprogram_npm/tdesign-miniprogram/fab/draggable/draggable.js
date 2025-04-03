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
import { SuperComponent, wxComponent } from '../../common/src/index';
import config from '../../common/config';
import props from './props';
import { getRect, systemInfo } from '../../common/utils';
const { prefix } = config;
const name = `${prefix}-draggable`;
let Draggable = class Draggable extends SuperComponent {
    constructor() {
        super(...arguments);
        this.properties = props;
        this.externalClasses = [`${prefix}-class`];
        this.data = {
            prefix,
            classPrefix: name,
        };
        this.lifetimes = {
            ready() {
                this.computedRect();
            },
        };
        this.methods = {
            onTouchStart(e) {
                if (this.properties.direction === 'none')
                    return;
                this.startX = e.touches[0].clientX + systemInfo.windowWidth - this.rect.right;
                this.startY = e.touches[0].clientY + systemInfo.windowHeight - this.rect.bottom;
                this.triggerEvent('start', { startX: this.startX, startY: this.startY, rect: this.rect, e });
            },
            onTouchMove(e) {
                if (this.properties.direction === 'none')
                    return;
                let x = this.startX - e.touches[0].clientX;
                let y = this.startY - e.touches[0].clientY;
                if (this.properties.direction === 'vertical') {
                    x = systemInfo.windowWidth - this.rect.right;
                }
                if (this.properties.direction === 'horizontal') {
                    y = systemInfo.windowHeight - this.rect.bottom;
                }
                this.triggerEvent('move', { x, y, rect: this.rect, e });
            },
            onTouchEnd(e) {
                return __awaiter(this, void 0, void 0, function* () {
                    if (this.properties.direction === 'none')
                        return;
                    yield this.computedRect();
                    this.triggerEvent('end', { rect: this.rect, e });
                });
            },
            computedRect() {
                return __awaiter(this, void 0, void 0, function* () {
                    this.rect = { right: 0, bottom: 0, width: 0, height: 0 };
                    try {
                        this.rect = yield getRect(this, `.${this.data.classPrefix}`);
                    }
                    catch (e) {
                    }
                });
            },
        };
    }
};
Draggable = __decorate([
    wxComponent()
], Draggable);
export default Draggable;
