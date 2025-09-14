CREATE TABLE `user`
(
	`id` int(11) NOT NULL COMMENT "编号",
	`name` varchar(200) NULL COMMENT "名称"
)
ENGINE=OLAP
PRIMARY KEY(`id`)
DISTRIBUTED BY HASH(`id`) BUCKETS 10;

insert into test.user values (1, "laokou");
