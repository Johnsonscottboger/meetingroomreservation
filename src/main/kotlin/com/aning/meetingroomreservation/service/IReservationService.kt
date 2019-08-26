package com.aning.meetingroomreservation.service

import com.aning.meetingroomreservation.entity.ReservationRecord
import java.util.*

/**
 * 提供对 [ReservationRecord] 实体操作服务
 */
public interface IReservationService {
    /**
     * 初始化, 加载历史数据添加到定时任务
     */
    public fun initialize()

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
    public fun getByDateTime(start: Date, end : Date): List<ReservationRecord>

    /**
     * 获取指定的 [meetingRoomId], [start] 和 [end] 的 [ReservationRecord] 记录
     * @param meetingRoomId 指定的会议室
     * @param start 指定的开始时间
     * @param end 指定的结束时间
     * @return [ReservationRecord] 记录
     */
    public fun getByMeetingRoomIdDateTime(meetingRoomId: String, start: Date, end: Date): List<ReservationRecord>

    /**
     * 更新会议室预约状态, 当前记录中的所有实例
     */
    public fun updateMeetingRoomReservationStatus()

    /**
     * 更新会议室预约状态, 指定的 [reservationRecord] 实例
     */
    public fun updateMeetingRoomReservationStatus(reservationRecord: ReservationRecord)

    /**
     * 更新会议室预约状态, 指定的 [reservationRecords] 实例
     */
    public fun updateMeetingRoomReservationStatus(reservationRecords: List<ReservationRecord>)
}