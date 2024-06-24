import React, { useEffect } from 'react';
import { Modal, Descriptions, Button } from 'antd';
import { FormattedMessage, useIntl } from '@umijs/max';
import { getValueEnumLabel } from '@/utils/options';
import { DictValueEnumObj } from '@/components/DictTag';

/* *
 *
 * @author whiteshader@163.com
 * @datetime  2023/02/07
 * 
 * */

export type OperlogFormValueType = Record<string, unknown> & Partial<API.Monitor.Job>;

export type OperlogFormProps = {
  onCancel: (flag?: boolean, formVals?: OperlogFormValueType) => void;
  open: boolean;
  values: Partial<API.Monitor.Job>;
  statusOptions: DictValueEnumObj;
};

const OperlogForm: React.FC<OperlogFormProps> = (props) => {
  const { values, statusOptions } = props;

  useEffect(() => {}, [props]);

  const intl = useIntl();

  const misfirePolicy: any = {
    '0': '默认策略',
    '1': '立即执行',
    '2': '执行一次',
    '3': '放弃执行',
  };

  const handleCancel = () => {
    props.onCancel();
  };

  return (
    <Modal
      width={800}
      title={intl.formatMessage({
        id: 'monitor.job.detail',
        defaultMessage: '操作日志详细信息',
      })}
      open={props.open}
      destroyOnClose
      onCancel={handleCancel}
      footer={[
        <Button key="back" onClick={handleCancel}>
          关闭
        </Button>,
      ]}
    >
      <Descriptions column={24}>
        <Descriptions.Item
          span={12}
          label={<FormattedMessage id="monitor.job.job_id" defaultMessage="任务编号" />}
        >
          {values.jobId}
        </Descriptions.Item>
        <Descriptions.Item
          span={12}
          label={<FormattedMessage id="monitor.job.job_name" defaultMessage="任务名称" />}
        >
          {values.jobName}
        </Descriptions.Item>
        <Descriptions.Item
          span={12}
          label={<FormattedMessage id="monitor.job.job_group" defaultMessage="任务组名" />}
        >
          {values.jobGroup}
        </Descriptions.Item>
        <Descriptions.Item
          span={12}
          label={<FormattedMessage id="monitor.job.concurrent" defaultMessage="是否并发执行" />}
        >
          {values.concurrent === '1' ? '禁止' : '允许'}
        </Descriptions.Item>
        <Descriptions.Item
          span={12}
          label={
            <FormattedMessage id="monitor.job.misfire_policy" defaultMessage="计划执行错误策略" />
          }
        >
          {misfirePolicy[values.misfirePolicy ? values.misfirePolicy : '0']}
        </Descriptions.Item>
        <Descriptions.Item
          span={12}
          label={<FormattedMessage id="monitor.job.create_time" defaultMessage="创建时间" />}
        >
          {values.createTime?.toString()}
        </Descriptions.Item>
        <Descriptions.Item
          span={12}
          label={<FormattedMessage id="monitor.job.status" defaultMessage="状态" />}
        >
          {getValueEnumLabel(statusOptions, values.status, '未知')}
        </Descriptions.Item>
        <Descriptions.Item
          span={12}
          label={
            <FormattedMessage id="monitor.job.next_valid_time" defaultMessage="下次执行时间" />
          }
        >
          {values.nextValidTime}
        </Descriptions.Item>
        <Descriptions.Item
          span={24}
          label={
            <FormattedMessage id="monitor.job.cron_expression" defaultMessage="cron执行表达式" />
          }
        >
          {values.cronExpression}
        </Descriptions.Item>
        <Descriptions.Item
          span={24}
          label={
            <FormattedMessage id="monitor.job.invoke_target" defaultMessage="调用目标字符串" />
          }
        >
          {values.invokeTarget}
        </Descriptions.Item>
      </Descriptions>
    </Modal>
  );
};

export default OperlogForm;
