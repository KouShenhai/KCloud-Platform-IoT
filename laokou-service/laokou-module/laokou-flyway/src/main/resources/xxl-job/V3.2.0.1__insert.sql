-- ----------------------------
-- Records of xxl_job_group
-- ----------------------------
INSERT INTO `xxl_job_group` VALUES (4, 'laokou-logstash', '分布式链路', 0, 'http://192.168.30.1:9999/', '2023-11-07 11:44:45');

-- ----------------------------
-- Records of xxl_job_info
-- ----------------------------
INSERT INTO `xxl_job_info` VALUES (3, 4, '分布式链路', '2023-11-03 15:45:55', '2023-11-03 15:45:55', 'laokou', '2413176044@qq.com', 'CRON', '0 50 23 L * ?', 'DO_NOTHING', 'FIRST', 'traceJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-11-03 15:45:55', '', 0, 0, 0);

-- ----------------------------
-- Records of xxl_job_lock
-- ----------------------------
INSERT INTO `xxl_job_lock` VALUES ('schedule_lock');

-- ----------------------------
-- Records of xxl_job_user
-- ----------------------------
INSERT INTO `xxl_job_user` VALUES (1, 'admin', '653ef328fde361f0f2977b7ce8bc08f6', 1, NULL);