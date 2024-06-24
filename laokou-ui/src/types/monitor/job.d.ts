/**
 * 定时任务调度 Model Declare
 * 
 * @author whiteshader@163.com
 * @date 2023-02-07
 */

declare namespace API.Monitor {

  export interface Job {
    jobId: number;
    jobName: string;
    jobGroup: string;
    invokeTarget: string;
    cronExpression: string;
    misfirePolicy: string;
    concurrent: string;
    nextValidTime: string;
    status: string;
    createBy: string;
    createTime: Date;
    updateBy: string;
    updateTime: Date;
    remark: string;
  }

  export interface JobListParams {
    jobId?: string;
    jobName?: string;
    jobGroup?: string;
    invokeTarget?: string;
    cronExpression?: string;
    misfirePolicy?: string;
    concurrent?: string;
    status?: string;
    createBy?: string;
    createTime?: string;
    updateBy?: string;
    updateTime?: string;
    remark?: string;
    pageSize?: string;
    current?: string;
  }

  export interface JobInfoResult { 
    code: number;
    msg: string;
    data: Job;
  } 

   export interface JobPageResult { 
    code: number;
    msg: string;
    total: number;
    rows: Array<Job>;
  }

}