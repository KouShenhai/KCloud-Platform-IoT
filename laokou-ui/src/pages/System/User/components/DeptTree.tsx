import React, { useState, useEffect } from 'react';
import { Tree, message } from 'antd';
import { getDeptTree } from '@/services/system/user';

const { DirectoryTree } = Tree;


export type TreeProps = {
  onSelect: (values: any) => Promise<void>;
};

const DeptTree: React.FC<TreeProps> = (props) => {
  const [treeData, setTreeData] = useState<any>([]);
  const [expandedKeys, setExpandedKeys] = useState<React.Key[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);

  const fetchDeptList = async () => {
    const hide = message.loading('正在查询');
    try {
      await getDeptTree({}).then((res: any) => {
        const exKeys = [];
        exKeys.push('1');
        setTreeData(res);
        exKeys.push(res[0].children[0].id);
        setExpandedKeys(exKeys);
        props.onSelect(res[0].children[0]);
      });
      hide();
      return true;
    } catch (error) {
      hide();
      return false;
    }
  };

  useEffect(() => {
    fetchDeptList();
  }, []);

  const onSelect = (keys: React.Key[], info: any) => {
    props.onSelect(info.node);
  };

  const onExpand = (expandedKeysValue: React.Key[]) => {
    setExpandedKeys(expandedKeysValue);
    setAutoExpandParent(false);
  };

  return (
    <DirectoryTree
      // multiple
      defaultExpandAll
      onExpand={onExpand}
      expandedKeys={expandedKeys}
      autoExpandParent={autoExpandParent}
      onSelect={onSelect}
      treeData={treeData}
    />
  );
};

export default DeptTree;
