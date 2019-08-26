CREATE TABLE `MeetingRoom` (
  `id` char(36) NOT NULL,
  `name` varchar(128) NOT NULL,
  `location` varchar(256) DEFAULT NULL,
  `comments` varchar(256) DEFAULT NULL COMMENT '会议室表',
  `createDateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
