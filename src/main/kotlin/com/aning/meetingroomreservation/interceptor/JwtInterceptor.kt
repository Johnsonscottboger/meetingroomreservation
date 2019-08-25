package com.aning.meetingroomreservation.interceptor

import com.aning.meetingroomreservation.annotation.AllowAnonymous
import com.aning.meetingroomreservation.exception.UnauthorizedException
import com.aning.meetingroomreservation.util.JwtTokenUtil
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * JWT 拦截器
 */
public class JwtInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"))
        response.setHeader("Access-control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE")
        response.setHeader("Access-control-Allow-Headers", request.getHeader("Access-control-request-Headers"))
        if (request.method.equals(RequestMethod.OPTIONS.name)) {
            response.status = HttpStatus.OK.value()
            return false
        }
        if (handler !is HandlerMethod)
            return true
        val allowAnonymous = handler.getMethodAnnotation(AllowAnonymous::class.java)
        if (allowAnonymous != null)
            return true
        val header = request.getHeader(JwtTokenUtil.AUTH_HEADER) ?: throw UnauthorizedException()
        val token = (if (header.length <= JwtTokenUtil.TOKEN_PREFIX.length) null else header.substring(JwtTokenUtil.TOKEN_PREFIX.length))
                ?: throw  UnauthorizedException()
        val userId = JwtTokenUtil.getUserId(token) ?: throw UnauthorizedException()
        val userName = JwtTokenUtil.getUserName(token) ?: throw UnauthorizedException()
        if (!JwtTokenUtil.verify(token, userName, userId))
            throw  UnauthorizedException()
        return true
    }
}