/**
 * 定时任务调度日志 Model Declare
 * 
 * @author whiteshader
 * @date 2023-02-07
 */

declare namespace API.Monitor {

  export interface JobLog {
    jobLogId: number;
    jobName: string;
    jobGroup: string;
    invokeTarget: string;
    jobMessage: string;
    status: string;
    exceptionInfo: string;
    createTime: Date;
  }

  export interface JobLogListParams {
    jobLogId?: string;
    jobName?: string;
    jobGroup?: string;
    invokeTarget?: string;
    jobMessage?: string;
    status?: string;
    exceptionInfo?: string;
    createTime?: string;
    pageSize?: string;
    current?: string;
  }

  export interface JobLogInfoResult { 
    code: number;
    msg: string;
    data: JobLog;
  } 

   export interface JobLogPageResult { 
    code: number;
    msg: string;
    total: number;
    rows: Array<JobLog>;
  }

}