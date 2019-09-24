package com.aning.meetingroomreservation.controller

import com.aning.meetingroomreservation.exception.UnauthorizedException
import com.aning.meetingroomreservation.viewmodel.Json
import org.apache.tomcat.util.http.fileupload.FileUploadBase
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartException

/**
 * 异常处理控制器
 */
@ControllerAdvice
public class ExceptionController {

    /**
     * 处理 [UnauthorizedException] 异常
     */
    @ResponseBody
    @ExceptionHandler(UnauthorizedException::class)
    public fun handle401(): Json {
        return Json.fail("认证", code = 401, message = "没有权限")
    }

    @ResponseBody
    @ExceptionHandler(MultipartException::class)
    public fun handleFileSizeOutOfLimit() : Json {
        return Json.fail("文件", code = 500, message = "文件超出限制")
    }
}