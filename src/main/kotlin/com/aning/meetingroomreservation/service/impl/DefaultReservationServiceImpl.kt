package com.aning.meetingroomreservation.service.impl

import com.aning.meetingroomreservation.dao.IReservationDao
import com.aning.meetingroomreservation.entity.ReservationRecord
import com.aning.meetingroomreservation.model.ReservationStatus
import com.aning.meetingroomreservation.service.IReservationService
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource
import kotlin.collections.HashSet

@Service
public class DefaultReservationServiceImpl : IReservationService {

    @Resource
    private lateinit var _dao: IReservationDao

    /**
     * 添加 [ReservationRecord] 记录
     * @param reservationRecord 添加的 [ReservationRecord] 实例
     */
    override fun add(reservationRecord: ReservationRecord) {
        this._dao.add(reservationRecord)
    }

    /**
     * 删除 [ReservationRecord] 记录
     * @param reservationRecord 删除的 [ReservationRecord] 实例
     */
    override fun delete(reservationRecord: ReservationRecord) {
        this._dao.delete(reservationRecord)
    }

    /**
     * 修改 [ReservationRecord] 记录
     * @param reservationRecord 修改的 [ReservationRecord] 实例
     */
    override fun update(reservationRecord: ReservationRecord) {
        this._dao.update(reservationRecord)
    }

    /**
     * 添加或修改 [ReservationRecord] 记录
     * @param reservationRecord 添加或修改的 [ReservationRecord] 实例
     */
    override fun addOrUpdate(reservationRecord: ReservationRecord) {
        this._dao.addOrUpdate(reservationRecord)
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
        return this._dao.getByDateTime(start, end)
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

    /**
     * 更新会议室预约状态, 当前记录中的所有实例
     */
    override fun updateMeetingRoomReservationStatus() {
        val list = this._dao.getByStatusList(listOf(ReservationStatus.UNUSED.value, ReservationStatus.USING.value))
        updateMeetingRoomReservationStatus(list)
    }

    /**
     * 更新会议室预约状态, 指定的 [reservationRecord] 实例
     */
    override fun updateMeetingRoomReservationStatus(reservationRecord: ReservationRecord) {
        updateMeetingRoomReservationStatus(listOf(reservationRecord))
    }

    /**
     * 更新会议室预约状态, 指定的 [reservationRecords] 实例
     */
    override fun updateMeetingRoomReservationStatus(reservationRecords: List<ReservationRecord>) {
        val now = Date()
        /**
         * 会议室预约状态
         * 1. 如果开始时间大于当前时间 -> 未开始
         * 2. 如果开始时间小于等于当前时间 -> 正在使用
         * 3. 如果结束时间大于当前时间 -> 正在使用
         * 4. 如果结束时间小于当前时间 -> 使用结束
         */
        val updateList = HashSet<ReservationRecord>()
        for (record in reservationRecords) {
            if(record.startTime.after(now) && record.status != ReservationStatus.UNUSED.value){
                record.status = ReservationStatus.UNUSED.value
                updateList.add(record)
            }
            if(record.startTime.before(now) && record.status != ReservationStatus.USING.value){
                record.status = ReservationStatus.USING.value
                updateList.add(record)
            }
            if(record.endTime.after(now) && record.status != ReservationStatus.USING.value){
                record.status = ReservationStatus.USING.value
                updateList.add(record)
            }
            if(record.endTime.before(now) && record.status != ReservationStatus.USED.value){
                record.status = ReservationStatus.USED.value
                updateList.add(record)
            }
        }
        try {
            this._dao.updateList(updateList.toList())
        }
        catch (ex: Exception){
            println("更新会议室状态失败!!!")
            ex.printStackTrace()
            throw ex
        }
    }
}