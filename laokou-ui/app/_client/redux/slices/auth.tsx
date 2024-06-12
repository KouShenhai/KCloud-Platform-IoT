import { createAsyncThunk, createSlice,createAction } from '@reduxjs/toolkit';


// 发起网络请求获取数据
const loadMoviesAPI = () =>
  fetch(
    'https://pcw-api.iqiyi.com/search/recommend/list?channel_id=1&data_type=1&mode=11&page_id=2&ret_num=48'
  ).then((res) => res.json())

// 这个action是可以直接调用的，用来处理异步操作获取数据
export const loadData = createAsyncThunk('movie/loadData', async () => {
  const res = await loadMoviesAPI()
  return res // 此处的返回结果会在 .fulfilled中作为payload的值
})

type movieState = {
  list: any[]
  total: number
}

const initialState: movieState = {
  list: [],
  total: 0,
}


export const authSlice = createSlice({
  name: 'movie',
  initialState,
  reducers: {
    changeData(state, { payload }) {
      state.list = payload
      state.total = payload.length
    },
  },

  extraReducers: (builder) => {
    builder
      .addCase(loadData.pending, () => {
        console.log('loading')
      })
      .addCase(loadData.rejected, (err) => {
        console.log(err)
      })
  }
})

export default authSlice.reducer

