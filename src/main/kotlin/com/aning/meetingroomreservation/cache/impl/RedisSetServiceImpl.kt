package com.aning.meetingroomreservation.cache.impl

import com.aning.meetingroomreservation.cache.ICacheSetService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SetOperations
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.Resource
import javax.security.auth.kerberos.KerberosTicket
import kotlin.math.exp

/**
 * 使用 Redis 集合实现的缓存服务
 */
@Service
public class RedisSetServiceImpl<T : Any> : ICacheSetService<T> {
    @Resource
    private lateinit var redisTemplate: RedisTemplate<String, T>

    private val operations: SetOperations<String, T> by lazy {
        this.redisTemplate.setEnableTransactionSupport(true)
        this.redisTemplate.opsForSet()
    }

    /**
     * 添加指定的值 [value] 到缓存中
     * @param key 指定的键
     * @param value 指定的值
     */
    override fun add(key: String, value: T) {
        this.operations.add(key, value)
    }

    /**
     * 添加指定的值 [value] 到缓存中, 并指定 [expire] 毫秒后过期
     * @param key 指定的键
     * @param value 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun add(key: String, value: T, expire: Long) {
        this.operations.add(key, value)
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
    }

    /**
     * 添加指定的值 [value] 到缓存中, 并指定 [time] 时间后过期
     * @param key 指定的键
     * @param value 指定的值
     * @param time 指定的有效期, 时间
     */
    override fun add(key: String, value: T, time: Duration) {
        this.operations.add(key, value)
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
    }

    /**
     * 批量添加指定的值到缓存的同一个集合中
     * @param key 指定的键
     * @param values 指定的值
     */
    override fun addRange(key: String, values: Iterable<T>) {
        this.redisTemplate.multi()
        for (value in values)
            this.operations.add(key, value)
        this.redisTemplate.exec()
    }

    /**
     * 批量添加指定的值到缓存的同一个集合中
     * @param key 指定的键
     * @param values 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun addRange(key: String, values: Iterable<T>, expire: Long) {
        this.redisTemplate.multi()
        for (value in values)
            this.operations.add(key, value)
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        this.redisTemplate.multi()
    }

    /**
     * 批量添加指定的值到缓存的同一个集合中
     * @param key 指定的键
     * @param values 指定的值
     * @param time 指定的有效期, 时间
     */
    override fun addRange(key: String, values: Iterable<T>, time: Duration) {
        this.redisTemplate.multi()
        for (value in values)
            this.operations.add(key, value)
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
        this.redisTemplate.exec()
    }

    /**
     * 批量添加指定的值到缓存中, 每一个 [Pair] 将被添加到不同的集合中
     * @param pairs 指定的键值对
     */
    override fun addRange(pairs: Map<String, Iterable<T>>) {
        this.redisTemplate.multi()
        for (pair in pairs) {
            for (value in pair.value) {
                this.operations.add(pair.key, value)
            }
        }
        this.redisTemplate.exec()
    }

    /**
     * 批量添加指定的值到缓存中, 每一个 [Pair] 将被添加到不同的集合中
     * @param pairs 指定的键值对
     * @param expire 指定的有效期, 毫秒
     */
    override fun addRange(pairs: Map<String, Iterable<T>>, expire: Long) {
        this.redisTemplate.multi()
        for (pair in pairs) {
            for (value in pair.value) {
                this.operations.add(pair.key, value)
            }
            this.redisTemplate.expire(pair.key, expire, TimeUnit.MILLISECONDS)
        }
        this.redisTemplate.exec()
    }

    /**
     * 批量添加指定的值到缓存中, 每一个 [Pair] 将被添加到不同的集合中
     * @param pairs 指定的键值对
     * @param time 指定的有效期, 时间
     */
    override fun addRange(pairs: Map<String, Iterable<T>>, time: Duration) {
        this.redisTemplate.multi()
        for (pair in pairs) {
            for (value in pair.value) {
                this.operations.add(pair.key, value)
            }
            this.redisTemplate.expire(pair.key, time.toMillis(), TimeUnit.MILLISECONDS)
        }
        this.redisTemplate.exec()
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param value 指定的值
     */
    override fun addOrUpdate(key: String, value: T, comparator: ((T, T) -> Boolean)?) {
        val item = if (comparator == null) value else {
            this.operations.members(key)?.firstOrNull { p -> comparator(p, value) }
        }
        this.redisTemplate.multi()
        item?.run {
            this@RedisSetServiceImpl.operations.remove(key, item)
        }
        this.operations.add(key, value)
        this.redisTemplate.exec()
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param value 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun addOrUpdate(key: String, value: T, expire: Long, comparator: ((T, T) -> Boolean)?) {
        val item = if(comparator == null) value else {
            this.operations.members(key)?.firstOrNull { p -> comparator(p, value) }
        }
        this.redisTemplate.multi()
        item?.run {
            this@RedisSetServiceImpl.operations.remove(key, item)
        }
        this.operations.add(key, value)
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        this.redisTemplate.exec()
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param value 指定的值
     * @param time 指定的有效期, 时间
     */
    override fun addOrUpdate(key: String, value: T, time: Duration, comparator: ((T, T) -> Boolean)?) {
        val item = if (comparator == null) value else {
            this.operations.members(key)?.firstOrNull { p -> comparator(p, value) }
        }
        this.redisTemplate.multi()
        item?.run {
            this@RedisSetServiceImpl.operations.remove(key, item)
        }
        this.operations.add(key, value)
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
        this.redisTemplate.exec()
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param values 指定的值
     */
    override fun addOrUpdateRange(key: String, values: Iterable<T>, comparator: ((T, T) -> Boolean)?) {
        val members = if(comparator == null) emptySet<T>() else {
            this.operations.members(key)
        }
        this.redisTemplate.multi()
        for (value in values) {
            val item = if(comparator == null) value else {
                members?.firstOrNull { p -> comparator(p, value) }
            }
            item?.run {
                this@RedisSetServiceImpl.operations.remove(key, item)
            }
            this.operations.add(key, value)
        }
        this.redisTemplate.exec()
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param values 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun addOrUpdateRange(key: String, values: Iterable<T>, expire: Long, comparator: ((T, T) -> Boolean)?) {
        val members = if(comparator == null) emptySet<T>() else {
            this.operations.members(key)
        }
        this.redisTemplate.multi()
        for (value in values) {
            val item = if(comparator == null) value else {
                members?.firstOrNull { p -> comparator(p, value) }
            }
            item?.run {
                this@RedisSetServiceImpl.operations.remove(key, item)
            }
            this.operations.add(key, value)
        }
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        this.redisTemplate.exec()
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param values 指定的值
     * @param time 指定的有效期, 时间
     */
    override fun addOrUpdateRange(key: String, values: Iterable<T>, time: Duration, comparator: ((T, T) -> Boolean)?) {
        val members = if(comparator == null) emptySet<T>() else {
            this.operations.members(key)
        }
        this.redisTemplate.multi()
        for (value in values) {
            val item = if(comparator == null) value else {
                members?.firstOrNull { p -> comparator(p, value) }
            }
            item?.run {
                this@RedisSetServiceImpl.operations.remove(key, item)
            }
            this.operations.add(key, value)
        }
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
        this.redisTemplate.exec()
    }

    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回空的集合
     * @param key 指定的键
     * @return 缓存中键的值
     */
    override fun get(key: String): Set<T> {
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回空的集合
     * @param keys 指定的键
     * @return 缓存中键的值
     */
    override fun getRange(keys: Iterable<String>): Map<String, Set<T>> {
        return keys.map { key ->
            key to (this.operations.members(key)?.toSet() ?: emptySet())
        }.toMap()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, value: T): Set<T> {
        this.operations.add(key, value)
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, value: T, expire: Long): Set<T> {
        this.operations.add(key, value)
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @param time 指定的有效期, 时间
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, value: T, time: Duration): Set<T> {
        this.operations.add(key, value)
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valueFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, valueFactory: (String) -> T, expire: Long?): Set<T> {
        if (!this.redisTemplate.hasKey(key)) {
            val value = valueFactory(key)
            this.operations.add(key, value)
            expire?.run {
                this@RedisSetServiceImpl.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
            }
            return setOf(value)
        }
        expire?.run {
            this@RedisSetServiceImpl.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        }
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param values 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAddRange(key: String, values: Iterable<T>): Set<T> {
        this.redisTemplate.multi()
        for (value in values)
            this.operations.add(key, value)
        this.redisTemplate.exec()
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param values 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAddRange(key: String, values: Iterable<T>, expire: Long): Set<T> {
        this.redisTemplate.multi()
        for (value in values)
            this.operations.add(key, value)
        this.redisTemplate.exec()
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param values 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param time 指定的有效期, 时间
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAddRange(key: String, values: Iterable<T>, time: Duration): Set<T> {
        this.redisTemplate.multi()
        for (value in values)
            this.operations.add(key, value)
        this.redisTemplate.exec()
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valuesFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAddRange(key: String, valuesFactory: (String) -> Iterable<T>, expire: Long?): Set<T> {
        if (!this.redisTemplate.hasKey(key)) {
            val values = valuesFactory(key)
            this.redisTemplate.multi()
            for (value in values) {
                this.operations.add(key, value)
            }
            expire?.run {
                this@RedisSetServiceImpl.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
            }
            this.redisTemplate.exec()
            return values.toSet()
        }
        return this.operations.members(key)?.toSet() ?: emptySet()
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
     * @param key 指定的键
     * @param value 指定的值
     */
    override fun remove(key: String, value: T) {
        this.operations.remove(key, value)
    }

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     * @param values 指定的值
     */
    override fun removeRange(key: String, values: Iterable<T>) {
        this.redisTemplate.multi()
        for (value in values) {
            this.operations.remove(key, value)
        }
        this.redisTemplate.exec()
    }

    /**
     * 从缓存中删除指定的键
     * @param pairs 指定的键值
     */
    override fun removeRange(pairs: Map<String, Iterable<T>>) {
        this.redisTemplate.multi()
        for (pair in pairs) {
            this.operations.remove(pair.key, pair.value)
        }
        this.redisTemplate.exec()
    }

    /**
     * 获取指定的键在缓存中是否存在
     * @param key 指定的键
     * @return 是否存在
     */
    override fun exists(key: String): Boolean {
        return this.redisTemplate.hasKey(key)
    }

    /**
     * 获取指定的键在缓存中是否存在
     * @param key 指定的键
     * @param value 指定的值
     * @return 是否存在
     */
    override fun exists(key: String, value: T): Boolean {
        return this.operations.isMember(key, value) ?: false
    }
}