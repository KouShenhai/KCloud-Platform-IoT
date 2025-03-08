declare namespace API {
	type NoticeLogCO = {
		extValues?: Record<string, any>;
		id?: number;
		code?: string;
		name?: string;
		status?: number;
		errorMessage?: string;
		param?: string;
	};

	type NoticeLogExportCmd = true;

	type NoticeLogModifyCmd = {
		co?: NoticeLogCO;
	};

	type NoticeLogPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type NoticeLogSaveCmd = {
		co?: NoticeLogCO;
	};

	type DeptCO = {
		extValues?: Record<string, any>;
		id?: number;
		pid?: number;
		name?: string;
		path?: string;
		sort?: number;
	};

	type DeptExportCmd = true;

	type DeptModifyCmd = {
		co?: DeptCO;
	};

	type DeptPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type DeptSaveCmd = {
		co?: DeptCO;
	};

	type DictItemCO = {
		extValues?: Record<string, any>;
		id?: number;
		label?: string;
		value?: string;
		sort?: number;
		remark?: string;
		status?: number;
		typeId?: number;
	};

	type DictItemExportCmd = true;

	type DictItemModifyCmd = {
		co?: DictItemCO;
	};

	type DictItemPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type DictItemSaveCmd = {
		co?: DictItemCO;
	};

	type DictCO = {
		extValues?: Record<string, any>;
		id?: number;
		name?: string;
		type?: string;
		remark?: string;
		status?: number;
	};

	type DictExportCmd = true;

	type DictModifyCmd = {
		co?: DictCO;
	};

	type DictPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type DictSaveCmd = {
		co?: DictCO;
	};

	type DomainEventCO = {
		extValues?: Record<string, any>;
		id?: number;
		aggregateId?: number;
		eventType?: string;
		topic?: string;
		sourceName?: string;
		attribute?: string;
		serviceId?: string;
		tag?: string;
	};

	type DomainEventExportCmd = true;

	type DomainEventModifyCmd = {
		co?: DomainEventCO;
	};

	type DomainEventPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type DomainEventSaveCmd = {
		co?: DomainEventCO;
	};

	type getByIdV3Params = {
		id: number;
	};

	type I18nMessageCO = {
		extValues?: Record<string, any>;
		id?: number;
		code?: string;
		zhMessage?: string;
		enMessage?: string;
	};

	type I18nMessageExportCmd = true;

	type I18nMessageModifyCmd = {
		co?: I18nMessageCO;
	};

	type I18nMessagePageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type I18nMessageSaveCmd = {
		co?: I18nMessageCO;
	};

	type IpCO = {
		extValues?: Record<string, any>;
		id?: number;
		label?: string;
		value?: string;
	};

	type IpExportCmd = true;

	type IpModifyCmd = {
		co?: IpCO;
	};

	type IpPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type IpSaveCmd = {
		co?: IpCO;
	};

	type LoginLogCO = {
		extValues?: Record<string, any>;
		id?: number;
		username?: string;
		ip?: string;
		address?: string;
		browser?: string;
		os?: string;
		status?: number;
		errorMessage?: string;
		type?: string;
	};

	type LoginLogExportCmd = true;

	type LoginLogModifyCmd = {
		co?: LoginLogCO;
	};

	type LoginLogPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type LoginLogSaveCmd = {
		co?: LoginLogCO;
	};

	type MenuCO = {
		extValues?: Record<string, any>;
		id?: number;
		pid?: number;
		permission?: string;
		type?: number;
		name?: string;
		path?: string;
		icon?: string;
		sort?: number;
		hidden?: number;
		status?: number;
		url?: string;
	};

	type MenuExportCmd = true;

	type MenuModifyCmd = {
		co?: MenuCO;
	};

	type MenuPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type MenuSaveCmd = {
		co?: MenuCO;
	};

	type OperateLogCO = {
		extValues?: Record<string, any>;
		id?: number;
		name?: string;
		moduleName?: string;
		uri?: string;
		methodName?: string;
		requestType?: string;
		requestParams?: string;
		userAgent?: string;
		ip?: string;
		address?: string;
		status?: number;
		operator?: string;
		errorMessage?: string;
        costTime?: number;
	};

	type OperateLogExportCmd = true;

	type OperateLogModifyCmd = {
		co?: OperateLogCO;
	};

	type OperateLogPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type OperateLogSaveCmd = {
		co?: OperateLogCO;
	};

	type OssCO = {
		extValues?: Record<string, any>;
		id?: number;
		name?: string;
		endpoint?: string;
		region?: string;
		accessKey?: string;
		secretKey?: string;
		bucketName?: string;
		pathStyleAccessEnabled?: number;
	};

	type OssExportCmd = true;

	type OssLogCO = {
		extValues?: Record<string, any>;
		id?: number;
		name?: string;
		md5?: string;
		url?: string;
		size?: number;
		status?: number;
		errorMessage?: string;
	};

	type OssLogExportCmd = true;

	type OssLogModifyCmd = {
		co?: OssLogCO;
	};

	type OssLogPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type OssLogSaveCmd = {
		co?: OssLogCO;
	};

	type OssModifyCmd = {
		co?: OssCO;
	};

	type OssPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type OssSaveCmd = {
		co?: OssCO;
	};

	// @ts-ignore
    type Result = {
		/** 状态标识 */
		code?: string;
		/** 响应描述 */
		msg?: string;
		/** 响应结果 */
		data?: any;
		/** 链路ID */
		traceId?: string;
		/** 标签ID */
		spanId?: string;
	};

	type RoleCO = {
		extValues?: Record<string, any>;
		id?: number;
		name?: string;
		sort?: number;
		dataScope?: string;
	};

	type RoleExportCmd = true;

	type RoleModifyCmd = {
		co?: RoleCO;
	};

	type RoleModifyAuthorityCmd = {
		co?: RoleCO;
	};

	type RolePageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type RoleSaveCmd = {
		co?: RoleCO;
	};

	type SourceCO = {
		extValues?: Record<string, any>;
		id?: number;
		name?: string;
		driverClassName?: string;
		url?: string;
		username?: string;
		password?: string;
	};

	type SourceExportCmd = true;

	type SourceModifyCmd = {
		co?: SourceCO;
	};

	type SourcePageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type SourceSaveCmd = {
		co?: SourceCO;
	};

	type TenantCO = {
		extValues?: Record<string, any>;
		id?: number;
		name?: string;
		label?: string;
		sourceId?: number;
		packageId?: number;
	};

	type TenantExportCmd = true;

	type TenantModifyCmd = {
		co?: TenantCO;
	};

	type TenantPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type TenantSaveCmd = {
		co?: TenantCO;
	};

	type UserCO = {
		extValues?: Record<string, any>;
		id?: number;
		password?: string;
		superAdmin?: number;
		mail?: string;
		mobile?: string;
		status?: number;
		avatar?: string;
		username?: string;
	};

	type UserExportCmd = true;

	type UserModifyCmd = {
		co?: UserCO;
	};

	type UserModifyAuthorityCmd = {
		co?: UserCO;
	};

	type ResetPwdCmd = {
		id?: number;
		password?: string;
	};

	type UserPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
	};

	type UserSaveCmd = {
		co?: UserCO;
	};
}
