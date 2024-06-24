import { Button, Col, Divider, Form, Row, TreeSelect } from 'antd';
import React, { Fragment, useEffect, useState } from 'react';
import { history } from '@umijs/max';
import type { TableInfo } from '../data';
import styles from '../style.less';
import { DataNode } from 'antd/es/tree';
import { ProForm, ProFormRadio, ProFormSelect, ProFormText } from '@ant-design/pro-components';

export type GenInfoProps = {
  values?: any;
  menuData?: DataNode[];
  tableInfo?: TableInfo[];
  onStepSubmit?: any;
};

const GenInfo: React.FC<GenInfoProps> = (props) => {
  const [form] = Form.useForm();

  const [pathType, setPathType] = useState<string>('0');
  const [tlpType, setTlpType] = useState<string>('curd');

  const [subTablesColumnOptions, setSubTablesColumnOptions] = useState<any[]>();

  const { menuData, tableInfo, onStepSubmit } = props;

  const tablesOptions = tableInfo?.map((item: any) => {
    return {
      value: item.tableName,
      label: `${item.tableName}：${item.tableComment}`,
    };
  });

  if (tableInfo) {
    for (let index = 0; index < tableInfo?.length; index += 1) {
      const tbl = tableInfo[index];
      if (tbl.tableName === props.values.subTableName) {
        const opts = [];
        tbl.columns.forEach((item) => {
          opts.push({
            value: item.columnName,
            label: `${item.columnName}: ${item.columnComment}`,
          });
        });
        break;
      }
    }
  }

  const treeColumns = props.values.columns.map((item: any) => {
    return {
      value: item.columnName,
      label: `${item.columnName}: ${item.columnComment}`,
    };
  });

  const onSubmit = async (direction: string) => {
    const values = await form.validateFields();
    onStepSubmit('gen', values, direction);
  };

  useEffect(() => {
    setPathType(props.values.genType);
    setTlpType(props.values.tplCategory);
  }, [props.values.genType, props.values.tplCategory]);

  return (
    <Fragment>
      <Row>
        <Col span={24}>
          <ProForm
            form={form}
            onFinish={async () => {
              const values = await form.validateFields();
              onStepSubmit('gen', values);
            }}
            initialValues={{
              curd: props.values.curd,
              tree: props.values.tree,
              sub: props.values.sub,
              tplCategory: props.values.tplCategory,
              packageName: props.values.packageName,
              moduleName: props.values.moduleName,
              businessName: props.values.businessName,
              functionName: props.values.functionName,
              parentMenuId: props.values.parentMenuId,
              genType: props.values.genType,
              genPath: props.values.genPath,
              treeCode: props.values.treeCode,
              treeParentCode: props.values.treeParentCode,
              treeName: props.values.treeName,
              subTableName: props.values.subTableName,
              subTableFkName: props.values.subTableFkName,
            }}
            submitter={{
              resetButtonProps: {
                style: { display: 'none' },
              },
              submitButtonProps: {
                style: { display: 'none' },
              },
            }}
          >
            <Row gutter={[16, 16]}>
              <Col span={12} order={1}>
                <ProFormSelect
                  fieldProps={{
                    onChange: (val) => {
                      setTlpType(val);
                    },
                  }}
                  valueEnum={{
                    crud: '单表（增删改查）',
                    tree: '树表（增删改查）',
                    sub: '主子表（增删改查）',
                  }}
                  name="tplCategory"
                  label="生成模板"
                  rules={[
                    {
                      required: true,
                      message: '选择类型',
                    },
                  ]}
                />
              </Col>
              <Col span={12} order={2}>
                <ProFormText name="packageName" label="生成包路径" />
              </Col>
            </Row>
            <Row gutter={[16, 16]}>
              <Col span={12} order={1}>
                <ProFormText name="moduleName" label="生成模块名" />
              </Col>
              <Col span={12} order={2}>
                <ProFormText name="businessName" label="生成业务名" />
              </Col>
            </Row>
            <Row gutter={[16, 16]}>
              <Col span={12} order={1}>
                <ProFormText name="functionName" label="生成功能名" />
              </Col>
              <Col span={12} order={2}>
                <ProForm.Item
                  labelCol={{ span: 20 }}
                  name="parentMenuId"
                  label="父菜单"
                >
                  <TreeSelect
                    style={{ width: '74%' }}
                    defaultValue={props.values.parentMenuId}
                    treeData={menuData}
                    placeholder="请选择父菜单"
                  />
                </ProForm.Item>
              </Col>
            </Row>
            <Row gutter={[16, 16]}>
              <Col span={24}>
                <ProFormRadio.Group
                  valueEnum={{
                    '0': 'zip压缩包',
                    '1': '自定义路径',
                  }}
                  name="genType"
                  label="生成代码方式"
                  rules={[
                    {
                      required: true,
                      message: '选择类型',
                    },
                  ]}
                  fieldProps={{
                    onChange: (e) => {
                      setPathType(e.target.value);
                    },
                  }}
                />
              </Col>
            </Row>
            <Row gutter={[16, 16]}>
              <Col span={24} order={1}>
                <ProFormText
                  hidden={pathType === '0'}
                  width="md"
                  name="genPath"
                  label="自定义路径"
                />
              </Col>
            </Row>
            <div hidden={tlpType !== 'tree'}>
              <Divider orientation="left">其他信息</Divider>
              <Row gutter={[16, 16]}>
                <Col span={12} order={1}>
                  <ProFormSelect
                    name="treeCode"
                    label="树编码字段"
                    options={treeColumns}
                    rules={[
                      {
                        required: tlpType === 'tree',
                        message: '树编码字段',
                      },
                    ]}
                  />
                </Col>
                <Col span={12} order={2}>
                  <ProFormSelect
                    name="treeParentCode"
                    label="树父编码字段"
                    options={treeColumns}
                    rules={[
                      {
                        required: tlpType === 'tree',
                        message: '树父编码字段',
                      },
                    ]}
                  />
                </Col>
              </Row>
              <Row gutter={[16, 16]}>
                <Col span={12} order={1}>
                  <ProFormSelect
                    name="treeName"
                    label="树名称字段"
                    options={treeColumns}
                    rules={[
                      {
                        required: tlpType === 'tree',
                        message: '树名称字段',
                      },
                    ]}
                  />
                </Col>
              </Row>
            </div>
            <div hidden={tlpType !== 'sub'}>
              <Divider orientation="left">关联信息</Divider>
              <Row gutter={[16, 16]}>
                <Col span={12} order={1}>
                  <ProFormSelect
                    name="subTableName"
                    label="关联子表的表名"
                    options={tablesOptions}
                    rules={[
                      {
                        required: tlpType === 'sub',
                        message: '关联子表的表名',
                      },
                    ]}
                    fieldProps={{
                      onChange: (val) => {
                        form.setFieldsValue({
                          subTableFkName: '',
                        });
                        if (tableInfo) {
                          for (let index = 0; index < tableInfo?.length; index += 1) {
                            const tbl = tableInfo[index];
                            if (tbl.tableName === val) {
                              const opts: any[] = [];
                              tbl.columns.forEach((item) => {
                                opts.push({
                                  value: item.columnName,
                                  label: `${item.columnName}：${item.columnComment}`,
                                });
                              });
                              setSubTablesColumnOptions(opts);
                              break;
                            }
                          }
                        }
                      },
                    }}
                  />
                </Col>
                <Col span={12} order={2}>
                  <ProFormSelect
                    name="subTableFkName"
                    options={subTablesColumnOptions}
                    label="子表关联的外键名"
                    rules={[
                      {
                        required: tlpType === 'sub',
                        message: '子表关联的外键名',
                      },
                    ]}
                  />
                </Col>
              </Row>
            </div>
          </ProForm>
        </Col>
      </Row>
      <Row justify="center">
        <Col span={4}>
          <Button
            type="primary"
            onClick={() => {
              history.back();
            }}
          >
            返回
          </Button>
        </Col>
        <Col span={4}>
          <Button
            type="primary"
            className={styles.step_buttons}
            onClick={() => {
              onSubmit('prev');
            }}
          >
            上一步
          </Button>
        </Col>
        <Col span={4}>
          <Button
            type="primary"
            onClick={() => {
              onSubmit('next');
            }}
          >
            提交
          </Button>
        </Col>
      </Row>
    </Fragment>
  );
};

export default GenInfo;
