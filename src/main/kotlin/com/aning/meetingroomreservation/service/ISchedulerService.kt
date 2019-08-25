package com.aning.meetingroomreservation.service

import com.aning.meetingroomreservation.entity.ReservationRecord

/**
 * 定时任务服务, 定时更新会议室预约状态
 */
public interface ISchedulerService {

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
}