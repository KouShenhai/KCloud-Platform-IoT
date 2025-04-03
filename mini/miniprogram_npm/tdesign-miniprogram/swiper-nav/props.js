const props = {
    current: {
        type: Number,
        value: 0,
    },
    direction: {
        type: String,
        value: 'horizontal',
    },
    minShowNum: {
        type: Number,
        value: 2,
    },
    paginationPosition: {
        type: String,
        value: 'bottom',
    },
    showControls: {
        type: Boolean,
        value: false,
    },
    total: {
        type: Number,
        value: 0,
    },
    type: {
        type: String,
        value: 'dots',
    },
};
export default props;
