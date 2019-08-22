package com.aning.meetingroomreservation.controller

import com.aning.meetingroomreservation.entity.User
import com.aning.meetingroomreservation.service.IUserService
import com.aning.meetingroomreservation.util.IPUtil
import com.aning.meetingroomreservation.viewmodel.Json
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * 主页控制器
 */
@Controller
public class HomeController {
    @Autowired
    private lateinit var _userService: IUserService

    private val log = LoggerFactory.getLogger(HomeController::class.java)

    /**
     * 首页
     */
    @GetMapping("/")
    public fun index(): String {
        log.info("Application Running...")
        return "index";
    }

    /**
     * 登录
     */
    @ResponseBody
    @GetMapping("/login")
    public fun login(request: HttpServletRequest): Json {
        val operation = "登录"
        return try {
            val ip = IPUtil.getIPAddress(request)
            val user = this._userService.getByIP(ip)
            if (user == null) {
                Json.fail(operation, message = "未登录")
            } else {
                Json.succ(operation, data = user)
            }
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }

    /**
     * 登录
     */
    @ResponseBody
    @PostMapping("/login")
    public fun login(request: HttpServletRequest, @RequestBody user: User): Json {
        val operation = "登录"
        return try {
            val current = this._userService.getById(user.id)
            if (current == null) {
                Json.fail(operation, message = "当前用户不存在")
            } else {
                if (current.ip.isNotEmpty()) {
                    Json.fail(operation, message = "当前用户已绑定")
                } else {
                    current.ip = IPUtil.getIPAddress(request)
                    this._userService.addOrUpdate(current)
                    Json.succ(operation, data = current)
                }
            }
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }
}