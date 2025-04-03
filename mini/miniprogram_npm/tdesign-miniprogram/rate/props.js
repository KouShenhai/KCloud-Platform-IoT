const props = {
    allowHalf: {
        type: Boolean,
        value: false,
    },
    color: {
        type: null,
        value: '#ED7B2F',
    },
    count: {
        type: Number,
        value: 5,
    },
    disabled: {
        type: null,
        value: undefined,
    },
    gap: {
        type: null,
        value: 8,
    },
    icon: {
        type: null,
    },
    iconPrefix: {
        type: String,
        value: undefined,
    },
    placement: {
        type: String,
        value: 'top',
    },
    showText: {
        type: Boolean,
        value: false,
    },
    size: {
        type: String,
        value: '24px',
    },
    texts: {
        type: Array,
        value: [],
    },
    value: {
        type: Number,
        value: null,
    },
    defaultValue: {
        type: Number,
        value: 0,
    },
    variant: {
        type: String,
        value: 'outline',
    },
};
export default props;
