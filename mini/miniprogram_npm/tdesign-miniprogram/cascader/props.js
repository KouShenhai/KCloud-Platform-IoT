const props = {
    checkStrictly: {
        type: Boolean,
        value: false,
    },
    closeBtn: {
        type: Boolean,
        value: true,
    },
    keys: {
        type: Object,
    },
    options: {
        type: Array,
        value: [],
    },
    placeholder: {
        type: String,
        value: '选择选项',
    },
    subTitles: {
        type: Array,
        value: [],
    },
    theme: {
        type: String,
        value: 'step',
    },
    title: {
        type: String,
    },
    value: {
        type: null,
        value: null,
    },
    defaultValue: {
        type: null,
        value: null,
    },
    visible: {
        type: Boolean,
        value: false,
    },
};
export default props;
