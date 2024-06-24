import React, { useEffect } from 'react';
import {
  ProForm,
  ProFormDigit,
  ProFormText,
  ProFormSelect,
  ProFormTextArea,
  ProFormRadio,
  } from '@ant-design/pro-components';
import { Form, Modal} from 'antd';
import { useIntl, FormattedMessage } from '@umijs/max';
import { DictValueEnumObj } from '@/components/DictTag';

export type NoticeFormData = Record<string, unknown> & Partial<API.System.Notice>;

export type NoticeFormProps = {
  onCancel: (flag?: boolean, formVals?: NoticeFormData) => void;
  onSubmit: (values: NoticeFormData) => Promise<void>;
  open: boolean;
  values: Partial<API.System.Notice>;
  noticeTypeOptions: DictValueEnumObj;
  statusOptions: DictValueEnumObj;
};

const NoticeForm: React.FC<NoticeFormProps> = (props) => {
  const [form] = Form.useForm();
  
  const { noticeTypeOptions,statusOptions, } = props;

  useEffect(() => {
    form.resetFields();
    form.setFieldsValue({
			noticeId: props.values.noticeId,
			noticeTitle: props.values.noticeTitle,
			noticeType: props.values.noticeType,
			noticeContent: props.values.noticeContent,
			status: props.values.status,
			createBy: props.values.createBy,
			createTime: props.values.createTime,
			updateBy: props.values.updateBy,
			updateTime: props.values.updateTime,
			remark: props.values.remark,
    });
  }, [form, props]);

  const intl = useIntl();
  const handleOk = () => {
    form.submit();
  };
  const handleCancel = () => {
    props.onCancel();
  };
  const handleFinish = async (values: Record<string, any>) => {
    props.onSubmit(values as NoticeFormData);
  };

  return (
    <Modal
      width={640}
      title={intl.formatMessage({
        id: 'system.notice.title',
        defaultMessage: '编辑通知公告',
      })}
      forceRender
      open={props.open}
      destroyOnClose
      onOk={handleOk}
      onCancel={handleCancel}
    >
		  <ProForm 
        form={form}
        grid={true}
        submitter={false}
        layout="horizontal" 
        onFinish={handleFinish}>
        <ProFormDigit
          name="noticeId"
          label={intl.formatMessage({
            id: 'system.notice.notice_id',
            defaultMessage: '公告编号',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入公告编号"
          disabled
          hidden={true}
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入公告编号！" defaultMessage="请输入公告编号！" />,                  
            },
          ]}
        />
        <ProFormText
          name="noticeTitle"
          label={intl.formatMessage({
            id: 'system.notice.notice_title',
            defaultMessage: '公告标题',
          })}
          placeholder="请输入公告标题"
          rules={[
            {
              required: true,
              message: <FormattedMessage id="请输入公告标题！" defaultMessage="请输入公告标题！" />,                  
            },
          ]}
        />
        <ProFormSelect
          valueEnum={noticeTypeOptions}
          name="noticeType"
          label={intl.formatMessage({
            id: 'system.notice.notice_type',
            defaultMessage: '公告类型',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入公告类型"
          rules={[
            {
              required: true,
              message: <FormattedMessage id="请输入公告类型！" defaultMessage="请输入公告类型！" />,                  
            },
          ]}
        />
        <ProFormRadio.Group
          valueEnum={statusOptions}
          name="status"
          label={intl.formatMessage({
            id: 'system.notice.status',
            defaultMessage: '公告状态',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入公告状态"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入公告状态！" defaultMessage="请输入公告状态！" />,                  
            },
          ]}
        />
        <ProFormTextArea
          name="noticeContent"
          label={intl.formatMessage({
            id: 'system.notice.notice_content',
            defaultMessage: '公告内容',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入公告内容"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入公告内容！" defaultMessage="请输入公告内容！" />,                  
            },
          ]}
        />
        <ProFormText
          name="remark"
          label={intl.formatMessage({
            id: 'system.notice.remark',
            defaultMessage: '备注',
          })}
          colProps={{ md: 12, xl: 24 }}
          placeholder="请输入备注"
          rules={[
            {
              required: false,
              message: <FormattedMessage id="请输入备注！" defaultMessage="请输入备注！" />,                  
            },
          ]}
        />
      </ProForm>
    </Modal>
  );
};

export default NoticeForm;
