package com.aning.meetingroomreservation.service.impl

import com.aning.meetingroomreservation.service.ICacheService
import com.mysql.cj.util.TimeUtil
import io.netty.util.Timeout
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

/**
 * 使用 Redis 实现的缓存服务
 */
@Service
public class RedisCacheServiceImpl<T : Any> : ICacheService<T> {

    @Resource
    private lateinit var redisTemplate: RedisTemplate<String, T>

    /**
     * 添加指定的值 [value] 到缓存中
     * @param key 指定的键
     * @param value 指定的值
     */
    override fun add(key: String, value: T) {
        this.redisTemplate.opsForValue().set(key, value)
    }

    /**
     * 添加指定的值 [value] 到缓存中, 并指定 [expire] 毫秒后过期
     * @param key 指定的键
     * @param value 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun add(key: String, value: T, expire: Long) {
        this.redisTemplate.opsForValue().set(key, value, expire, TimeUnit.MILLISECONDS)
    }

    /**
     * 添加指定的值 [value] 到缓存中, 并指定 [time] 时间后过期
     * @param key 指定的键
     * @param value 指定的值
     * @param time 指定的有效期, 时间
     */
    override fun add(key: String, value: T, time: Duration) {
        this.redisTemplate.opsForValue().set(key, value, time)
    }

    /**
     * 批量添加指定的值到缓存中
     * @param pairs 指定的键值对集合
     */
    override fun addRange(pairs: Map<String, T>) {
        this.redisTemplate.opsForValue().multiSet(pairs)
    }

    /**
     * 批量添加指定的值到缓存中, 并指定 [expire] 毫秒后过期
     * @param pairs 指定的键值对集合
     * @param expire 指定的有效期, 毫秒
     */
    override fun addRange(pairs: Map<String, T>, expire: Long) {
        this.redisTemplate.opsForValue().multiSet(pairs)
        for (pair in pairs) {
            this.redisTemplate.expire(pair.key, expire, TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 批量添加指定的值到缓存中, 并指定 [time] 时间后过期
     * @param pairs 指定的键值对集合
     * @param time 指定的有效期, 时间
     */
    override fun addRange(pairs: Map<String, T>, time: Duration) {
        this.redisTemplate.opsForValue().multiSet(pairs)
        for (pair in pairs) {
            this.redisTemplate.expire(pair.key, time.toMillis(), TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 批量添加指定的值到缓存中
     * @param pairs 指定的键值对集合
     */
    override fun addRange(pairs: Iterable<Pair<String, T>>) {
        this.redisTemplate.opsForValue().multiSet(pairs.toMap())
    }

    /**
     * 批量添加指定的值到缓存中, 并指定 [expire] 毫秒后过期
     * @param pairs 指定的键值对集合
     * @param expire 指定的有效期, 毫秒
     */
    override fun addRange(pairs: Iterable<Pair<String, T>>, expire: Long) {
        this.redisTemplate.opsForValue().multiSet(pairs.toMap())
        for (pair in pairs) {
            this.redisTemplate.expire(pair.first, expire, TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 批量添加指定的值到缓存中, 并指定 [time] 时间后过期
     * @param pairs 指定的键值对集合
     * @param time 指定的有效期, 时间
     */
    override fun addRange(pairs: Iterable<Pair<String, T>>, time: Duration) {
        this.redisTemplate.opsForValue().multiSet(pairs.toMap())
        for (pair in pairs) {
            this.redisTemplate.expire(pair.first, time.toMillis(), TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     */
    override fun addOrUpdate(key: String, addValue: T, updateValue: T) {
        val r = this.redisTemplate.opsForValue().setIfAbsent(key, addValue)
        if (r == null || r == false) {
            this.redisTemplate.opsForValue().setIfPresent(key, updateValue)
        }
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun addOrUpdate(key: String, addValue: T, updateValue: T, expire: Long) {
        val r = this.redisTemplate.opsForValue().setIfAbsent(key, addValue, expire, TimeUnit.MILLISECONDS)
        if (r == null || r == false) {
            this.redisTemplate.opsForValue().setIfPresent(key, updateValue, expire, TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     * @param time 指定的有效期, 时间
     */
    override fun addOrUpdate(key: String, addValue: T, updateValue: T, time: Duration) {
        val r = this.redisTemplate.opsForValue().setIfAbsent(key, addValue, time)
        if (r == null || r == false) {
            this.redisTemplate.opsForValue().setIfPresent(key, updateValue, time)
        }
    }

    /**
     * 添加或更新指定的值
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValueFactory 指定更新的值生成方法
     */
    override fun addOrUpdate(key: String, addValue: T, updateValueFactory: (String) -> T) {
        val r = this.redisTemplate.opsForValue().setIfAbsent(key, addValue)
        if (r == null || r == false) {
            this.redisTemplate.opsForValue().setIfPresent(key, updateValueFactory(key))
        }
    }

    /**
     * 添加或更新指定的值
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValueFactory 指定更新的值生成方法
     * @param expire 指定的有效期, 毫秒
     */
    override fun addOrUpdate(key: String, addValue: T, updateValueFactory: (String) -> T, expire: Long) {
        val r = this.redisTemplate.opsForValue().setIfAbsent(key, addValue, expire, TimeUnit.MILLISECONDS)
        if (r == null || r == false) {
            this.redisTemplate.opsForValue().setIfPresent(key, updateValueFactory(key), expire, TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 添加或更新指定的值
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValueFactory 指定更新的值生成方法
     * @param time 指定的有效期, 时间
     */
    override fun addOrUpdate(key: String, addValue: T, updateValueFactory: (String) -> T, time: Duration) {
        val r = this.redisTemplate.opsForValue().setIfAbsent(key, addValue, time)
        if (r == null || r == false) {
            this.redisTemplate.opsForValue().setIfPresent(key, updateValueFactory(key), time)
        }
    }

    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回`null`
     * @param key 指定的键
     * @return 缓存中键的值, 或`null`
     */
    override fun get(key: String): T? {
        return this.redisTemplate.opsForValue().get(key)
    }

    /**
     * 根据指定的键, 获取对应的值, 只返回存在的值
     * @param keys 指定的键
     * @return 缓存中键的值
     */
    override fun getRange(keys: Iterable<String>): List<T> {
        val result = this.redisTemplate.opsForValue().multiGet(keys.toList())
        return result?.filterNotNull() ?: listOf()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, value: T): T? {
        this.redisTemplate.opsForValue().setIfAbsent(key, value)
        return this.redisTemplate.opsForValue().get(key)
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, value: T, expire: Long): T? {
        this.redisTemplate.opsForValue().setIfAbsent(key, value, expire, TimeUnit.MILLISECONDS)
        return this.redisTemplate.opsForValue().get(key)
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @param time 指定的有效期, 时间
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, value: T, time: Duration): T? {
        this.redisTemplate.opsForValue().setIfAbsent(key, value, time)
        return this.redisTemplate.opsForValue().get(key)
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valueFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, valueFactory: (String) -> T): T? {
        val result = this.redisTemplate.opsForValue().get(key)
        return result ?: this.run {
            val new = valueFactory(key)
            this.redisTemplate.opsForValue().setIfAbsent(key, new)
            new
        }
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valueFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, valueFactory: (String) -> T, expire: Long): T? {
        val result = this.redisTemplate.opsForValue().get(key)
        return result ?: this.run {
            val new = valueFactory(key)
            this.redisTemplate.opsForValue().setIfAbsent(key, new, expire, TimeUnit.MILLISECONDS)
            new
        }
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valueFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param time 指定的有效期, 时间
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, valueFactory: (String) -> T, time: Duration): T? {
        val result = this.redisTemplate.opsForValue().get(key)
        return result ?: this.run {
            val new = valueFactory(key)
            this.redisTemplate.opsForValue().setIfAbsent(key, new, time)
            new
        }
    }

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     */
    override fun remove(key: String) {
        this.redisTemplate.delete(key)
    }

    /**
     * 从缓存中删除指定的键
     * @param keys 指定的键
     */
    override fun removeRange(keys: Iterable<String>) {
        this.redisTemplate.delete(keys.toList())
    }

    /**
     * 获取指定的键在缓存中是否存在
     * @param key 指定的键
     * @return 指定的键是否存在
     */
    override fun exists(key: String): Boolean {
        return this.redisTemplate.hasKey(key)
    }
}