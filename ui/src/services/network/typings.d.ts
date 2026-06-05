declare namespace API {
	type ConnectionCO = {
		extValues?: Record<string, any>;
		/** ID */
		id?: number;
		/** Connection name */
		name?: string;
		/** 1 MQTT Server, 2 HTTP Server, 3 MQTT Client, 4 Kafka, 5 RabbitMQ */
		type?: number;
		/** Host */
		host?: string;
		/** Port */
		port?: number;
		/** 0 enabled, 1 disabled */
		enabled?: number;
		/** Type-specific JSON config */
		config?: string;
		/** Remark */
		remark?: string;
		/** Created time */
		createTime?: string;
	};

	type ConnectionGetByIdParams = {
		id: number;
	};

	type ConnectionModifyCmd = {
		co?: ConnectionCO;
	};

	type ConnectionPageQry = {
		pageNum?: number;
		pageSize?: number;
		pageIndex?: number;
		sqlFilter?: string;
		params?: Record<string, any>;
		name?: string;
		type?: number;
		enabled?: number;
	};

	type ConnectionRemoveCmd = {
		ids?: number[];
	};

	type ConnectionSaveCmd = {
		co?: ConnectionCO;
	};
}
