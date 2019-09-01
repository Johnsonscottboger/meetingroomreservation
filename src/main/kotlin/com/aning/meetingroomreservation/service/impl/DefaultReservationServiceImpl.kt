package com.aning.meetingroomreservation.service.impl

import com.aning.meetingroomreservation.annotation.cache.CacheAdd
import com.aning.meetingroomreservation.cache.ICacheHashService
import com.aning.meetingroomreservation.cache.ICacheSetService
import com.aning.meetingroomreservation.dao.IReservationDao
import com.aning.meetingroomreservation.entity.ReservationRecord
import com.aning.meetingroomreservation.service.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource

@Service
public class DefaultReservationServiceImpl : IReservationService {
    private val log = LoggerFactory.getLogger(this::class.java)
    @Resource
    private lateinit var _dao: IReservationDao
    @Autowired
    private lateinit var _scheduler: ISchedulerService
    @Autowired
    private lateinit var _cacheService: ICacheSetService<ReservationRecord>

    /**
     * 初始化, 加载历史数据
     */
    override fun initialize() {
        this._scheduler.initialize()
    }

    /**
     * 添加 [ReservationRecord] 记录
     * @param reservationRecord 添加的 [ReservationRecord] 实例
     */
    @CacheAdd()
    override fun add(reservationRecord: ReservationRecord) {
        this._scheduler.add(reservationRecord)
        this._dao.add(reservationRecord)
    }

    /**
     * 删除 [ReservationRecord] 记录
     * @param reservationRecord 删除的 [ReservationRecord] 实例
     */
    override fun delete(reservationRecord: ReservationRecord) {
        this._scheduler.delete(reservationRecord)
        this._dao.delete(reservationRecord)
    }

    /**
     * 修改 [ReservationRecord] 记录
     * @param reservationRecord 修改的 [ReservationRecord] 实例
     */
    override fun update(reservationRecord: ReservationRecord) {
        this._scheduler.update(reservationRecord)
        this._dao.update(reservationRecord)
    }

    /**
     * 添加或修改 [ReservationRecord] 记录
     * @param reservationRecord 添加或修改的 [ReservationRecord] 实例
     */
    override fun addOrUpdate(reservationRecord: ReservationRecord) {
        val now = Calendar.getInstance()
        now.time = reservationRecord.startTime
        val start = Calendar.Builder()
                .setDate(now[Calendar.YEAR], now[Calendar.MONTH], now[Calendar.DAY_OF_MONTH])
                .setTimeOfDay(0, 0, 0)
                .build()
                .time
        val end = Calendar.Builder()
                .setDate(now[Calendar.YEAR], now[Calendar.MONTH], now[Calendar.DAY_OF_MONTH])
                .setTimeOfDay(23, 59, 59)
                .build()
                .time
        val key = "${start.time}_${end.time}"
        this._scheduler.addOrUpdate(reservationRecord)
        this._dao.addOrUpdate(reservationRecord)
        this._cacheService.addOrUpdate(key, reservationRecord, 86_400_000) { i, p ->
            i.id == p.id
        }
    }

    /**
     * 获取所有 [ReservationRecord] 记录
     * @return 所有 [ReservationRecord] 实例
     */
    override fun getAll(): List<ReservationRecord> {
        return this._dao.getAll()
    }

    /**
     * 获取指定的 [id] 的 [ReservationRecord] 记录
     * @param id 指定的 [ReservationRecord] 主键
     * @return [ReservationRecord] 记录
     */
    override fun getById(id: String): ReservationRecord? {
        return this._dao.getById(id)
    }

    /**
     * 获取指定的 [userId] 的 [ReservationRecord] 记录
     * @param userId 指定的 [ReservationRecord] 用户主键
     * @return [ReservationRecord] 记录
     */
    override fun getByUserId(userId: String): List<ReservationRecord> {
        return this._dao.getByUserId(userId)
    }

    /**
     * 获取指定的 [meetingRoomId] 的 [ReservationRecord] 记录
     * @param meetingRoomId 指定的 [ReservationRecord] 会议室主键
     * @return [ReservationRecord] 记录
     */
    override fun getByMeetingRoomId(meetingRoomId: String): List<ReservationRecord> {
        return this._dao.getByMeetingRoomId(meetingRoomId)
    }

    /**
     * 获取指定的 [start] 和 [end] 的 [ReservationRecord] 记录
     * @param start 指定的开始时间
     * @param end 指定的结束时间
     * @return [ReservationRecord] 记录
     */
    override fun getByDateTime(start: Date, end: Date): List<ReservationRecord> {
        val key = "${start.time}_${end.time}"
        return this._cacheService.getOrAddRange(key, valuesFactory = {
            log.warn("Get from database")
            this._dao.getByDateTime(start, end)
        }, expire = 86_400_000).toList()
    }

    /**
     * 获取指定的 [meetingRoomId], [start] 和 [end] 的 [ReservationRecord] 记录
     * @param meetingRoomId 指定的会议室
     * @param start 指定的开始时间
     * @param end 指定的结束时间
     * @return [ReservationRecord] 记录
     */
    override fun getByMeetingRoomIdDateTime(meetingRoomId: String, start: Date, end: Date): List<ReservationRecord> {
        return this._dao.getByMeetingRoomIdDateTime(meetingRoomId, start, end)
    }
}