declare namespace API {
  type ProductCO = {
    extValues?: Record<string, any>;
    /** ID */
    Id?: number;
    /** 产品名称 */
    name?: string;
    /** 产品类别 */
    categoryId?: number;
    /** 设备类型 1直连设备 2网关设备 3监控设备 */
	deviceType?: number;
    /** 产品图片URL */
    imgUrl?: string;
    /** 通讯协议ID */
    cpId?: number;
    /** 传输协议ID */
    tpId?: number;
    /** 备注 */
    remark?: string;
    /** 创建时间 */
    createTime?: string;
  };

  type ProductCategoryCO = {
    extValues?: Record<string, any>;
    /** ID */
    Id?: number;
    /** 产品类别名称 */
    name?: string;
    /** 排序 */
    sort?: number;
    /** 产品类别父节点ID */
    pid?: number;
    /** 备注 */
    remark?: string;
    /** 创建时间 */
    creatTime?: string;
  };

  type TransportProtocolCO = {
    extValues?: Record<string, any>;
    /** ID */
    Id?: number;
    /** 协议名称 */
    name?: string;
    /** 协议类型 */
	type?: string;
    /** 主机 */
    host?: string;
    /** 端口 */
    port?: string;
    /** 客户端ID */
    clientId?: string;
    /** 主题 */
    topic?: string;
    /** 用户名 */
    username?: string;
    /** 密码 */
    password?: string;
    /** 备注 */
    remark?: string;
    /** 创建时间 */
    createTime?: string;
  };

  type CommunicationProtocolExportCmd = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    name?: string;
    code?: string;
    sort?: number;
    remark?: string;
  };

  type CommunicationProtocolModifyCmd = {
    co?: CommunicationProtocolCO;
  };

  type CommunicationProtocolPageQry = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    name?: string;
    code?: string;
    sort?: number;
    remark?: string;
  };

  type CommunicationProtocolSaveCmd = {
    co?: CommunicationProtocolCO;
  };

  type DeviceExportCmd = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    sn?: string;
    name?: string;
    status?: number;
    longitude?: number;
    latitude?: number;
    imgUrl?: string;
    address?: string;
    remark?: string;
    productId?: number;
  };

  type DeviceModifyCmd = {
    co?: DeviceCO;
  };

  type DevicePageQry = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    sn?: string;
    name?: string;
    status?: number;
    longitude?: number;
    latitude?: number;
    imgUrl?: string;
    address?: string;
    remark?: string;
    productId?: number;
  };

  type DeviceSaveCmd = {
    co?: DeviceCO;
  };

  type getByIdV3Params = {
    id: number;
  };

  type getByIdV3Params = {
    id: number;
  };

  type getByIdV3Params = {
    id: number;
  };

  type getByIdV3Params = {
    id: number;
  };

  type getByIdV3Params = {
    id: number;
  };

  type getByIdV3Params = {
    id: number;
  };

  type getByIdV3Params = {
    id: number;
  };

  type ThingModelCO = {
    extValues?: Record<string, any>;
    /** ID */
    Id?: number;
    /** 模型名称 */
    name?: string;
    /** 模型编码 */
    code?: string;
    /** 数据类型 integer string decimal boolean */
    dataType?: string;
    /** 模型类别 1属性 2事件 */
    category?: number;
    /** 模型类型 read读 write写 report上报 */
    type?: string;
    /** 表达式 */
    expression?: string;
    /** 排序 */
    sort?: number;
    /** 规则说明 */
    specs?: string;
    /** 备注 */
    remark?: string;
    /** 创建时间 */
    createTime?: string;
  };

  type ProductCategoryExportCmd = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    name?: string;
    sort?: number;
    pid?: number;
    remark?: string;
  };

  type ProductCategoryModifyCmd = {
    co?: ProductCategoryCO;
  };

  type ProductCategoryPageQry = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    name?: string;
    sort?: number;
    pid?: number;
    remark?: string;
  };

  type ProductCategorySaveCmd = {
    co?: ProductCategoryCO;
  };

  type ProductExportCmd = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    name?: string;
    categoryId?: number;
    deviceType?: number;
    imgUrl?: string;
    cpId?: number;
    tpId?: number;
    remark?: string;
  };

  type ProductModifyCmd = {
    co?: ProductCO;
  };

  type ProductPageQry = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    name?: string;
    categoryId?: number;
    deviceType?: number;
    imgUrl?: string;
    cpId?: number;
    tpId?: number;
    remark?: string;
  };

  type ProductSaveCmd = {
    co?: ProductCO;
  };

	type Result = {
		/** 状态编码 */
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

  type DeviceCO = {
    extValues?: Record<string, any>;
    /** ID */
    Id?: number;
    /** 设备序列号 */
    sn?: string;
    /** 设备名称 */
    name?: string;
    /** 设备状态 0在线 1离线 */
    status?: number;
    /** 设备经度 */
    longitude?: number;
    /** 设备纬度 */
    latitude?: number;
    /** 设备图片URL */
    imgUrl?: string;
    /** 设备地址 */
    address?: string;
    /** 设备备注 */
    remark?: string;
    /** 产品ID */
    productId?: number;
    /** 创建时间 */
    createTime?: string;
  };

  type ThingModelModifyCmd = {
    co?: ThingModelCO;
  };

  type ThingModelPageQry = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    name?: string;
    code?: string;
    dataType?: string;
    category?: number;
    type?: string;
    expression?: string;
    sort?: number;
    specs?: string;
    remark?: string;
  };

  type ThingModelSaveCmd = {
    co?: ThingModelCO;
  };

  type ThingThingModelExportCmd = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    name?: string;
    code?: string;
    dataType?: string;
    category?: number;
    type?: string;
    expression?: string;
    sort?: number;
    specs?: string;
    remark?: string;
  };

  type CommunicationProtocolCO = {
    extValues?: Record<string, any>;
    /** ID */
    Id?: number;
    /** 协议名称 */
    name?: string;
    /** 协议编码 */
    code?: string;
    /** 排序 */
    sort?: number;
    /** 备注 */
    remark?: string;
    /** 创建时间 */
    createTime?: string;
  };

  type TransportProtocolExportCmd = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    name?: string;
    type?: string;
    host?: string;
    port?: string;
    clientId?: string;
    topic?: string;
    username?: string;
    password?: string;
    remark?: string;
  };

  type TransportProtocolModifyCmd = {
    co?: TransportProtocolCO;
  };

  type TransportProtocolPageQry = {
    pageNum?: number;
    pageSize?: number;
    pageIndex?: number;
    sqlFilter?: string;
    params?: Record<string, any>;
    name?: string;
    type?: string;
    host?: string;
    port?: string;
    clientId?: string;
    topic?: string;
    username?: string;
    password?: string;
    remark?: string;
  };

  type TransportProtocolSaveCmd = {
    co?: TransportProtocolCO;
  };
}
