package com.aning.meetingroomreservation.service

import com.aning.meetingroomreservation.cache.ICacheSetService
import com.aning.meetingroomreservation.entity.ReservationRecord

/**
 * 会议室预约缓存服务
 */
interface IReservationCacheService : ICacheSetService<ReservationRecord>