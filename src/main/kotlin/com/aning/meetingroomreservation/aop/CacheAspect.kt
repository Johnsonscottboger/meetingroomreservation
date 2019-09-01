package com.aning.meetingroomreservation.aop

import com.aning.meetingroomreservation.annotation.cache.*
import com.aning.meetingroomreservation.extension.takeIf
import com.aning.meetingroomreservation.cache.ICacheService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.expression.Expression
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

/**
 * 缓存切面
 */
@Aspect
@Component
public class CacheAspect {
    companion object {
        private const val DEFAULT_EXPIRES = 60 * 60 * 1000

        private const val cacheGet = "cacheGet()"
        private const val cacheGetOrAdd = "cacheGetOrAdd()"
        private const val cacheAdd = "cacheAdd()"
        private const val cacheAddOrUpdate = "cacheAddOrUpdate()"
        private const val cacheRemove = "cacheRemove()"
    }

    @Autowired
    private lateinit var _cacheService: ICacheService<Any>
    private val _expressionCache = ConcurrentHashMap<String, Expression>()

    @Pointcut("@annotation(com.aning.meetingroomreservation.annotation.cache.CacheGet)")
    public fun cacheGet() {
    }

    @Pointcut("@annotation(com.aning.meetingroomreservation.annotation.cache.CacheGetOrAdd)")
    public fun cacheGetOrAdd() {
    }

    @Pointcut("@annotation(com.aning.meetingroomreservation.annotation.cache.CacheAdd)")
    public fun cacheAdd() {
    }

    @Pointcut("@annotation(com.aning.meetingroomreservation.annotation.cache.CacheAddOrUpdate)")
    public fun cacheAddOrUpdate() {
    }

    @Pointcut("@annotation(com.aning.meetingroomreservation.annotation.cache.CacheRemove)")
    public fun cacheRemove() {
    }

    /**
     * 优先获取指定目标的缓存, 如果没有, 则返回目标的结果
     */
    @Around(cacheGet)
    public fun doCacheGet(joinPoint: ProceedingJoinPoint): Any? {
        val annotation = this.getAnnotation<CacheGet>(joinPoint)
        val key = this.getKey(joinPoint, annotation)
        if (key.isEmpty())
            return joinPoint.proceed(joinPoint.args)
        return this._cacheService.get(key) ?: joinPoint.proceed(joinPoint.args)
    }

    /**
     * 优先获取指定目标的缓存
     */
    @Around(cacheGetOrAdd)
    public fun doCacheGetOrAdd(joinPoint: ProceedingJoinPoint): Any? {
        val annotation = this.getAnnotation<CacheGetOrAdd>(joinPoint)
        val key = this.getKey(joinPoint, annotation)
        if (key.isEmpty())
            return joinPoint.proceed(joinPoint.args)
        return _cacheService.getOrAdd(key = key, valueFactory = {
            joinPoint.proceed(joinPoint.args)
        }, expire = annotation.expire)
    }

    /**
     * 将目标结果添加到缓存中
     */
    @Around(cacheAdd)
    public fun doCacheAdd(joinPoint: ProceedingJoinPoint): Any? {
        val annotation = this.getAnnotation<CacheAdd>(joinPoint)
        val key = this.getKey(joinPoint, annotation)
        val result = joinPoint.proceed(joinPoint.args)
        if (key.isNotEmpty())
            this._cacheService.add(key, result, annotation.expire)
        return result
    }

    /**
     * 将目标结果添加到缓存中, 如果缓存已存在则更新
     */
    @Around(cacheAddOrUpdate)
    public fun doCacheAddOrUpdate(joinPoint: ProceedingJoinPoint): Any?{
        val annotation = this.getAnnotation<CacheAddOrUpdate>(joinPoint)
        val key = this.getKey(joinPoint, annotation)
        val result = joinPoint.proceed(joinPoint.args)
        if(key.isNotEmpty())
            this._cacheService.addOrUpdate(key, result, result, annotation.expire)
        return result
    }

    /**
     * 获取当前切点的注解实例
     */
    private inline fun <reified TAnnotation : Annotation> getAnnotation(joinPoint: ProceedingJoinPoint): TAnnotation {
        val methodSignature = joinPoint.signature as MethodSignature
        return methodSignature.method.getAnnotation(TAnnotation::class.java)
    }

    /**
     * 获取当前切点的键
     */
    private inline fun <reified TAnnotation> getKey(joinPoint: ProceedingJoinPoint, annotation: TAnnotation): String {
        val isNotEmpty: (String) -> Boolean = { p -> p.isNotEmpty() }
        val keyExpression = when (annotation) {
            is CacheGet -> CacheGet::key.get(annotation).takeIf(isNotEmpty, CacheGet::keyGenerator.get(annotation))
            is CacheGetOrAdd -> CacheGetOrAdd::key.get(annotation).takeIf(isNotEmpty, CacheGetOrAdd::keyGenerator.get(annotation))
            is CacheAdd -> CacheAdd::key.get(annotation).takeIf(isNotEmpty, CacheAdd::keyGenerator.get(annotation))
            is CacheAddOrUpdate -> CacheAddOrUpdate::key.get(annotation).takeIf(isNotEmpty, CacheAddOrUpdate::keyGenerator.get(annotation))
            is CacheRemove -> CacheRemove::key.get(annotation).takeIf(isNotEmpty, CacheRemove::keyGenerator.get(annotation))
            else -> ""
        }
        if (keyExpression.isEmpty())
            return ""
        else {
            val parser = SpelExpressionParser()
            val context = StandardEvaluationContext()
            val methodSignature = joinPoint.signature as MethodSignature
            methodSignature.parameterNames.forEachIndexed { index, parameterName ->
                context.setVariable(parameterName, joinPoint.args[index])
            }
            val expression = this._expressionCache[keyExpression] ?: this.run {
                val ep = parser.parseExpression(keyExpression)
                this._expressionCache[keyExpression] = ep
                ep
            }
            val value = expression.getValue(context)
            return value?.toString() ?: ""
        }
    }
}