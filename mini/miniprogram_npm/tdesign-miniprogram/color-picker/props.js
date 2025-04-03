const props = {
    autoClose: {
        type: Boolean,
        value: true,
    },
    enableAlpha: {
        type: Boolean,
        value: false,
    },
    fixed: {
        type: Boolean,
        value: false,
    },
    format: {
        type: String,
        value: 'RGB',
    },
    popupProps: {
        type: Object,
        value: {},
    },
    swatchColors: {
        type: Array,
    },
    type: {
        type: String,
        value: 'base',
    },
    usePopup: {
        type: Boolean,
        value: false,
    },
    value: {
        type: String,
        value: null,
    },
    defaultValue: {
        type: String,
        value: '',
    },
    visible: {
        type: Boolean,
        value: false,
    },
};
export default props;
