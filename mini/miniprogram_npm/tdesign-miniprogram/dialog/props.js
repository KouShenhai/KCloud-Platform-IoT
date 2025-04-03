const props = {
    actions: {
        type: Array,
    },
    buttonLayout: {
        type: String,
        value: 'horizontal',
    },
    cancelBtn: {
        type: null,
    },
    closeBtn: {
        type: null,
        value: false,
    },
    closeOnOverlayClick: {
        type: Boolean,
        value: false,
    },
    confirmBtn: {
        type: null,
    },
    content: {
        type: String,
    },
    externalClasses: {
        type: Array,
    },
    overlayProps: {
        type: Object,
        value: {},
    },
    preventScrollThrough: {
        type: Boolean,
        value: true,
    },
    showOverlay: {
        type: Boolean,
        value: true,
    },
    style: {
        type: String,
        value: '',
    },
    title: {
        type: String,
    },
    visible: {
        type: Boolean,
    },
    zIndex: {
        type: Number,
        value: 11500,
    },
};
export default props;
