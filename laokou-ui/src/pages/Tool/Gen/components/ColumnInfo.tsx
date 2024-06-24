import React, { Fragment, useEffect, useRef, useState } from 'react';
import type { GenCodeType } from '../data';
import { Button, Checkbox, Col, Row, Tag } from 'antd';
import type { FormInstance } from 'antd';
import { history } from '@umijs/max';
import styles from '../style.less';
import { EditableProTable, ProColumns } from '@ant-design/pro-components';

export type ColumnInfoProps = {
  parentType?: string;
  data?: any[];
  dictData?: any[];
  onStepSubmit?: any;
};

const booleanEnum = [
  {
    label: 'true',
    value: '1',
  },
  {
    label: 'false',
    value: '0',
  },
];

const ColumnInfo: React.FC<ColumnInfoProps> = (props) => {
  const formRef = useRef<FormInstance>();

  const [dataSource, setDataSource] = useState<any[]>();

  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>([]);

  const { data, dictData, onStepSubmit } = props;

  const columns: ProColumns<GenCodeType>[] = [
    {
      title: '编号',
      dataIndex: 'columnId',
      editable: false,
      width: 80,
    },
    {
      title: '字段名',
      dataIndex: 'columnName',
      editable: false,
    },
    {
      title: '字段描述',
      dataIndex: 'columnComment',
      hideInForm: true,
      hideInSearch: true,
      width: 200,
    },
    {
      title: '字段类型',
      dataIndex: 'columnType',
      editable: false,
    },
    {
      title: 'Java类型',
      dataIndex: 'javaType',
      valueType: 'select',
      valueEnum: {
        Long: {
          text: 'Long',
        },
        String: {
          text: 'String',
        },
        Integer: {
          text: 'Integer',
        },
        Double: {
          text: 'Double',
        },
        BigDecimal: {
          text: 'BigDecimal',
        },
        Date: {
          text: 'Date',
        },
      },
    },
    {
      title: 'Java属性',
      dataIndex: 'javaField',
    },
    {
      title: '插入',
      dataIndex: 'isInsert',
      valueType: 'select',
      fieldProps: {
        options: booleanEnum,
      },
      render: (_, record) => {
        return <Checkbox checked={record.isInsert === '1'} />;
      },
    },
    {
      title: '编辑',
      dataIndex: 'isEdit',
      valueType: 'select',
      fieldProps: {
        options: booleanEnum,
      },
      render: (_, record) => {
        return <Checkbox checked={record.isEdit === '1'} />;
      },
    },
    {
      title: '列表',
      dataIndex: 'isList',
      valueType: 'select',
      fieldProps: {
        options: booleanEnum,
      },
      render: (_, record) => {
        return <Checkbox checked={record.isList === '1'} />;
      },
    },
    {
      title: '查询',
      dataIndex: 'isQuery',
      valueType: 'select',
      fieldProps: {
        options: booleanEnum,
      },
      render: (_, record) => {
        return <Checkbox checked={record.isQuery === '1'} />;
      },
    },
    {
      title: '查询方式',
      dataIndex: 'queryType',
      valueType: 'select',
      valueEnum: {
        EQ: {
          text: '=',
        },
        NE: {
          text: '!=',
        },
        GT: {
          text: '>',
        },
        GTE: {
          text: '>=',
        },
        LT: {
          text: '<',
        },
        LTE: {
          text: '<=',
        },
        LIKE: {
          text: 'LIKE',
        },
        BETWEEN: {
          text: 'BETWEEN',
        },
      },
    },
    {
      title: '必填',
      dataIndex: 'isRequired',
      valueType: 'select',
      fieldProps: {
        options: booleanEnum,
      },
      render: (_, record) => {
        return <Checkbox checked={record.isRequired === '1'} />;
      },
    },
    {
      title: '显示类型',
      dataIndex: 'htmlType',
      hideInSearch: true,
      valueType: 'select',
      valueEnum: {
        input: {
          text: '文本框',
        },
        textarea: {
          text: '文本域',
        },
        select: {
          text: '下拉框',
        },
        radio: {
          text: '单选框',
        },
        checkbox: {
          text: '复选框',
        },
        datetime: {
          text: '日期控件',
        },
        imageUpload: {
          text: '图片上传',
        },
        fileUpload: {
          text: '文件上传',
        },
        editor: {
          text: '富文本控件',
        },
      },
    },
    {
      title: '字典类型',
      dataIndex: 'dictType',
      hideInSearch: true,
      valueType: 'select',
      fieldProps: {
        options: dictData,
      },
      render: (text) => {
        return <Tag color="#108ee9">{text}</Tag>;
      },
    },
  ];

  useEffect(() => {
    setDataSource(data);
    if (data) {
      setEditableRowKeys(data.map((item) => item.columnId));
    }
  }, [data]);

  const onSubmit = (direction: string) => {
    if (onStepSubmit) {
      onStepSubmit('column', dataSource, direction);
    }
  };

  const onDataChange = (value: readonly GenCodeType[]) => {
    setDataSource({ ...value } as []);
  };

  return (
    <Fragment>
      <Row>
        <Col span={24}>
          <EditableProTable<GenCodeType>
            formRef={formRef}
            rowKey="columnId"
            search={false}
            columns={columns}
            value={dataSource}
            editable={{
              type: 'multiple',
              editableKeys,
              onChange: setEditableRowKeys,
              actionRender: (row, config, defaultDoms) => {
                return [defaultDoms.delete];
              },
              onValuesChange: (record, recordList) => {
                setDataSource(recordList);
              },
            }}
            onChange={onDataChange}
            recordCreatorProps={false}
          />
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
            下一步
          </Button>
        </Col>
      </Row>
    </Fragment>
  );
};

export default ColumnInfo;
