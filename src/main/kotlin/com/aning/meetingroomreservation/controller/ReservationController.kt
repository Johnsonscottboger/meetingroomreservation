package com.aning.meetingroomreservation.controller

import com.aning.meetingroomreservation.annotation.AllowAnonymous
import com.aning.meetingroomreservation.entity.ReservationRecord
import com.aning.meetingroomreservation.model.ReservationRecordComparator
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
    private val initialize : Unit by lazy {
        this._reserveService.initialize()
    }

    @Autowired
    private lateinit var _userService: IUserService

    @Autowired
    private lateinit var _meetingRoomService: IMeetingRoomService

    @Autowired
    private lateinit var _reserveService: IReservationService

    /**
     * 首页
     */
    @AllowAnonymous
    @GetMapping("/index")
    public fun index(): String {
        print(this.initialize)
        return "reservation/index"
    }

    /**
     * 获取今天的会议室预约记录
     */
    @AllowAnonymous
    @ResponseBody
    @GetMapping("")
    public fun getReservationRecordToday(@RequestParam("date") date: Date?): Json {
        val operation = "今日会议室预约记录"
        return try {

            //刷新会议室状态
            //this._reserveService.updateMeetingRoomReservationStatus()

            val now = Calendar.getInstance()
            if(date != null){
                now.time = date
            }
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
                val now = Calendar.getInstance()
                val start = Calendar.getInstance()
                start.time = reservationRecord.startTime
                start.add(Calendar.MINUTE, 1)
                if (start.before(now))
                    return Json.fail(operation, message = "开始时间无效")

                val end = Calendar.getInstance()
                end.time = reservationRecord.endTime
                end.add(Calendar.MINUTE, 1)
                if (end.before(start))
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