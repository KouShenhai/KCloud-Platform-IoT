import api from "./api";
const http = (options) = > {
	const accessToken = uni.getStorageSync('accessToken')
	return new Promise((resolve, reject) => {
		uni.request({
			// 接口地址：前缀+方法中传入的地址
			url: api.url() + options.url, 
			// 请求方法：传入的方法或者默认是GET
			method: options.method || 'GET',
			// 传递参数：传入的参数或者默认传递空集合
			data: options.data || {},
			header: {
			   'Authorization': `Bearer ${accessToken}`,
			},
			success: (res) => {
				// 自定请求失败的情况
				if(res.data.code !== 'OK') {
					uni.showToast({
					    title:res.data.msg,
					    icon: 'none'
					});
				}
				// 请求成功
				resolve(res.data)
			},
			fail: (err) => {
				uni.showToast({
				    title: "" + err.msg,
				    icon: 'none'
				});
				reject(err)
			}
		})
	})
}
export default http