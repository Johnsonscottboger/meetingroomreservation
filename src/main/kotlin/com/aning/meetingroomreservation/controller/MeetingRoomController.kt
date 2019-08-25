package com.aning.meetingroomreservation.controller

import com.aning.meetingroomreservation.annotation.AllowAnonymous
import com.aning.meetingroomreservation.entity.MeetingRoom
import com.aning.meetingroomreservation.service.IMeetingRoomService
import com.aning.meetingroomreservation.viewmodel.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.Exception

/**
 * 会议室管理控制器
 */
@Controller
@RequestMapping("/meetingroom")
public class MeetingRoomController {
    @Autowired
    private lateinit var _service: IMeetingRoomService

    /**
     * 首页
     */
    @AllowAnonymous
    @GetMapping("/index")
    public fun index(): String {
        return "meetingroom/index"
    }

    /**
     * 获取所有会议室
     */
    @AllowAnonymous
    @ResponseBody
    @GetMapping("")
    public fun getMeetingRoomList(@RequestParam("name") name: String?): Json {
        val operation = "获取会议室列表"
        return try {
            val meetingRooms = if (name.isNullOrBlank())
                this._service.getAll()
            else
                this._service.getByName(name!!)
            Json.succ(operation, data = meetingRooms)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }

    /**
     * 获取会议室
     */
    @ResponseBody
    @GetMapping("/{id}")
    public fun getMeetingRoom(@PathVariable("id") id: String): Json {
        val operation = "获取会议室"
        return try {
            val meetingRoom = this._service.getById(id)
            if (meetingRoom == null) {
                Json.fail(operation, message = "会议室不存在")
            } else {
                Json.succ(operation, data = meetingRoom)
            }
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }

    /**
     * 添加或更新会议室
     */
    @ResponseBody
    @RequestMapping("", method = [RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH])
    public fun addOrUpdateMeetingRoom(request: HttpServletRequest, @RequestBody meetingRoom: MeetingRoom): Json {
        val operation = "添加或更新会议室"
        if (meetingRoom.name.isBlank())
            return Json.fail(operation, message = "会议室名称不能为空")
        return try {
            if (meetingRoom.id.isBlank())
                meetingRoom.id = UUID.randomUUID().toString()
            this._service.addOrUpdate(meetingRoom)
            Json.succ(operation)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }

    ///删除会议室
    @ResponseBody
    @RequestMapping("", method = [RequestMethod.DELETE])
    public fun deleteMeetingRoom(request: HttpServletRequest, meetingRoom: MeetingRoom): Json {
        val operation = "删除会议室"
        return try {
            this._service.delete(meetingRoom)
            Json.succ(operation)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }
}