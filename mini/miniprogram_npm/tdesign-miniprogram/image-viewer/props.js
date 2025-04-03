const props = {
    backgroundColor: {
        type: String,
        value: 'rgba(0, 0, 0, 1)',
    },
    closeBtn: {
        type: null,
        value: false,
    },
    deleteBtn: {
        type: null,
        value: false,
    },
    images: {
        type: Array,
        value: [],
    },
    initialIndex: {
        type: Number,
        value: 0,
    },
    showIndex: {
        type: Boolean,
        value: false,
    },
    usingCustomNavbar: {
        type: Boolean,
        value: false,
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
