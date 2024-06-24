import React, { useEffect, useState } from 'react';
import BaseInfo from './components/BaseInfo';
import { Card, Layout, message, Steps } from 'antd';
import ColumnInfo from './components/ColumnInfo';
import GenInfo from './components/GenInfo';
import { getGenCode, updateData } from './service';
import { formatTreeData } from '@/utils/tree';
import styles from './style.less';
import type { GenCodeType } from './data';
import { getMenuTree } from '@/services/system/menu';
import { getDictTypeList } from '@/services/system/dict';
import queryString from 'query-string';
import { useLocation } from '@umijs/max';

const { Content } = Layout;

export type GenCodeArgs = {
  id: string;
};

const TableList: React.FC = () => {
  const location = useLocation();
  const query = queryString.parse(location.search);
  const { id } = query as GenCodeArgs;
  const tableId = id;

  const [currentStep, setCurrentStep] = useState<number>(0);
  const [columnData, setColumnData] = useState<any>([]);
  const [baseInfoData, setBaseInfoData] = useState<any>([]);
  const [genInfoData, setGenInfoData] = useState<any>([]);
  const [menuTree, setMenuTree] = useState<any>([]);
  const [dictData, setDictData] = useState<any>([]);
  const [tableInfo, setTableInfo] = useState<any>([]);
  const [formData, setFormData] = useState<any>([]);
  const [stepComponent, setStepComponent] = useState<any>([]);
  const [stepKey, setStepKey] = useState<string>('');

  const getCurrentStepAndComponent = (key?: string) => {
    if (key === 'base') {
      return (
        <BaseInfo
          values={baseInfoData}
          // eslint-disable-next-line @typescript-eslint/no-use-before-define
          onStepSubmit={onNextStep}
        />
      );
    }
    if (key === 'column') {
      return (
        <ColumnInfo
          data={columnData}
          dictData={dictData}
          // eslint-disable-next-line @typescript-eslint/no-use-before-define
          onStepSubmit={onNextStep}
        />
      );
    }
    if (key === 'gen') {
      return (
        <GenInfo
          values={genInfoData}
          menuData={menuTree}
          tableInfo={tableInfo}
          // eslint-disable-next-line @typescript-eslint/no-use-before-define
          onStepSubmit={onNextStep}
        />
      );
    }
    return null;
  };

  const onNextStep = (step: string, values: any, direction: string) => {
    let stepKey = 'base';
    if (step === 'base') {
      setStepKey('column');
      setCurrentStep(1);
      setFormData(values);
      setStepComponent(getCurrentStepAndComponent(stepKey));
    } else if (step === 'column') {
      if (direction === 'prev') {
        setStepKey('base');
        setCurrentStep(0);
      } else {
        setStepKey('gen');
        const tableData: GenCodeType = formData || ({} as GenCodeType);
        tableData.columns = values;
        setCurrentStep(2);
        setFormData(tableData);
      }
      setStepComponent(getCurrentStepAndComponent(stepKey));
    } else if (step === 'gen') {
      if (direction === 'prev') {
        setStepKey('column');
        setCurrentStep(1);
        setStepComponent(getCurrentStepAndComponent(stepKey));
      } else {
        const postData: GenCodeType = {
          ...formData,
          ...values,
          params: values,
          tableId: tableId,
        };
        setFormData(postData);
        updateData({ ...postData } as GenCodeType).then((res) => {
          if (res.code === 200) {
            message.success('提交成功');
            history.back();
          } else {
            message.success('提交失败');
          }
        });
      }
    }
  };
  useEffect(() => {
    setStepComponent(getCurrentStepAndComponent(stepKey));
  }, [stepKey]);

  useEffect(() => {
    getGenCode(tableId).then((res) => {
      if (res.code === 200) {
        setBaseInfoData(res.data.info);
        setColumnData(res.data.rows);
        setGenInfoData(res.data.info);
        setTableInfo(res.data.tables);
        setStepKey('base');
      } else {
        message.error(res.msg);
      }
    });
    getMenuTree().then((res) => {
      if (res.code === 200) {
        const treeData = formatTreeData(res.data);
        setMenuTree(treeData);
      } else {
        message.error(res.msg);
      }
    });

    getDictTypeList().then((res: any) => {
      if (res.code === 200) {
        const dicts = res.rows.map((item: any) => {
          return {
            label: item.dictName,
            value: item.dictType,
          };
        });
        setDictData(dicts);
      } else {
        message.error(res.msg);
      }
    });
  }, []);

  // const onFinish = (values: any) => {
  //   console.log('Success:', values);
  // };

  // const onFinishFailed = (errorInfo: any) => {
  //   console.log('Failed:', errorInfo);
  // };

  return (
    <Content>
      <Card className={styles.tabsCard} bordered={false}>
        <Steps current={currentStep} className={styles.steps} items={[
          {
            title: '基本信息',
          },
          {
            title: '字段信息',
          },
          {
            title: '生成信息',
          },
        ]} />
        {stepComponent}
      </Card>
    </Content>
  );
};

export default TableList;
