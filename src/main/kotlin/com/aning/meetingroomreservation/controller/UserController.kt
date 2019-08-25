package com.aning.meetingroomreservation.controller

import com.aning.meetingroomreservation.annotation.AllowAnonymous
import com.aning.meetingroomreservation.entity.User
import com.aning.meetingroomreservation.service.IUserService
import com.aning.meetingroomreservation.util.IPUtil
import com.aning.meetingroomreservation.viewmodel.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private lateinit var _service: IUserService

    /**
     * 首页
     */
    @AllowAnonymous
    @GetMapping("/index")
    public fun index(): String {
        return "user/index"
    }

    /**
     * 获取所有用户
     */
    @AllowAnonymous
    @ResponseBody
    @GetMapping("")
    public fun getUserList(@RequestParam("name") name: String?): Json {
        val operation = "获取用户列表"
        return try {
            val users = if (name.isNullOrEmpty())
                this._service.getAll()
            else
                this._service.getByName(name!!)
            Json.succ(operation, data = users)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }

    /**
     * 获取用户
     */
    @ResponseBody
    @GetMapping("/{id}")
    public fun getUser(@PathVariable("id") id: String): Json {
        val operation = "获取用户"
        return try {
            val user = this._service.getById(id)
            if (user == null) {
                Json.fail(operation, message = "用户不存在")
            } else {
                Json.succ(operation, data = user)
            }
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }

    /**
     * 添加或修改用户
     */
    @ResponseBody
    @RequestMapping("", method = [RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH])
    public fun addOrUpdateUser(request: HttpServletRequest, @RequestBody user: User): Json {
        val operation = "添加或修改用户"
        if (user.name.isBlank())
            return Json.fail(operation, message = "用户名不能为空")
        if (user.department.isBlank())
            return Json.fail(operation, message = "用户部门不能为空")
        return try {
            if (user.id.isBlank())
                user.id = UUID.randomUUID().toString()
            user.ip = IPUtil.getIPAddress(request)
            this._service.addOrUpdate(user)
            Json.succ(operation, data = user)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }

    /**
     * 删除用户
     */
    @ResponseBody
    @RequestMapping("", method = [RequestMethod.DELETE])
    public fun deleteUser(request: HttpServletRequest, user: User): Json {
        val operation = "删除用户"
        return try {
            this._service.delete(user)
            Json.succ(operation)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }

    /**
     * 导入用户
     */
    @ResponseBody
    @RequestMapping("/import", method = [RequestMethod.POST])
    public fun importUsers(request: HttpServletRequest, @RequestBody users: List<User>): Json {
        val operation = "导入用户"
        return try {
            val usersExists = this._service.getAll()
            val added = users.filter { p ->
                !usersExists.any { x -> x.name == p.name && x.department == p.department }
            }.map { p ->
                p.id = UUID.randomUUID().toString()
                return@map p
            }
            this._service.addRange(added)
            Json.succ(operation)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }
}