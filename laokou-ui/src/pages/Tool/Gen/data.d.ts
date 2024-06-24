export type GenCodeType = {
  columnId: string;
  businessName: string;
  className: string;
  createBy: string;
  createTime: string;
  crud: string;
  functionAuthor: string;
  functionName: string;
  genPath: string;
  genType: string;
  moduleName: string;
  options: string;
  packageName: string;
  params: string;
  parentMenuId: string;
  parentMenuName: string;
  remark: string;
  status: string;
  isList: string;
  isEdit: string;
  isQuery: string;
  isInsert: string;
  isRequired: string;
  tableComment: string;
  tableId: string;
  tableName: string;
  tplCategory: string;
  tree: string;
  treeCode: number;
  treeName: string;
  treeParentCode: number;
  updateBy: string;
  updateTime: string;
  columns: any;
};

export type GenCodePagination = {
  total: number;
  pageSize: number;
  current: number;
};

export type GenCodeListData = {
  list: GenCodeType[];
  pagination: Partial<GenCodePagination>;
};

export type GenCodeTableListParams = {
  businessName?: string;
  className?: string;
  createBy?: string;
  createTime?: string;
  crud?: string;
  functionAuthor?: string;
  functionName?: string;
  genPath?: string;
  genType?: string;
  moduleName?: string;
  isList?: string;
  isEdit?: string;
  isQuery?: string;
  isInsert?: string;
  isRequired?: string;
  options?: string;
  params?: any;
  packageName?: string;
  parentMenuId?: string;
  columns?: any;
  parentMenuName?: string;
  remark?: string;
  tableComment?: string;
  tableId?: string;
  tableName?: string;
  tplCategory?: string;
  tree?: string;
  treeCode?: string;
  treeName?: string;
  treeParentCode?: string;
  updateBy?: string;
  updateTime?: string;
  pageSize?: string;
  currentPage?: string;
  filter?: string;
  sorter?: string;
};

export type TableColumnInfo = {
  columnName: string;
  columnComment: string;
};

export type TableInfo = {
  tableName: string;
  tableComment: string;
  columns: TableColumnInfo[];
};

export type ImportTableListItem = {
  tableName: string;
  tableComment: string;
};

export type ImportTableParams = {
  tables: string;
};
