const props = {
    autoClose: {
        type: Boolean,
        value: true,
    },
    confirmBtn: {
        type: null,
        value: '',
    },
    firstDayOfWeek: {
        type: Number,
        value: 0,
    },
    format: {
        type: null,
    },
    localeText: {
        type: Object,
    },
    maxDate: {
        type: Number,
    },
    minDate: {
        type: Number,
    },
    switchMode: {
        type: String,
        value: 'none',
    },
    title: {
        type: String,
    },
    type: {
        type: String,
        value: 'single',
    },
    usePopup: {
        type: Boolean,
        value: true,
    },
    usingCustomNavbar: {
        type: Boolean,
        value: false,
    },
    value: {
        type: null,
        value: null,
    },
    defaultValue: {
        type: null,
    },
    visible: {
        type: Boolean,
        value: false,
    },
};
export default props;
