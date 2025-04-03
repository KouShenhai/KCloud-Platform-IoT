const props = {
    adjustPosition: {
        type: Boolean,
        value: true,
    },
    align: {
        type: String,
        value: 'left',
    },
    allowInputOverMax: {
        type: Boolean,
        value: false,
    },
    alwaysEmbed: {
        type: Boolean,
        value: false,
    },
    autoFocus: {
        type: Boolean,
        value: false,
    },
    borderless: {
        type: Boolean,
        value: false,
    },
    clearTrigger: {
        type: String,
        value: 'always',
    },
    clearable: {
        type: null,
        value: false,
    },
    confirmHold: {
        type: Boolean,
        value: false,
    },
    confirmType: {
        type: String,
        value: 'done',
    },
    cursor: {
        type: Number,
        required: true,
    },
    cursorColor: {
        type: String,
        value: '#0052d9',
    },
    cursorSpacing: {
        type: Number,
        value: 0,
    },
    disabled: {
        type: null,
        value: undefined,
    },
    focus: {
        type: Boolean,
        value: false,
    },
    format: {
        type: null,
    },
    holdKeyboard: {
        type: Boolean,
        value: false,
    },
    label: {
        type: String,
    },
    layout: {
        type: String,
        value: 'horizontal',
    },
    maxcharacter: {
        type: Number,
    },
    maxlength: {
        type: Number,
        value: -1,
    },
    placeholder: {
        type: String,
        value: undefined,
    },
    placeholderClass: {
        type: String,
        value: 'input-placeholder',
    },
    placeholderStyle: {
        type: String,
        value: '',
        required: true,
    },
    prefixIcon: {
        type: null,
    },
    readonly: {
        type: null,
        value: undefined,
    },
    safePasswordCertPath: {
        type: String,
        value: '',
    },
    safePasswordCustomHash: {
        type: String,
        value: '',
    },
    safePasswordLength: {
        type: Number,
    },
    safePasswordNonce: {
        type: String,
        value: '',
    },
    safePasswordSalt: {
        type: String,
        value: '',
    },
    safePasswordTimeStamp: {
        type: Number,
    },
    selectionEnd: {
        type: Number,
        value: -1,
    },
    selectionStart: {
        type: Number,
        value: -1,
    },
    status: {
        type: String,
        value: 'default',
    },
    suffix: {
        type: String,
    },
    suffixIcon: {
        type: null,
    },
    tips: {
        type: String,
    },
    type: {
        type: String,
        value: 'text',
    },
    value: {
        type: null,
    },
};
export default props;
