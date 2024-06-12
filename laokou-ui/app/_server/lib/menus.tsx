import * as icons from '@ant-design/icons'

// type MenuItem = Required<MenuProps>['items'][number];

interface MenuItem  {
    label: React.ReactNode,
    key: React.Key,
    icon?: React.ReactNode,
    children?: MenuItem[],
    type?:React.ReactNode,
    path?:React.ReactNode,
    url?:React.ReactNode,
    parent?:React.ReactNode,
  }

const rootPath:string = '/home';

const menusList: MenuItem[] = [
    {
        label: '首页',
        key: 1,
        path:rootPath,
        icon: <icons.HomeOutlined />,
        parent:rootPath,
        // type:'group',
        // children:[],
    },
    {
        label: '系统管理',
        key: 2,
        path:rootPath,
        parent:rootPath,
        icon: <icons.SettingFilled />,
        // type:'group',
        children: [
            {
                label: '菜单管理',
                key: 3,
                path: '/menus',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '部门管理',
                key: 4,
                path:'/depts',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '角色管理',
                key: 5,
                path: '/roles',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '用户管理',
                key: 6,
                path: '/users',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '租户管理',
                key: 7,
                path: '/tenants',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '数据源管理',
                key: 8,
                path:'/dbSources',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '套餐管理',
                key: 9,
                path: '/setMeal',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '存储管理',
                key:10,
                path: '/oss',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '字典管理',
                key: 11,
                path:'/dicts',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '消息管理',
                key: 12,
                path: '/messages',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '索引管理',
                key: 13,
                path: '/indexs',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // type:'group',
                children:[
                    {
                        label: '索引概览',
                        key: 14,
                        path:'/indexs/overview',
                        parent:rootPath,
                        icon: <icons.AppstoreOutlined />,
                        // children:[],
        
                    },
                    {
                        label: '分布式链路索引',
                        key: 15,
                        path:'/indexs/trace',
                        parent:rootPath,
                        icon: <icons.AppstoreOutlined />,
                        // children:[],
        
                    }
                ],

            },
            {
                label: '资源管理',
                key: 16,
                path:'/resource',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // type:'group',
                children:[
                    {
                        label: '资源天堂',
                        key: 17,
                        path: '/resource/heaven',
                        parent:rootPath,
                        icon: <icons.AppstoreOutlined />,
                        // children:[],
        
                    },
                    {
                        label: '资源搜索',
                        key:18,
                        path: '/resource/search',
                        parent:rootPath,
                        icon: <icons.AppstoreOutlined />,
                        // children:[],
        
                    },
                    {
                        label: '资源流程',
                        key: 19,
                        path: '/resource/task',
                        parent:rootPath,
                        icon: <icons.AppstoreOutlined />,
                        // children:[],
        
                    }
                ],

            },
            {
                label: 'IP管理',
                key: 20,
                path: '/ip',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // type:'group',
                children:[
                    {
                        label: '白名单',
                        key: 21,
                        path:'/ip/white',
                        parent:rootPath,
                        icon: <icons.AppstoreOutlined />,
                        // children:[],
        
                    },
                    {
                        label: '黑名单',
                        key:22,
                        path:'/ip/black',
                        parent:rootPath,
                        icon: <icons.AppstoreOutlined />,
                        // children:[],
        
                    }
                ],

            },
            {
                label: '集群管理',
                key: 23,
                path: '/clusters',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '日志管理',
                key: 24,
                path: '/logs',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // type:'group',
                children:[
                    {
                        label: '操作日志',
                        key: 25,
                        path: '/logs/operate',
                        parent:rootPath,
                        icon: <icons.AppstoreOutlined />,
                        // children:[],
        
                    },
                    {
                        label: '登录日志',
                        key: 26,
                        path: '/logs/login',
                        parent:rootPath,
                        icon: <icons.AppstoreOutlined />,
                        // children:[],
        
                    },

                ],

            },
            {
                label: '配置中心',
                key: 28,
                // path: '',
                parent:rootPath,
                url:'http://127.0.0.1:8080',
                icon: <icons.SettingTwoTone />,
                // children:[],

            },
            {
                label: '事务管理',
                key: 29,
                // path: '/transactional',
                url:'http://127.0.0.1:8080',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '接口文档',
                key: 30,
                // path: '/word',
                url:'http://127.0.0.1:8080',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            },
            {
                label: '任务调度',
                key: 31,
                // path: '/taskscheduling',
                url:'http://127.0.0.1:8080',
                parent:rootPath,
                icon: <icons.AppstoreOutlined />,
                // children:[],

            }
        ],
    },
    {
        label: '系统监控',
        key: 32,
        path: '/monitor',
        parent:rootPath,
        icon: <icons.FundProjectionScreenOutlined />,
        // type:'group',
        children:[
            {
                label: '在线用户',
                key: 33,
                path: '/online',
                parent:rootPath,
                icon: <icons.FundProjectionScreenOutlined />,
                // children:[],
        
            },
            {
                label: '流量监控',
                key: 34,
                // path: '/flow',
                url:'http://127.0.0.1:8080',
                parent:rootPath,
                icon: <icons.FundProjectionScreenOutlined />,
                // children:[],
        
            },
            {
                label: '服务监控',
                key: 35,
                path: '/service',
                url:'http://127.0.0.1:8080',
                parent:rootPath,
                icon: <icons.FundProjectionScreenOutlined />,
                // children:[],
        
            },
            {
                label: '缓存监控',
                key: 36,
                path: '/cache',
                parent:rootPath,
                icon: <icons.FundProjectionScreenOutlined />,
                // children:[],
        
            },
            {
                label: '主机监控',
                key: 37,
                path: '/server',
                url:'http://127.0.0.1:8080',
                parent:rootPath,
                icon: <icons.FundProjectionScreenOutlined />,
                // children:[],
        
            },
        ],

    },
    {
        label: '工作流程',
        key: 38,
        path: '/workFlow',
        parent:rootPath,
        icon: <icons.PartitionOutlined />,
        // type:'group',
        children:[
            {
                label: '流程定义',
                key: 39,
                path: '/definitions',
                parent:rootPath,
                icon: <icons.PartitionOutlined />,
                // children:[],
        
            }
        ],

    },
    {
        label: '设备管理（Iot还在建设中）',
        key: 40,
        path: '/equipment',
        parent:rootPath,
        icon: <icons.AndroidOutlined />,
        // children:[],

    }

];

export default menusList
