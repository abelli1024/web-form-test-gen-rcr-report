CREATE DATABASE IF NOT EXISTS must_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
USE must_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_chat_item
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_item`;
CREATE TABLE `t_chat_item` (
  `id` bigint NOT NULL DEFAULT '0',
  `chat_token` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `model_name` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `session_id` bigint DEFAULT NULL,
  `question` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `answer` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `time` decimal(11,2) DEFAULT NULL,
  `status` tinyint DEFAULT NULL COMMENT 'Status 0 Normal 1 Delete',
  `created_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='Session Recording';

-- ----------------------------
-- Table structure for t_replicate
-- ----------------------------
DROP TABLE IF EXISTS `t_replicate`;
CREATE TABLE `t_replicate` (
  `link_id` bigint DEFAULT NULL,
  `id` varchar(128) COLLATE utf8mb4_0900_bin NOT NULL,
  `model` varchar(256) COLLATE utf8mb4_0900_bin DEFAULT NULL,
  `version` varchar(128) COLLATE utf8mb4_0900_bin DEFAULT NULL,
  `input` text COLLATE utf8mb4_0900_bin,
  `logs` text COLLATE utf8mb4_0900_bin,
  `output` longtext COLLATE utf8mb4_0900_bin,
  `error` text COLLATE utf8mb4_0900_bin,
  `status` varchar(64) COLLATE utf8mb4_0900_bin DEFAULT NULL,
  `created_at` varchar(64) COLLATE utf8mb4_0900_bin DEFAULT NULL,
  `started_at` varchar(64) COLLATE utf8mb4_0900_bin DEFAULT NULL,
  `completed_at` varchar(64) COLLATE utf8mb4_0900_bin DEFAULT NULL,
  `urls_cancel` varchar(256) COLLATE utf8mb4_0900_bin DEFAULT NULL,
  `urls_get` varchar(256) COLLATE utf8mb4_0900_bin DEFAULT NULL,
  `metrics_input_token_count` int DEFAULT NULL,
  `metrics_output_token_count` int DEFAULT NULL,
  `metrics_predict_time` decimal(40,20) DEFAULT NULL,
  `metrics_time_to_first_token` decimal(40,20) DEFAULT NULL,
  `metrics_tokens_per_second` decimal(40,20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='Replicate record';

SET FOREIGN_KEY_CHECKS = 1;
