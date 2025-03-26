const baiduCode = require('./config/baiduCode.js'); // 百度统计hm码
const htmlModules = require('./config/htmlModules.js');


module.exports = {

	theme: 'vdoing', // 使用依赖包主题
	// theme: require.resolve('../../vdoing'), // 使用本地主题 (先将vdoing主题文件下载到本地：https://github.com/KouShenhai/KCloud-Platform-IoT)

	title: "KCloud-Platform-IoT",
	description: '一个企业级微服务架构的IoT云平台',
	// base: '/', // 默认'/'。如果你想将你的网站部署到如 https://foo.github.io/bar/，那么 base 应该被设置成 "/bar/",（否则页面将失去样式等文件）
	head: [ // 注入到页面<head> 中的标签，格式[tagName, { attrName: attrValue }, innerHTML?]
		['link', {rel: 'icon', href: '/img/favicon.ico'}], //favicons，资源放在public文件夹
		['meta', {name: 'keywords', content: 'vuepress,theme,blog,vdoing'}],
		['meta', {name: 'theme-color', content: '#11a8cd'}], // 移动浏览器主题颜色
		// ['meta', { name: 'wwads-cn-verify', content: '6c4b761a28b734fe93831e3fb400ce87' }], // 广告相关，你可以去掉
		// ['script', { src: 'https://cdn.wwads.cn/js/makemoney.js', type: 'text/javascript' }], // 广告相关，你可以去掉
	],

	// 主题配置
	themeConfig: {
		nav: [
			{text: '首页', link: '/'},
			{
				text: '指南', link: '/pages/a2f161/', items: [
					{
						text: '开发手册', items: [
							{ text: '组件【ai】', link: '/pages/zj00/'},
							{ text: '组件【algorithm】', link: '/pages/zj01/'},
							{ text: '组件【banner】', link: '/pages/zj02/'},
							{ text: '组件【clickhouse】', link: '/pages/zj03/'},
							{ text: '组件【core】', link: '/pages/zj04/'},
							{ text: '组件【bom】', link: '/pages/zj05/'},
							{ text: '组件【cors】', link: '/pages/zj06/'},
							{ text: '组件【domain】', link: '/pages/zj07/'},
							{ text: '组件【crypto】', link: '/pages/zj08/'},
							{ text: '组件【data-cache】', link: '/pages/zj09/'},
							{ text: '组件【dubbo】', link: '/pages/zj10/'},
							{ text: '组件【elasticsearch】', link: '/pages/zj11/'},
							{ text: '组件【excel】', link: '/pages/zj12/'},
							{ text: '组件【extension】', link: '/pages/zj13/'},
							{ text: '组件【flink】', link: '/pages/zj14/'},
							{ text: '组件【grpc】', link: '/pages/zj15/'},
							{ text: '组件【i18n】', link: '/pages/zj16/'},
							{ text: '组件【idempotent】', link: '/pages/zj17/'},
							{ text: '组件【influxdb】', link: '/pages/zj18/'},
							{ text: '组件【kafka】', link: '/pages/zj19/'},
							{ text: '组件【log】', link: '/pages/zj20/'},
							{ text: '组件【lock】', link: '/pages/zj21/'},
							{ text: '组件【mail】', link: '/pages/zj22/'},
							{ text: '组件【log4j2】', link: '/pages/zj23/'},
							{ text: '组件【mqtt】', link: '/pages/zj24/'},
							{ text: '组件【mongodb】', link: '/pages/zj25/'},
							{ text: '组件【mybatis-plus】', link: '/pages/zj26/'},
							{ text: '组件【nacos】', link: '/pages/zj27/'},
							{ text: '组件【netty】', link: '/pages/zj28/'},
							{ text: '组件【openapi-doc】', link: '/pages/zj29/'},
							{ text: '组件【openfeign】', link: '/pages/zj30/'},
							{ text: '组件【oss】', link: '/pages/zj31/'},
							{ text: '组件【prometheus】', link: '/pages/zj32/'},
							{ text: '组件【rabbitmq】', link: '/pages/zj33/'},
							{ text: '组件【rate-limiter】', link: '/pages/zj34/'},
							{ text: '组件【reactor】', link: '/pages/zj35/'},
							{ text: '组件【redis】', link: '/pages/zj36/'},
							{ text: '组件【rocketmq】', link: '/pages/zj37/'},
							{ text: '组件【ruleengine】', link: '/pages/zj38/'},
							{ text: '组件【secret】', link: '/pages/zj39/'},
							{ text: '组件【security】', link: '/pages/zj40/'},
							{ text: '组件【sensitive】', link: '/pages/zj41/'},
							{ text: '组件【sentinel】', link: '/pages/zj42/'},
							{ text: '组件【sms】', link: '/pages/zj43/'},
							{ text: '组件【snail-job】', link: '/pages/zj44/'},
							{ text: '组件【spark】', link: '/pages/zj45/'},
							{ text: '组件【starrocks】', link: '/pages/zj46/'},
							{ text: '组件【statemachine】', link: '/pages/zj47/'},
							{ text: '组件【storage】', link: '/pages/zj48/'},
							{ text: '组件【tdengine】', link: '/pages/zj49/'},
							{ text: '组件【tenant】', link: '/pages/zj50/'},
							{ text: '组件【test】', link: '/pages/zj51/'},
							{ text: '组件【trace】', link: '/pages/zj52/'},
							{ text: '组件【xss】', link: '/pages/zj53/'},
							{ text: '组件【shardingsphere】', link: '/pages/zj54/'},
						]
					},
					{
						text: '环境搭建', items: [
							{text: 'Centos7安装Mysql 8.0.33', link: '/pages/a2f161/'},
							{text: 'Centos7安装Redis 7.0.11', link: '/pages/90401a/'},
							{text: 'Centos7安装RocketMQ 5.1.1', link: '/pages/0fb88c/'},
							{text: 'Centos7安装Jdk 17.0.7', link: '/pages/65acfd/'},
							{text: 'Centos7安装Docker 23.0.6', link: '/pages/65acff/'},
							{text: 'Centos7安装Elasticsearch 8.6.2', link: '/pages/d715cf/'},
							{text: 'Docker安装RabbitMQ 3.12.2', link: '/pages/552b64/'},
							{text: 'Docker安装Postgresql 16.1', link: '/pages/d715cb/'}
						]
					},
					{
						text: '常用命令', items: [
							{text: 'Centos7常用命令', link: '/pages/76bfa2/'},
							{text: 'Centos7常用命令', link: '/pages/2f475f/'}
						]
					},
					{
						text: '快速上手', link: '/pages/10bfa7/', items: [
							{text: '项目启动【dev环境】', link: '/pages/10bfa7/'},
							{text: '项目启动【test环境】', link: '/pages/9fade8/'},
							{text: '项目启动【prod环境】', link: '/pages/7be29e/'},
						]
					},
					{
						text: '前端指南', link: '/pages/a5d759/', items: [
							{text: '前端启动', link: '/pages/a5d759/'},
						]
					},
					{
						text: '后端指南', link: '/pages/59afe2/', items: [
							{text: 'COLA代码规范', link: '/pages/59afe2/'},
							{text: 'SSL证书', link: '/pages/10bfa8/'},
							{text: '一键修改项目模块', link: '/pages/10bfa9/'},
							{text: '一键生成项目骨架', link: '/pages/1e1e32/'},
							{text: '一键修改项目版本号', link: '/pages/77f103/'},
							{text: '一键跳过测试用例', link: '/pages/843853/'},
							{text: '一键生成后端COLA代码', link: '/pages/889fdb/'},
							{text: '分布式链路跟踪之ELK日志', link: '/pages/fe2754/'},
							{text: '一键检查代码规范', link: '/pages/cf6984/'},
							{text: '动态路由', link: '/pages/4bce44/'}
						]
					},
					{
						text: '项目部署', link: '/pages/61389d/', items: [
							{text: '项目部署之镜像打包与推送', link: '/pages/61389d/'}
						]
					},
					{
						text: '其他', link: '/pages/643da2/', items: [
							{text: 'Java如何快速转Go', link: '/pages/643da2/'},
							{text: 'Go快速开发API', link: '/pages/b4322a/'},
							{text: 'Vue快速开发Api', link: '/pages/52f121/'},
							{text: 'React快速开发Api', link: '/pages/3de3af/'},
						]
					},
				]
			},
			{
				text: '摘抄',link: '/pages/85233a/', items: [
					{
						text: '儒学', link: '', items: [
							{text: '儒学摘抄（一）', link: '/pages/85233a/'}
						]
					},
					{
						text: '禅语',link: '/pages/59b0b4/', items: [
							{text: '禅语摘抄（一）', link: '/pages/59b0b4/'},
						]
					},
					{
						text: '诗词', link: '/pages/f8adf5/', items: [
							{text: '诗词摘抄（一）', link: '/pages/f8adf5/'}
						]
					},
                    {
                        text: '道法', link: '/pages/e03540/', items: [
                            {text: '道法摘抄（一）', link: '/pages/e03540/'}
                        ]
                    },
					{
						text: '养生', link: '/pages/e03541/', items: [
							{text: '养生摘抄（一）', link: '/pages/e03541/'}
						]
					}
				]
			},
			{
				text: '感悟', link: '/pages/623577/', items: [
					{
						text: '读后感', items: [
							{text: '读《强者，都是含泪奔跑的人》读后感', link: '/pages/623577/'}
						]
					},
					{
						text: '修行', items: [
							{text: '修身/养生/情感', link: '/pages/623578/'}
						]
					},
					{
						text: '觉悟', items: [
							{text: '觉悟日记（一）', link: '/pages/b5ee4c/'}
						]
					}
				]
			},
			{text: '赞助', link: '/pages/1b12ed/'},
			{text: '项目课程', link: 'https://koushenhai.github.io/KCloud-Platform-IoT'}
		],
		sidebarDepth: 2, // 侧边栏显示深度，默认1，最大2（显示到h3标题）
		logo: '/img/logo.png', // 导航栏logo
		repo: 'KouShenhai/KCloud-Platform-IoT', // 导航栏右侧生成Github链接
		searchMaxSuggestions: 10, // 搜索结果显示最大数
		lastUpdated: '上次更新', // 更新的时间，及前缀文字   string | boolean (取值为git提交时间)

		// docsDir: 'docs', // 编辑的文件夹
		// editLinks: true, // 编辑链接
		// editLinkText: '编辑',

		// 以下配置是Vdoing主题改动的和新增的配置
		sidebar: {mode: 'structuring', collapsable: false}, // 侧边栏  'structuring' | { mode: 'structuring', collapsable: Boolean} | 'auto' | 自定义    温馨提示：目录页数据依赖于结构化的侧边栏数据，如果你不设置为'structuring',将无法使用目录页

		// sidebarOpen: false, // 初始状态是否打开侧边栏，默认true
		updateBar: { // 最近更新栏
			showToArticle: false, // 显示到文章页底部，默认true
			// moreArticle: '/archives' // “更多文章”跳转的页面，默认'/archives'
		},
		// titleBadge: false, // 文章标题前的图标是否显示，默认true
		// titleBadgeIcons: [ // 文章标题前图标的地址，默认主题内置图标
		//   '图标地址1',
		//   '图标地址2'
		// ],

		pageStyle: 'line', // 页面风格，可选值：'card'卡片 | 'line' 线（未设置bodyBgImg时才生效）， 默认'card'。 说明：card时背景显示灰色衬托出卡片样式，line时背景显示纯色，并且部分模块带线条边框

		// contentBgStyle: 1,

		category: false, // 是否打开分类功能，默认true。 如打开，会做的事情有：1. 自动生成的frontmatter包含分类字段 2.页面中显示与分类相关的信息和模块 3.自动生成分类页面（在@pages文件夹）。如关闭，则反之。
		tag: false, // 是否打开标签功能，默认true。 如打开，会做的事情有：1. 自动生成的frontmatter包含标签字段 2.页面中显示与标签相关的信息和模块 3.自动生成标签页面（在@pages文件夹）。如关闭，则反之。
		// archive: false, // 是否打开归档功能，默认true。 如打开，会做的事情有：1.自动生成归档页面（在@pages文件夹）。如关闭，则反之。

		author: { // 文章默认的作者信息，可在md文件中单独配置此信息 String | {name: String, href: String}
			name: 'KCloud-Platform-IoT', // 必需
			href: 'https://github.com/KouShenhai' // 可选的
		},
		social: { // 社交图标，显示于博主信息栏和页脚栏
			// iconfontCssFile: '//at.alicdn.com/t/font_1678482_u4nrnp8xp6g.css', // 可选，阿里图标库在线css文件地址，对于主题没有的图标可自由添加
			icons: [
				{
					iconClass: 'icon-youjian',
					title: '发邮件',
					link: 'mailto:2413176044@qq.com'
				},
				{
					iconClass: 'icon-github',
					title: 'GitHub',
					link: 'https://github.com/KouShenhai'
				}
			]
		},
		footer: { // 页脚信息
			createYear: 2022, // 博客创建年份
			copyrightInfo: 'laokou | Apache 2.0 License', // 博客版权信息，支持a标签
		},
		htmlModules,
	},

	// 插件
	plugins: [
		// [require('./plugins/love-me'), { // 鼠标点击爱心特效
		//   color: '#11a8cd', // 爱心颜色，默认随机色
		//   excludeClassName: 'theme-vdoing-content' // 要排除元素的class, 默认空''
		// }],

		['fulltext-search'], // 全文搜索

		// ['thirdparty-search', { // 可以添加第三方搜索链接的搜索框（原官方搜索框的参数仍可用）
		//   thirdparty: [ // 可选，默认 []
		//     {
		//       title: '在GitHub中搜索',
		//       frontUrl: 'https://github.com/search?q=', // 搜索链接的前面部分
		//       behindUrl: '' // 搜索链接的后面部分，可选，默认 ''
		//     },
		//     {
		//       title: '在npm中搜索',
		//       frontUrl: 'https://www.npmjs.com/search?q=',
		//     },
		//     {
		//       title: '在Bing中搜索',
		//       frontUrl: 'https://cn.bing.com/search?q='
		//     }
		//   ]
		// }],

		[
			'vuepress-plugin-baidu-tongji', // 百度统计
			{
				hm: baiduCode || '01293bffa6c3962016c08ba685c79d78'
			}
		],

		['one-click-copy', { // 代码块复制按钮
			copySelector: ['div[class*="language-"] pre', 'div[class*="aside-code"] aside'], // String or Array
			copyMessage: '复制成功', // default is 'Copy successfully and then paste it for use.'
			duration: 1000, // prompt message display time.
			showInMobile: false // whether to display on the mobile side, default: false.
		}],
		['demo-block', { // demo演示模块 https://github.com/xiguaxigua/vuepress-plugin-demo-block
			settings: {
				// jsLib: ['http://xxx'], // 在线示例(jsfiddle, codepen)中的js依赖
				// cssLib: ['http://xxx'], // 在线示例中的css依赖
				// vue: 'https://fastly.jsdelivr.net/npm/vue/dist/vue.min.js', // 在线示例中的vue依赖
				jsfiddle: false, // 是否显示 jsfiddle 链接
				codepen: true, // 是否显示 codepen 链接
				horizontal: false // 是否展示为横向样式
			}
		}],
		[
			'vuepress-plugin-zooming', // 放大图片
			{
				selector: '.theme-vdoing-content img:not(.no-zoom)',
				options: {
					bgColor: 'rgba(0,0,0,0.6)'
				},
			},
		],
		[
			'@vuepress/last-updated' // "上次更新"时间格式
		]
	],

	markdown: {
		// lineNumbers: true,
		extractHeaders: ['h2', 'h3', 'h4', 'h5', 'h6'], // 提取标题到侧边栏的级别，默认['h2', 'h3']
	},

	// 监听文件变化并重新构建
	extraWatchFiles: [
		'.vuepress/config.js',
		'.vuepress/config/htmlModules.js',
	]
}
