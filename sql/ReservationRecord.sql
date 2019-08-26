CREATE TABLE `ReservationRecord` (
  `id` char(36) NOT NULL,
  `ip` char(36) NOT NULL,
  `userId` char(36) NOT NULL,
  `meetingRoomId` char(36) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `status` int(11) NOT NULL COMMENT '会议室状态\n-1 - 取消预订\n0 - 空置中\n1 - 使用中',
  `comments` varchar(256) DEFAULT NULL,
  `reserveTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
