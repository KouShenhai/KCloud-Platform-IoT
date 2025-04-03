const props = {
    disabled: {
        type: Boolean,
        value: false,
    },
    externalClasses: {
        type: Array,
    },
    keys: {
        type: Object,
    },
    label: {
        type: String,
        value: '',
    },
    multiple: {
        type: Boolean,
        value: false,
    },
    options: {
        type: Array,
        value: [],
    },
    optionsColumns: {
        type: null,
        value: 1,
    },
    placement: {
        type: String,
        value: 'left',
    },
    value: {
        type: null,
        value: undefined,
    },
    defaultValue: {
        type: null,
        value: undefined,
    },
};
export default props;
