const props = {
    allowUncheck: {
        type: Boolean,
        value: false,
    },
    borderless: {
        type: Boolean,
        value: false,
    },
    disabled: {
        type: null,
        value: undefined,
    },
    icon: {
        type: null,
        value: 'circle',
    },
    keys: {
        type: Object,
    },
    name: {
        type: String,
        value: '',
    },
    options: {
        type: Array,
    },
    placement: {
        type: String,
        value: 'left',
    },
    readonly: {
        type: null,
        value: undefined,
    },
    value: {
        type: null,
        value: null,
    },
    defaultValue: {
        type: null,
    },
};
export default props;
