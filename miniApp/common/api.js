export default {
	url: function() {
		let version = wx.getAccountInfoSync().miniProgram.envVersion
		switch(version) {
			case "develop":
				// 开发预览版
				return ""
			case "trial":
				// 体验版
				return ""
			case "release":
				// 稳定版
				return ""
			default:
				return ""
		}
	}
}