/*
 Navicat Premium Data Transfer

 Source Server         : 本地虚拟机
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.111.128:3306
 Source Schema         : mam

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 12/06/2022 00:08:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mam_project
-- ----------------------------
DROP TABLE IF EXISTS `mam_project`;
CREATE TABLE `mam_project`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '项目id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '项目名称',
  `createTime` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `leader` bigint(0) NULL DEFAULT NULL COMMENT '项目负责人账户',
  `leaderName` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '负责人名称',
  `taskCount` int(0) NOT NULL DEFAULT 0 COMMENT '项目任务数量',
  `finishedTask` int(0) NULL DEFAULT 0 COMMENT '已完成任务数量',
  `deleted` int(0) NULL DEFAULT 0 COMMENT '逻辑删除位',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs COMMENT = '媒资项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mam_project
-- ----------------------------

-- ----------------------------
-- Table structure for mam_task
-- ----------------------------
DROP TABLE IF EXISTS `mam_task`;
CREATE TABLE `mam_task`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '任务名称',
  `createTime` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `project` bigint(0) NULL DEFAULT NULL COMMENT '归属项目',
  `projectName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '归属项目名称',
  `cataloger` bigint(0) NULL DEFAULT NULL COMMENT '编目员账户',
  `catalogerName` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '编目员名称',
  `auditor` bigint(0) NULL DEFAULT NULL COMMENT '审核员账户',
  `auditorName` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '审核员名称',
  `status` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '任务状态，0：编目中、1：待修改、2：审核中、3：完成',
  `deleted` int(0) NULL DEFAULT 0 COMMENT '逻辑删除位',
  `videoInfoId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '对应视频信息id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs COMMENT = '媒资任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mam_task
-- ----------------------------

-- ----------------------------
-- Table structure for mam_user
-- ----------------------------
DROP TABLE IF EXISTS `mam_user`;
CREATE TABLE `mam_user`  (
  `account` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '用户账户',
  `username` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '用户密码',
  `createTime` bigint(0) NULL DEFAULT NULL COMMENT '注册时间',
  `role` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '角色权限',
  `deleted` int(0) NULL DEFAULT 0 COMMENT '逻辑删除位',
  PRIMARY KEY (`account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs COMMENT = '媒资用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mam_user
-- ----------------------------
INSERT INTO `mam_user` VALUES ('180807426', '王锦彬', '$2a$10$FENnvc.2kCl6jK33sR5Y2.8CMaiTwKSFTqZR37IhEDmc8ughH2Rk2', 1642692983000, 'ROLE_ADMIN', 0);

SET FOREIGN_KEY_CHECKS = 1;
