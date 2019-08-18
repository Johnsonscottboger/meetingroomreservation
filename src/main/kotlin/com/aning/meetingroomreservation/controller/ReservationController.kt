package com.aning.meetingroomreservation.controller

import com.aning.meetingroomreservation.entity.ReservationRecord
import com.aning.meetingroomreservation.model.ReservationStatus
import com.aning.meetingroomreservation.service.IMeetingRoomService
import com.aning.meetingroomreservation.service.IReservationService
import com.aning.meetingroomreservation.service.IUserService
import com.aning.meetingroomreservation.util.IPUtil
import com.aning.meetingroomreservation.viewmodel.Json
import com.aning.meetingroomreservation.viewmodel.ReservationRecordViewModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.Comparator

@Controller
@RequestMapping("/reserve")
public class ReservationController {
    @Autowired
    private lateinit var _userService: IUserService

    @Autowired
    private lateinit var _meetingRoomService: IMeetingRoomService

    @Autowired
    private lateinit var _reserveService: IReservationService

    /**
     * 首页
     */
    @GetMapping("/index")
    public fun index(): String {
        return "reservation/index"
    }

    /**
     * 获取今天的会议室预约记录
     */
    @ResponseBody
    @GetMapping("")
    public fun getReservationRecordToday(): Json {
        val operation = "今日会议室预约记录"
        return try {

            //刷新会议室状态
            this._reserveService.updateMeetingRoomReservationStatus()

            val now = Calendar.getInstance()
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
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val timeFormat = SimpleDateFormat("HH:mm")
            val records = this._reserveService.getByDateTime(start, end)
            val meetingRooms = this._meetingRoomService.getAll()
            val result = records.sortedWith(ReservationRecordComparator())
                    .map { p ->
                        return@map ReservationRecordViewModel(
                                id = p.id,
                                ip = p.ip,
                                userId = p.userId,
                                meetingRoomId = p.meetingRoomId,
                                date = dateFormat.format(p.startTime),
                                startTime = timeFormat.format(p.startTime),
                                endTime = timeFormat.format(p.endTime),
                                status = p.status,
                                statusDesc = ReservationStatus.valueOf(p.status).desc,
                                name = meetingRooms.firstOrNull() { c -> c.id == p.meetingRoomId }?.name,
                                comments = p.comments
                        )
                    }
            Json.succ(operation, data = result)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }

    /**
     * 预约会议室
     */
    @ResponseBody
    @RequestMapping("", method = [RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH])
    public fun reserve(request: HttpServletRequest, @RequestBody reservationRecord: ReservationRecord): Json {
        val operation = "预约会议室"
        return try {
            if(reservationRecord.status != ReservationStatus.CANCEL.value) {
                if (reservationRecord.userId.isBlank())
                    return Json.fail(operation, message = "未指定预约人")
                if (reservationRecord.meetingRoomId.isBlank())
                    return Json.fail(operation, message = "未指定预约的会议室")
                val now = Date()
                if (reservationRecord.startTime.before(now))
                    return Json.fail(operation, message = "开始时间无效")
                if (reservationRecord.endTime.before(reservationRecord.startTime))
                    return Json.fail(operation, message = "结束时间无效")
                val record = this._reserveService.getByMeetingRoomIdDateTime(reservationRecord.meetingRoomId, reservationRecord.startTime, reservationRecord.endTime)
                if (record.any { p -> p.id != reservationRecord.id && p.status != ReservationStatus.CANCEL.value })
                    return Json.fail(operation, message = "指定时间段内的会议室已被占用")
            }
            if (reservationRecord.id.isEmpty())
                reservationRecord.id = UUID.randomUUID().toString()
            reservationRecord.ip = IPUtil.getIPAddress(request)
            this._reserveService.addOrUpdate(reservationRecord)
            Json.succ(operation)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }
}

/**
 * 预约记录比较器
 */
public class ReservationRecordComparator : Comparator<ReservationRecord> {
    override fun compare(o1: ReservationRecord?, o2: ReservationRecord?): Int {
        if (o1 == null && o2 == null)
            return 0
        if (o1 == null)
            return -1
        if (o2 == null)
            return 1
        val queue = arrayOf(ReservationStatus.USING.value, ReservationStatus.UNUSED.value)
        val unqueue = arrayOf(ReservationStatus.CANCEL.value, ReservationStatus.USED.value)
        if (o1.status in queue && o2.status in queue) {
            return o1.startTime.compareTo(o2.startTime)
        }
        if (o1.status in unqueue && o2.status in unqueue) {
            return o1.startTime.compareTo(o2.startTime)
        }
        return if (o1.status in queue) {
            -1
        } else {
            1
        }
    }
}