package com.aning.meetingroomreservation.aop

import eu.bitwalker.useragentutils.UserAgent
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 控制器切面
 */
@Aspect
@Component
public class ControllerAspect {
    companion object {
        private const val loginPointcut = "login()"
        private const val actionPointCut = "action()"
    }

    private val log = LoggerFactory.getLogger("控制器切面")

    @Pointcut("execution(* com.aning.meetingroomreservation.controller.HomeController.login(..))")
    public fun login(){

    }

    /**
     * 动作切点
     */
    @Pointcut("execution(* com.aning.meetingroomreservation.controller..*(..))")
    public fun action() {

    }

    /**
     * 登录执行前
     */
    @Before(loginPointcut)
    public fun loginBefore(joinPoint: JoinPoint){
        val attributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val request = attributes.request
        val userAgent= UserAgent.parseUserAgentString(request.getHeader("User-Agent"))
        this.log.info("Request Url: {}", request.requestURL)
        this.log.info("Browser: {}", userAgent.browser)
        this.log.info("OperatingSystem: {}", userAgent.operatingSystem)
        this.log.info("IP Address: {}", request.remoteAddr)
        this.log.info("Method Type: {}", request.method)
        this.log.info("Target: {}", "${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}")
        this.log.info("Parameters: {}", joinPoint.args.joinToString())
    }

    /**
     * 动作执行前
     */
    @Before(actionPointCut)
    public fun actionBefore(joinPoint: JoinPoint) {
        val attributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val request = attributes.request
        val userAgent= UserAgent.parseUserAgentString(request.getHeader("User-Agent"))
        this.log.info("Request Url: {}", request.requestURL)
        this.log.info("Method Type: {}", request.method)
        this.log.info("Target: {}", "${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}")
        this.log.info("Parameters: {}", joinPoint.args.joinToString())
    }
}