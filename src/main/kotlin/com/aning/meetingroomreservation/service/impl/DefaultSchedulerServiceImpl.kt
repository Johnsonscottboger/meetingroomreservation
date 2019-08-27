package com.aning.meetingroomreservation.service.impl

import com.aning.meetingroomreservation.dao.IReservationDao
import com.aning.meetingroomreservation.entity.ReservationRecord
import com.aning.meetingroomreservation.model.ReservationStatus
import com.aning.meetingroomreservation.service.ISchedulerService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.Resource

/**
 * 使用 [java.util.Timer] 实现的定时任务
 */
@Service
public class DefaultSchedulerServiceImpl : ISchedulerService {
    companion object {
        private val log = LoggerFactory.getLogger(DefaultSchedulerServiceImpl::class.java)
        private val timer = Timer("会议室预约定时器")
        private val taskMap = ConcurrentHashMap<String, TimerTask>()
        private val recordMap = ConcurrentHashMap<String, ReservationRecord>()
    }

    @Resource
    private lateinit var _dao: IReservationDao

    /**
     * 初始化, 加载历史数据添加到定时任务
     */
    override fun initialize() {
        log.info("加载历史数据")
        val reservationRecords = this._dao.getByStatusList(listOf(
                ReservationStatus.UNUSED.value,
                ReservationStatus.USING.value
        ))
        for (reservationRecord in reservationRecords){
            add(reservationRecord)
        }
    }

    /**
     * 添加 [ReservationRecord] 记录
     * @param reservationRecord 添加的 [ReservationRecord] 实例
     */
    override fun add(reservationRecord: ReservationRecord) {
        recordMap[reservationRecord.id] = reservationRecord
        val task = createTimerTask(reservationRecord)
        taskMap[reservationRecord.id] = task
        timer.schedule(task, reservationRecord.startTime)
    }

    /**
     * 删除 [ReservationRecord] 记录
     * @param reservationRecord 删除的 [ReservationRecord] 实例
     */
    override fun delete(reservationRecord: ReservationRecord) {
        recordMap.remove(reservationRecord.id)
        taskMap.remove(reservationRecord.id)?.cancel()
    }

    /**
     * 修改 [ReservationRecord] 记录
     * @param reservationRecord 修改的 [ReservationRecord] 实例
     */
    override fun update(reservationRecord: ReservationRecord) {
        if(reservationRecord.status == ReservationStatus.CANCEL.value){
            delete(reservationRecord)
        } else {
            recordMap.replace(reservationRecord.id, reservationRecord)
            val task = createTimerTask(reservationRecord)
            taskMap.replace(reservationRecord.id, task)?.cancel()
            timer.schedule(task, reservationRecord.startTime)
        }
    }

    /**
     * 添加或修改 [ReservationRecord] 记录
     * @param reservationRecord 添加或修改的 [ReservationRecord] 实例
     */
    override fun addOrUpdate(reservationRecord: ReservationRecord) {
        val record = recordMap[reservationRecord.id]
        if (record == null) {
            recordMap[reservationRecord.id] = reservationRecord
        } else {
            if(reservationRecord.status == ReservationStatus.CANCEL.value){
                recordMap.remove(reservationRecord.id)
            } else {
                recordMap.replace(reservationRecord.id, reservationRecord)
            }
        }

        val newTask = createTimerTask(reservationRecord)
        val task = taskMap[reservationRecord.id]
        if (task == null) {
            taskMap[reservationRecord.id] = newTask
        } else {
            if(reservationRecord.status == ReservationStatus.CANCEL.value){
                taskMap.remove(reservationRecord.id)?.cancel()
                return
            } else {
                taskMap.replace(reservationRecord.id, newTask)?.cancel()
            }
        }
        timer.schedule(newTask, reservationRecord.startTime)
    }

    /**
     * 创建 [TimerTask] 实例
     */
    private fun createTimerTask(reservationRecord: ReservationRecord): TimerTask {
        return object : TimerTask() {
            override fun run() {
                log.info("刷新会议室状态", reservationRecord)
                val record = recordMap[reservationRecord.id]
                if (record != null) {
                    val result = this@DefaultSchedulerServiceImpl.updateMeetingRoomReservationStatus(record)
                    if(result.status == ReservationStatus.USING.value){
                           timer.schedule(createTimerTask(result), result.endTime)
                    }
                }
                this.cancel()
            }
        }
    }

    /**
     * 更新会议室预约状态, 指定的 [reservationRecord] 实例
     * @return 更新后的会议室记录
     */
    private fun updateMeetingRoomReservationStatus(reservationRecord: ReservationRecord): ReservationRecord {
        updateMeetingRoomReservationStatus(listOf(reservationRecord))
        return reservationRecord
    }

    /**
     * 更新会议室预约状态, 指定的 [reservationRecords] 实例
     * 更新后的会议室记录
     */
    private fun updateMeetingRoomReservationStatus(reservationRecords: List<ReservationRecord>): List<ReservationRecord> {
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
            if (record.startTime.after(now)) {
                if (record.status != ReservationStatus.UNUSED.value) {
                    record.status = ReservationStatus.UNUSED.value
                    updateList.add(record)
                }
                continue
            }
            if (record.endTime.after(now)) {
                if (record.status != ReservationStatus.USING.value) {
                    record.status = ReservationStatus.USING.value
                    updateList.add(record)
                }
                continue
            }
            if (record.endTime.before(now)) {
                if (record.status != ReservationStatus.USED.value) {
                    record.status = ReservationStatus.USED.value
                    updateList.add(record)
                }
                continue
            }
        }
        try {
            if (updateList.isNotEmpty()) {
                this._dao.updateList(updateList.toList())
            }
        } catch (ex: Exception) {
            println("更新会议室状态失败!!!")
            ex.printStackTrace()
            throw ex
        }
        return reservationRecords
    }
}