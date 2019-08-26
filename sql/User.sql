CREATE TABLE `User` (
  `id` char(36) NOT NULL,
  `ip` char(36) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `department` varchar(128) NOT NULL COMMENT '用户表',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
