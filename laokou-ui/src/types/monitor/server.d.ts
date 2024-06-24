
/* *
 *
 * @author whiteshader@163.com
 * @datetime  2023/02/07
 * 
 * */


declare namespace API.Monitor {

  export type CpuInfoType = {
    cpuNum: string;
    total: string;
    sys: string;
    used: string;
    wait: string;
    free: string;
  };

  export type MemInfoType = {
    total: string;
    used: string;
    usage: string;
    free: string;
  };

  export type SysInfoType = {
    computerIp: string;
    computerName: string;
    osArch: string;
    osName: string;
    userDir: string;
  };

  export type JvmInfoType = {
    free: string;
    home: string;
    max: string;
    name: string;
    runTime: string;
    startTime: string;
    total: string;
    usage: string;
    used: string;
    version: string;
  };

  export type DiskInfoType = {
    dirName: string;
    free: string;
    sysTypeName: string;
    total: string;
    typeName: string;
    usage: string;
    used: string;
  };

  export type ServerInfoType = {
    cpu: CpuInfoType;
    mem: MemInfoType;
    jvm: JvmInfoType;
    sys: SysInfoType;
    sysFiles: DiskInfoType[];
  };

  export type ServerInfoResponseType = {
    data: ServerInfoType;
    code: number;
    msg: string;
  };

  export type CpuRowType = {
    name: string;
    value: string;
  };

  export type MemRowType = {
    name: string;
    mem: string;
    jvm: string;
  };

}