declare namespace API {
  type RoutersMenuItem = {
    alwaysShow?: boolean;
    children?: RoutersMenuItem[];
    component?: string;
    hidden?: boolean;
    meta: MenuItemMeta;
    name: string;
    path: string;
    redirect?: string;
    [key: string]: any;
  };
  interface GetRoutersResult {
    code: number;
    msg: string;
    data: RoutersMenuItem[];
  }
}
