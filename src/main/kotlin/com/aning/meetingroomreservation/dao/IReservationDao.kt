package com.aning.meetingroomreservation.dao

import com.aning.meetingroomreservation.entity.ReservationRecord
import org.apache.ibatis.annotations.Param
import java.util.*

/**
 * 提供对 [ReservationRecord] 实体操作的方法
 */
interface IReservationDao {

    /**
     * 添加 [ReservationRecord] 记录
     * @param reservationRecord 添加的 [ReservationRecord] 实例
     */
    public fun add(reservationRecord: ReservationRecord)

    /**
     * 删除 [ReservationRecord] 记录
     * @param reservationRecord 删除的 [ReservationRecord] 实例
     */
    public fun delete(reservationRecord: ReservationRecord)

    /**
     * 修改 [ReservationRecord] 记录
     * @param reservationRecord 修改的 [ReservationRecord] 实例
     */
    public fun update(reservationRecord: ReservationRecord)

    /**
     * 添加或修改 [ReservationRecord] 记录
     * @param reservationRecord 添加或修改的 [ReservationRecord] 实例
     */
    public fun addOrUpdate(reservationRecord: ReservationRecord)

    /**
     * 修改多条 [ReservationRecord] 记录
     * @param reservationRecords 修改的 [ReservationRecord] 实例集合
     */
    public fun updateList(reservationRecords: List<ReservationRecord>)

    /**
     * 获取所有 [ReservationRecord] 记录
     * @return 所有 [ReservationRecord] 实例
     */
    public fun getAll(): List<ReservationRecord>

    /**
     * 获取指定的 [id] 的 [ReservationRecord] 记录
     * @param id 指定的 [ReservationRecord] 主键
     * @return [ReservationRecord] 记录
     */
    public fun getById(id: String): ReservationRecord?

    /**
     * 获取指定的 [userId] 的 [ReservationRecord] 记录
     * @param userId 指定的 [ReservationRecord] 用户主键
     * @return [ReservationRecord] 记录
     */
    public fun getByUserId(userId: String): List<ReservationRecord>

    /**
     * 获取指定 [status] 的 [ReservationRecord] 记录
     * @param status 指定的 [ReservationRecord] 状态
     *
     */
    public fun getByStatus(status: Int): List<ReservationRecord>

    /**
     *获取指定 [status] 的 [ReservationRecord] 记录
     * @param status 指定的 [ReservationRecord] 状态
     */
    public fun getByStatusList(status: List<Int>): List<ReservationRecord>

    /**
     * 获取指定的 [meetingRoomId] 的 [ReservationRecord] 记录
     * @param meetingRoomId 指定的 [ReservationRecord] 会议室主键
     * @return [ReservationRecord] 记录
     */
    public fun getByMeetingRoomId(meetingRoomId: String): List<ReservationRecord>

    /**
     * 获取指定的 [start] 和 [end] 的 [ReservationRecord] 记录
     * @param start 指定的开始时间
     * @param end 指定的结束时间
     * @return [ReservationRecord] 记录
     */
    public fun getByDateTime(@Param("start")start: Date, @Param("end")end: Date): List<ReservationRecord>

    /**
     * 获取指定的 [meetingRoomId], [start] 和 [end] 的 [ReservationRecord] 记录
     * @param meetingRoomId 指定的会议室
     * @param start 指定的开始时间
     * @param end 指定的结束时间
     * @return [ReservationRecord] 记录
     */
    public fun getByMeetingRoomIdDateTime(@Param("meetingRoomId")meetingRoomId: String, @Param("start")start: Date, @Param("end")end: Date): List<ReservationRecord>
}