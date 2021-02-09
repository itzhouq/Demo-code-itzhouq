/*
 Navicat Premium Data Transfer

 Source Server         : 42.192.99.56
 Source Server Type    : MySQL
 Source Server Version : 50651
 Source Host           : 42.192.99.56:3306
 Source Schema         : spring-security-demo

 Target Server Type    : MySQL
 Target Server Version : 50651
 File Encoding         : 65001

 Date: 09/02/2021 20:47:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES (1, 'lucy', '123');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
