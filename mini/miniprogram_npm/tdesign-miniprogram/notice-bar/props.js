const props = {
    content: {
        type: null,
    },
    direction: {
        type: String,
        value: 'horizontal',
    },
    interval: {
        type: Number,
        value: 2000,
    },
    marquee: {
        type: null,
        value: false,
    },
    operation: {
        type: String,
    },
    prefixIcon: {
        type: null,
        value: true,
    },
    suffixIcon: {
        type: null,
    },
    theme: {
        type: String,
        value: 'info',
    },
    visible: {
        type: Boolean,
        value: null,
    },
    defaultVisible: {
        type: Boolean,
        value: false,
    },
};
export default props;
