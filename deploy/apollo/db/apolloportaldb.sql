/*
Navicat MySQL Data Transfer

Source Server         : 124.222.196.51
Source Server Version : 50709
Source Host           : 124.222.196.51:3306
Source Database       : apolloportaldb

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2022-08-10 13:52:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `AppId` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'AppID',
  `Name` varchar(500) NOT NULL DEFAULT 'default' COMMENT '应用名',
  `OrgId` varchar(32) NOT NULL DEFAULT 'default' COMMENT '部门Id',
  `OrgName` varchar(64) NOT NULL DEFAULT 'default' COMMENT '部门名字',
  `OwnerName` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'ownerName',
  `OwnerEmail` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'ownerEmail',
  `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `AppId` (`AppId`(191)),
  KEY `DataChange_LastTime` (`DataChange_LastTime`),
  KEY `IX_Name` (`Name`(191))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='应用表';

-- ----------------------------
-- Records of app
-- ----------------------------
INSERT INTO `app` VALUES ('1', 'SampleApp', 'Sample App', 'TEST1', '样例部门1', 'apollo', 'apollo@acme.com', '', 'default', '2022-06-25 18:49:11', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `app` VALUES ('2', 'admin', 'laokou-admin', 'TEST1', '样例部门1', 'apollo', 'apollo@acme.com', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `app` VALUES ('3', 'gateway', 'laokou-gateway', 'TEST1', '样例部门1', 'apollo', 'apollo@acme.com', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `app` VALUES ('4', 'auth', 'laokou-auth', 'TEST1', '样例部门1', 'apollo', 'apollo@acme.com', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');

-- ----------------------------
-- Table structure for appnamespace
-- ----------------------------
DROP TABLE IF EXISTS `appnamespace`;
CREATE TABLE `appnamespace` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `Name` varchar(32) NOT NULL DEFAULT '' COMMENT 'namespace名字，注意，需要全局唯一',
  `AppId` varchar(32) NOT NULL DEFAULT '' COMMENT 'app id',
  `Format` varchar(32) NOT NULL DEFAULT 'properties' COMMENT 'namespace的format类型',
  `IsPublic` bit(1) NOT NULL DEFAULT b'0' COMMENT 'namespace是否为公共',
  `Comment` varchar(64) NOT NULL DEFAULT '' COMMENT '注释',
  `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `IX_AppId` (`AppId`),
  KEY `Name_AppId` (`Name`,`AppId`),
  KEY `DataChange_LastTime` (`DataChange_LastTime`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='应用namespace定义';

-- ----------------------------
-- Records of appnamespace
-- ----------------------------
INSERT INTO `appnamespace` VALUES ('1', 'application', 'SampleApp', 'properties', '\0', 'default app namespace', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `appnamespace` VALUES ('2', 'application', 'admin', 'properties', '\0', 'default app namespace', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `appnamespace` VALUES ('3', 'application', 'gateway', 'properties', '\0', 'default app namespace', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `appnamespace` VALUES ('4', 'application', 'auth', 'properties', '\0', 'default app namespace', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');

-- ----------------------------
-- Table structure for authorities
-- ----------------------------
DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `Username` varchar(64) NOT NULL,
  `Authority` varchar(50) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of authorities
-- ----------------------------
INSERT INTO `authorities` VALUES ('1', 'apollo', 'ROLE_user');
INSERT INTO `authorities` VALUES ('2', 'root', 'ROLE_user');
INSERT INTO `authorities` VALUES ('3', 'root', 'ROLE_user');

-- ----------------------------
-- Table structure for consumer
-- ----------------------------
DROP TABLE IF EXISTS `consumer`;
CREATE TABLE `consumer` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `AppId` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'AppID',
  `Name` varchar(500) NOT NULL DEFAULT 'default' COMMENT '应用名',
  `OrgId` varchar(32) NOT NULL DEFAULT 'default' COMMENT '部门Id',
  `OrgName` varchar(64) NOT NULL DEFAULT 'default' COMMENT '部门名字',
  `OwnerName` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'ownerName',
  `OwnerEmail` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'ownerEmail',
  `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `AppId` (`AppId`(191)),
  KEY `DataChange_LastTime` (`DataChange_LastTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开放API消费者';

-- ----------------------------
-- Records of consumer
-- ----------------------------

-- ----------------------------
-- Table structure for consumeraudit
-- ----------------------------
DROP TABLE IF EXISTS `consumeraudit`;
CREATE TABLE `consumeraudit` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `ConsumerId` int(11) unsigned DEFAULT NULL COMMENT 'Consumer Id',
  `Uri` varchar(1024) NOT NULL DEFAULT '' COMMENT '访问的Uri',
  `Method` varchar(16) NOT NULL DEFAULT '' COMMENT '访问的Method',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `IX_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `IX_ConsumerId` (`ConsumerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='consumer审计表';

-- ----------------------------
-- Records of consumeraudit
-- ----------------------------

-- ----------------------------
-- Table structure for consumerrole
-- ----------------------------
DROP TABLE IF EXISTS `consumerrole`;
CREATE TABLE `consumerrole` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `ConsumerId` int(11) unsigned DEFAULT NULL COMMENT 'Consumer Id',
  `RoleId` int(10) unsigned DEFAULT NULL COMMENT 'Role Id',
  `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) DEFAULT '' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `IX_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `IX_RoleId` (`RoleId`),
  KEY `IX_ConsumerId_RoleId` (`ConsumerId`,`RoleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='consumer和role的绑定表';

-- ----------------------------
-- Records of consumerrole
-- ----------------------------

-- ----------------------------
-- Table structure for consumertoken
-- ----------------------------
DROP TABLE IF EXISTS `consumertoken`;
CREATE TABLE `consumertoken` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `ConsumerId` int(11) unsigned DEFAULT NULL COMMENT 'ConsumerId',
  `Token` varchar(128) NOT NULL DEFAULT '' COMMENT 'token',
  `Expires` datetime NOT NULL DEFAULT '2099-01-01 00:00:00' COMMENT 'token失效时间',
  `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_Token` (`Token`),
  KEY `DataChange_LastTime` (`DataChange_LastTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='consumer token表';

-- ----------------------------
-- Records of consumertoken
-- ----------------------------

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `UserId` varchar(32) NOT NULL DEFAULT 'default' COMMENT '收藏的用户',
  `AppId` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'AppID',
  `Position` int(32) NOT NULL DEFAULT '10000' COMMENT '收藏顺序',
  `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `AppId` (`AppId`(191)),
  KEY `IX_UserId` (`UserId`),
  KEY `DataChange_LastTime` (`DataChange_LastTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用收藏表';

-- ----------------------------
-- Records of favorite
-- ----------------------------

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `PermissionType` varchar(32) NOT NULL DEFAULT '' COMMENT '权限类型',
  `TargetId` varchar(256) NOT NULL DEFAULT '' COMMENT '权限对象类型',
  `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `IX_TargetId_PermissionType` (`TargetId`(191),`PermissionType`),
  KEY `IX_DataChange_LastTime` (`DataChange_LastTime`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COMMENT='permission表';

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', 'CreateCluster', 'SampleApp', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `permission` VALUES ('2', 'CreateNamespace', 'SampleApp', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `permission` VALUES ('3', 'AssignRole', 'SampleApp', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `permission` VALUES ('4', 'ModifyNamespace', 'SampleApp+application', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `permission` VALUES ('5', 'ReleaseNamespace', 'SampleApp+application', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `permission` VALUES ('6', 'CreateApplication', 'SystemRole', '\0', 'apollo', '2022-06-25 18:51:06', 'apollo', '2022-06-25 18:51:06');
INSERT INTO `permission` VALUES ('7', 'AssignRole', 'admin', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `permission` VALUES ('8', 'CreateCluster', 'admin', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `permission` VALUES ('9', 'CreateNamespace', 'admin', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `permission` VALUES ('10', 'ManageAppMaster', 'admin', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `permission` VALUES ('11', 'ModifyNamespace', 'admin+application', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `permission` VALUES ('12', 'ReleaseNamespace', 'admin+application', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `permission` VALUES ('13', 'ModifyNamespace', 'admin+application+DEV', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `permission` VALUES ('14', 'ReleaseNamespace', 'admin+application+DEV', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `permission` VALUES ('15', 'CreateNamespace', 'gateway', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `permission` VALUES ('16', 'CreateCluster', 'gateway', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `permission` VALUES ('17', 'AssignRole', 'gateway', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `permission` VALUES ('18', 'ManageAppMaster', 'gateway', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `permission` VALUES ('19', 'ModifyNamespace', 'gateway+application', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `permission` VALUES ('20', 'ReleaseNamespace', 'gateway+application', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `permission` VALUES ('21', 'ModifyNamespace', 'gateway+application+DEV', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `permission` VALUES ('22', 'ReleaseNamespace', 'gateway+application+DEV', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `permission` VALUES ('23', 'CreateCluster', 'auth', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `permission` VALUES ('24', 'AssignRole', 'auth', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `permission` VALUES ('25', 'CreateNamespace', 'auth', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `permission` VALUES ('26', 'ManageAppMaster', 'auth', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `permission` VALUES ('27', 'ModifyNamespace', 'auth+application', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `permission` VALUES ('28', 'ReleaseNamespace', 'auth+application', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `permission` VALUES ('29', 'ModifyNamespace', 'auth+application+DEV', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `permission` VALUES ('30', 'ReleaseNamespace', 'auth+application+DEV', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `RoleName` varchar(256) NOT NULL DEFAULT '' COMMENT 'Role name',
  `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `IX_RoleName` (`RoleName`(191)),
  KEY `IX_DataChange_LastTime` (`DataChange_LastTime`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'Master+SampleApp', '', 'default', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `role` VALUES ('2', 'ModifyNamespace+SampleApp+application', '', 'default', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `role` VALUES ('3', 'ReleaseNamespace+SampleApp+application', '', 'default', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `role` VALUES ('4', 'CreateApplication+SystemRole', '\0', 'apollo', '2022-06-25 18:51:06', 'apollo', '2022-06-25 18:51:06');
INSERT INTO `role` VALUES ('5', 'Master+admin', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `role` VALUES ('6', 'ManageAppMaster+admin', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `role` VALUES ('7', 'ModifyNamespace+admin+application', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `role` VALUES ('8', 'ReleaseNamespace+admin+application', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `role` VALUES ('9', 'ModifyNamespace+admin+application+DEV', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `role` VALUES ('10', 'ReleaseNamespace+admin+application+DEV', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `role` VALUES ('11', 'Master+gateway', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `role` VALUES ('12', 'ManageAppMaster+gateway', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `role` VALUES ('13', 'ModifyNamespace+gateway+application', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `role` VALUES ('14', 'ReleaseNamespace+gateway+application', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `role` VALUES ('15', 'ModifyNamespace+gateway+application+DEV', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `role` VALUES ('16', 'ReleaseNamespace+gateway+application+DEV', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `role` VALUES ('17', 'Master+auth', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `role` VALUES ('18', 'ManageAppMaster+auth', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `role` VALUES ('19', 'ModifyNamespace+auth+application', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `role` VALUES ('20', 'ReleaseNamespace+auth+application', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `role` VALUES ('21', 'ModifyNamespace+auth+application+DEV', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `role` VALUES ('22', 'ReleaseNamespace+auth+application+DEV', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');

-- ----------------------------
-- Table structure for rolepermission
-- ----------------------------
DROP TABLE IF EXISTS `rolepermission`;
CREATE TABLE `rolepermission` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `RoleId` int(10) unsigned DEFAULT NULL COMMENT 'Role Id',
  `PermissionId` int(10) unsigned DEFAULT NULL COMMENT 'Permission Id',
  `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) DEFAULT '' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `IX_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `IX_RoleId` (`RoleId`),
  KEY `IX_PermissionId` (`PermissionId`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COMMENT='角色和权限的绑定表';

-- ----------------------------
-- Records of rolepermission
-- ----------------------------
INSERT INTO `rolepermission` VALUES ('1', '1', '1', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `rolepermission` VALUES ('2', '1', '2', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `rolepermission` VALUES ('3', '1', '3', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `rolepermission` VALUES ('4', '2', '4', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `rolepermission` VALUES ('5', '3', '5', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `rolepermission` VALUES ('6', '4', '6', '\0', 'apollo', '2022-06-25 18:51:06', 'apollo', '2022-06-25 18:51:06');
INSERT INTO `rolepermission` VALUES ('7', '5', '7', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `rolepermission` VALUES ('8', '5', '8', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `rolepermission` VALUES ('9', '5', '9', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `rolepermission` VALUES ('10', '6', '10', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `rolepermission` VALUES ('11', '7', '11', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `rolepermission` VALUES ('12', '8', '12', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `rolepermission` VALUES ('13', '9', '13', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `rolepermission` VALUES ('14', '10', '14', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `rolepermission` VALUES ('15', '11', '16', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `rolepermission` VALUES ('16', '11', '17', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `rolepermission` VALUES ('17', '11', '15', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `rolepermission` VALUES ('18', '12', '18', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `rolepermission` VALUES ('19', '13', '19', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `rolepermission` VALUES ('20', '14', '20', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `rolepermission` VALUES ('21', '15', '21', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `rolepermission` VALUES ('22', '16', '22', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `rolepermission` VALUES ('23', '17', '23', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `rolepermission` VALUES ('24', '17', '24', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `rolepermission` VALUES ('25', '17', '25', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `rolepermission` VALUES ('26', '18', '26', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `rolepermission` VALUES ('27', '19', '27', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `rolepermission` VALUES ('28', '20', '28', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `rolepermission` VALUES ('29', '21', '29', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `rolepermission` VALUES ('30', '22', '30', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');

-- ----------------------------
-- Table structure for serverconfig
-- ----------------------------
DROP TABLE IF EXISTS `serverconfig`;
CREATE TABLE `serverconfig` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `Key` varchar(64) NOT NULL DEFAULT 'default' COMMENT '配置项Key',
  `Value` varchar(2048) NOT NULL DEFAULT 'default' COMMENT '配置项值',
  `Comment` varchar(1024) DEFAULT '' COMMENT '注释',
  `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `IX_Key` (`Key`),
  KEY `DataChange_LastTime` (`DataChange_LastTime`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='配置服务自身配置';

-- ----------------------------
-- Records of serverconfig
-- ----------------------------
INSERT INTO `serverconfig` VALUES ('1', 'apollo.portal.envs', 'dev', '可支持的环境列表', '\0', 'default', '2022-06-25 18:49:11', '', '2022-06-25 18:49:11');
INSERT INTO `serverconfig` VALUES ('2', 'organizations', '[{\"orgId\":\"TEST1\",\"orgName\":\"样例部门1\"},{\"orgId\":\"TEST2\",\"orgName\":\"样例部门2\"}]', '部门列表', '\0', 'default', '2022-06-25 18:49:11', '', '2022-06-25 18:49:11');
INSERT INTO `serverconfig` VALUES ('3', 'superAdmin', 'apollo', 'Portal超级管理员', '\0', 'default', '2022-06-25 18:49:11', '', '2022-06-25 18:49:11');
INSERT INTO `serverconfig` VALUES ('4', 'api.readTimeout', '10000', 'http接口read timeout', '\0', 'default', '2022-06-25 18:49:11', '', '2022-06-25 18:49:11');
INSERT INTO `serverconfig` VALUES ('5', 'consumer.token.salt', 'someSalt', 'consumer token salt', '\0', 'default', '2022-06-25 18:49:11', '', '2022-06-25 18:49:11');
INSERT INTO `serverconfig` VALUES ('6', 'admin.createPrivateNamespace.switch', 'true', '是否允许项目管理员创建私有namespace', '\0', 'default', '2022-06-25 18:49:11', '', '2022-06-25 18:49:11');
INSERT INTO `serverconfig` VALUES ('7', 'configView.memberOnly.envs', 'dev', '只对项目成员显示配置信息的环境列表，多个env以英文逗号分隔', '\0', 'default', '2022-06-25 18:49:11', '', '2022-06-25 18:49:11');

-- ----------------------------
-- Table structure for userrole
-- ----------------------------
DROP TABLE IF EXISTS `userrole`;
CREATE TABLE `userrole` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `UserId` varchar(128) DEFAULT '' COMMENT '用户身份标识',
  `RoleId` int(10) unsigned DEFAULT NULL COMMENT 'Role Id',
  `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) DEFAULT '' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `IX_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `IX_RoleId` (`RoleId`),
  KEY `IX_UserId_RoleId` (`UserId`,`RoleId`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COMMENT='用户和role的绑定表';

-- ----------------------------
-- Records of userrole
-- ----------------------------
INSERT INTO `userrole` VALUES ('1', 'apollo', '1', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `userrole` VALUES ('2', 'apollo', '2', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `userrole` VALUES ('3', 'apollo', '3', '', '', '2022-06-25 18:49:12', 'apollo', '2022-06-25 19:28:56');
INSERT INTO `userrole` VALUES ('4', 'apollo', '5', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `userrole` VALUES ('5', 'apollo', '7', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `userrole` VALUES ('6', 'apollo', '8', '\0', 'apollo', '2022-06-25 18:52:07', 'apollo', '2022-06-25 18:52:07');
INSERT INTO `userrole` VALUES ('7', 'root', '4', '\0', 'apollo', '2022-06-25 18:55:18', 'apollo', '2022-06-25 18:55:18');
INSERT INTO `userrole` VALUES ('8', 'root', '6', '\0', 'apollo', '2022-06-25 18:55:54', 'apollo', '2022-06-25 18:55:54');
INSERT INTO `userrole` VALUES ('9', 'apollo', '4', '\0', 'apollo', '2022-06-25 18:56:50', 'apollo', '2022-06-25 18:56:50');
INSERT INTO `userrole` VALUES ('10', 'apollo', '11', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `userrole` VALUES ('11', 'apollo', '13', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `userrole` VALUES ('12', 'apollo', '14', '\0', 'apollo', '2022-07-02 13:49:10', 'apollo', '2022-07-02 13:49:10');
INSERT INTO `userrole` VALUES ('13', 'apollo', '17', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `userrole` VALUES ('14', 'apollo', '19', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');
INSERT INTO `userrole` VALUES ('15', 'apollo', '20', '\0', 'apollo', '2022-07-17 12:20:10', 'apollo', '2022-07-17 12:20:10');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `Username` varchar(64) NOT NULL DEFAULT 'default' COMMENT '用户名',
  `Password` varchar(64) NOT NULL DEFAULT 'default' COMMENT '密码',
  `Email` varchar(64) NOT NULL DEFAULT 'default' COMMENT '邮箱地址',
  `Enabled` tinyint(4) DEFAULT NULL COMMENT '是否有效',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'apollo', '$2a$10$n9qkc8bStC6UsJG1f7yk/uQcUFSr2w5E2laSMtxAtT1c4jV6GCjVW', 'apollo@acme.com', '1');
