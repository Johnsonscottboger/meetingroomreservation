package com.aning.meetingroomreservation.controller

import com.aning.meetingroomreservation.entity.ReservationRecord
import com.aning.meetingroomreservation.model.ReservationStatus
import com.aning.meetingroomreservation.service.IMeetingRoomService
import com.aning.meetingroomreservation.service.IReservationService
import com.aning.meetingroomreservation.service.IUserService
import com.aning.meetingroomreservation.util.IPUtil
import com.aning.meetingroomreservation.viewmodel.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("reserve")
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
            val records = this._reserveService.getByDateTime(start, end)
            Json.succ(operation, data = records)
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
            if (record.any { p -> p.status != ReservationStatus.CANCEL.value })
                return Json.fail(operation, message = "指定时间段内的会议室已被占用")
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