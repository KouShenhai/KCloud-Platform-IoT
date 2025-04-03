const props = {
    disabled: {
        type: Boolean,
        value: false,
    },
    enableBackToTop: {
        type: Boolean,
        value: true,
    },
    enablePassive: {
        type: Boolean,
        value: false,
    },
    externalClasses: {
        type: Array,
    },
    loadingBarHeight: {
        type: null,
        value: 50,
    },
    loadingProps: {
        type: Object,
    },
    loadingTexts: {
        type: Array,
        value: [],
    },
    lowerThreshold: {
        type: null,
        value: 50,
    },
    maxBarHeight: {
        type: null,
        value: 80,
    },
    refreshTimeout: {
        type: Number,
        value: 3000,
    },
    scrollIntoView: {
        type: String,
        value: '',
    },
    showScrollbar: {
        type: Boolean,
        value: true,
    },
    upperThreshold: {
        type: null,
        value: 50,
    },
    usingCustomNavbar: {
        type: Boolean,
        value: false,
    },
    value: {
        type: Boolean,
        value: null,
    },
    defaultValue: {
        type: Boolean,
        value: false,
    },
};
export default props;
