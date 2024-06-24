import { getValueEnumLabel } from '@/utils/options';
import { FormattedMessage, useIntl } from '@umijs/max';
import { Descriptions, Modal } from 'antd';
import React, { useEffect } from 'react';
import { DictValueEnumObj } from '@/components/DictTag';

/* *
 *
 * @author whiteshader@163.com
 * @datetime  2021/09/16
 * 
 * */

export type JobLogFormValueType = Record<string, unknown> & Partial<API.Monitor.JobLog>;

export type JobLogFormProps = {
  onCancel: (flag?: boolean, formVals?: JobLogFormValueType) => void;
  open: boolean;
  values: Partial<API.Monitor.JobLog>;
  statusOptions: DictValueEnumObj;
  jobGroupOptions: DictValueEnumObj;
};

const JobLogDetailForm: React.FC<JobLogFormProps> = (props) => {

  const { values, statusOptions, jobGroupOptions } = props;

  useEffect(() => {
  }, []);

  const intl = useIntl();
  const handleOk = () => {
  };
  const handleCancel = () => {
    props.onCancel();
  };
  
  return (
    <Modal
      width={640}
      title={intl.formatMessage({
        id: 'monitor.job.log.title',
        defaultMessage: '定时任务调度日志',
      })}
      open={props.open}
      forceRender
      destroyOnClose
      onOk={handleOk}
      onCancel={handleCancel}
    >
      <Descriptions column={24}>
        <Descriptions.Item
          span={12}
          label={<FormattedMessage id="monitor.job.job_id" defaultMessage="任务编号" />}
        >
          {values.jobLogId}
        </Descriptions.Item>
        <Descriptions.Item
          span={12}
          label={<FormattedMessage id="monitor.job.create_time" defaultMessage="执行时间" />}
        >
          {values.createTime?.toString()}
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
          {getValueEnumLabel(jobGroupOptions, values.jobGroup, '无')}
        </Descriptions.Item>
        <Descriptions.Item
          span={24}
          label={<FormattedMessage id="monitor.job.invoke_target" defaultMessage="调用目标" />}
        >
          {values.invokeTarget}
        </Descriptions.Item>
        <Descriptions.Item
          span={24}
          label={<FormattedMessage id="monitor.job.log.job_message" defaultMessage="日志信息" />}
        >
          {values.jobMessage}
        </Descriptions.Item>
        <Descriptions.Item
          span={24}
          label={<FormattedMessage id="monitor.job.log.exception_info" defaultMessage="异常信息" />}
        >
          {values.exceptionInfo}
        </Descriptions.Item>
        <Descriptions.Item
          span={12}
          label={<FormattedMessage id="monitor.job.status" defaultMessage="执行状态" />}
        >
          {getValueEnumLabel(statusOptions, values.status, '未知')}
        </Descriptions.Item>
      </Descriptions>
    </Modal>
  );
};

export default JobLogDetailForm;
