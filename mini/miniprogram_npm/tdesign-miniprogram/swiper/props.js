const props = {
    autoplay: {
        type: Boolean,
        value: true,
    },
    current: {
        type: Number,
        value: 0,
    },
    direction: {
        type: String,
        value: 'horizontal',
    },
    displayMultipleItems: {
        type: Number,
        value: 1,
    },
    duration: {
        type: Number,
        value: 300,
    },
    easingFunction: {
        type: String,
        value: 'default',
    },
    height: {
        type: null,
        value: 192,
    },
    imageProps: {
        type: Object,
    },
    interval: {
        type: Number,
        value: 5000,
    },
    list: {
        type: Array,
    },
    loop: {
        type: Boolean,
        value: true,
    },
    navigation: {
        type: null,
        value: true,
    },
    nextMargin: {
        type: null,
        value: 0,
    },
    paginationPosition: {
        type: String,
        value: 'bottom',
    },
    previousMargin: {
        type: null,
        value: 0,
    },
    snapToEdge: {
        type: Boolean,
        value: false,
    },
};
export default props;
