const props = {
    align: {
        type: String,
        value: 'left',
    },
    closeBtn: {
        type: null,
        value: false,
    },
    content: {
        type: String,
    },
    duration: {
        type: Number,
        value: 3000,
    },
    gap: {
        type: null,
        value: 12,
    },
    icon: {
        type: null,
        value: true,
    },
    link: {
        type: null,
    },
    marquee: {
        type: null,
        value: false,
    },
    offset: {
        type: Array,
    },
    single: {
        type: Boolean,
        value: true,
    },
    theme: {
        type: String,
        value: 'info',
    },
    visible: {
        type: Boolean,
        value: false,
    },
    defaultVisible: {
        type: Boolean,
        value: false,
    },
    zIndex: {
        type: Number,
        value: 15000,
    },
};
export default props;
