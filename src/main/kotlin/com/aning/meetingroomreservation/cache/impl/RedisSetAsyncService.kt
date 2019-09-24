package com.aning.meetingroomreservation.cache.impl

import com.aning.meetingroomreservation.cache.ICacheSetAsyncService
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SetOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

/**
 * 使用 Redis 集合实现的异步缓存服务
 */
@Service
public class RedisSetAsyncService<T : Any> : ICacheSetAsyncService<T> {
    private val log = LoggerFactory.getLogger(this::class.java)
    @Resource
    private lateinit var redisTemplate: RedisTemplate<String, T>

    private val operations: SetOperations<String, T> by lazy {
        this.redisTemplate.setEnableTransactionSupport(true)
        this.redisTemplate.opsForSet()
    }

    /**
     * Spring 实现的异步
     */
    @Async
    override fun getOrAddRangeSpringAsync(key: String, valuesFactory: (String) -> Iterable<T>, expire: Long?): Set<T> {
        if (!this.redisTemplate.hasKey(key)) {
            val values = valuesFactory(key)
            this.redisTemplate.multi()
            for (value in values) {
                this.operations.add(key, value)
            }
            expire?.run {
                this@RedisSetAsyncService.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
            }
            this.redisTemplate.exec()
            return values.toSet()
        }
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valuesFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override suspend fun getOrAddRangeAsync(key: String, valuesFactory: (String) -> Iterable<T>, expire: Long?): Deferred<Set<T>> = GlobalScope.async(Dispatchers.IO) {
        if (!this@RedisSetAsyncService.redisTemplate.hasKey(key)) {
            val values = valuesFactory(key)
            this@RedisSetAsyncService.redisTemplate.multi()
            for (value in values) {
                this@RedisSetAsyncService.operations.add(key, value)
            }
            expire?.run {
                this@RedisSetAsyncService.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
            }
            this@RedisSetAsyncService.redisTemplate.exec()
            return@async values.toSet()
        }
        return@async this@RedisSetAsyncService.operations.members(key)?.toSet() ?: emptySet()
    }
}