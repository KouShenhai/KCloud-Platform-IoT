drop table if exists `seata_state_machine_def`;
CREATE TABLE if not exists `seata_state_machine_def`(
                                        id varchar(50) not null comment 'id',
                                        `name` varchar(50) not null comment 'name',
                                        tenant_id varchar(50) not null comment 'tenant id',
                                        app_name varchar(50) not null comment 'application name',
                                        type varchar(50) comment 'state language type',
                                        comment_ varchar(50) comment 'comment',
                                        ver varchar(50) not null comment 'version',
                                        gmt_create timestamp not null comment 'create time',
                                        status varchar(50) not null comment 'status(ac:active|in:inactive)',
                                        content text comment 'content',
                                        recover_strategy varchar(50) comment 'transaction recover strategy(compensate|retry)'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

drop table if exists