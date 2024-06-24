import { DataNode } from 'antd/es/tree';
import { parse } from 'querystring';

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function buildTreeData(
  data: any[],
  id: string,
  name: string,
  parentId: string,
  parentName: string,
  children: string,
) {
  const config = {
    id: id || 'id',
    name: name || 'name',
    parentId: parentId || 'parentId',
    parentName: parentName || 'parentName',
    childrenList: children || 'children',
  };

  const childrenListMap: any[] = [];
  const nodeIds: any[] = [];
  const tree: any[] = [];
  data.forEach((item) => {
    const d = item;
    const pId = d[config.parentId];
    if (!childrenListMap[pId]) {
      childrenListMap[pId] = [];
    }
    d.key = d[config.id];
    d.title = d[config.name];
    d.value = d[config.id];
    d[config.childrenList] = null;
    nodeIds[d[config.id]] = d;
    childrenListMap[pId].push(d);
  });

  data.forEach((item: any) => {
    const d = item;
    const pId = d[config.parentId];
    if (!nodeIds[pId]) {
      d[config.parentName] = '';
      tree.push(d);
    }
  });

  function adaptToChildrenList(item: any) {
    const o = item;
    if (childrenListMap[o[config.id]]) {
      if (!o[config.childrenList]) {
        o[config.childrenList] = [];
      }
      o[config.childrenList] = childrenListMap[o[config.id]];
    }
    if (o[config.childrenList]) {
      o[config.childrenList].forEach((child: any) => {
        const c = child;
        c[config.parentName] = o[config.name];
        adaptToChildrenList(c);
      });
    }
  }

  tree.forEach((t: any) => {
    adaptToChildrenList(t);
  });

  return tree;
}

export const getPageQuery = () => parse(window.location.href.split('?')[1]);

export function formatTreeData(arrayList: any): DataNode[] {
  const treeSelectData: DataNode[] = arrayList.map((item: any) => {
    const node: DataNode = {
      id: item.id,
      title: item.label,
      key: `${item.id}`,
      value: item.id,
    } as DataNode;
    if (item.children) {
      node.children = formatTreeData(item.children);
    }
    return node;
  });
  return treeSelectData;
}
