package com.aning.meetingroomreservation.service.impl

import com.aning.meetingroomreservation.entity.ReservationRecord
import com.aning.meetingroomreservation.service.IReservationCacheService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SetOperations
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

/**
 * [ReservationRecord] 默认会议室预约缓存服务实现
 */
@Service
public class DefaultReservationCacheServiceImpl : IReservationCacheService {

    @Resource
    private lateinit var redisTemplate: RedisTemplate<String, ReservationRecord>

    private val operations : SetOperations<String, ReservationRecord> by lazy {
        this.redisTemplate.stringSerializer = RedisSerializer.string()
        this.redisTemplate.keySerializer = RedisSerializer.string()
        this.redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(ReservationRecord::class.java)
        this.redisTemplate.hashKeySerializer = RedisSerializer.string()
        this.redisTemplate.hashValueSerializer = Jackson2JsonRedisSerializer(ReservationRecord::class.java)
        this.redisTemplate.opsForSet()
    }

    /**
     * 添加指定的值 [value] 到缓存中
     * @param key 指定的键
     * @param value 指定的值
     */
    override fun add(key: String, value: ReservationRecord) {
        this.operations.add(key, value)
    }

    /**
     * 添加指定的值 [value] 到缓存中, 并指定 [expire] 毫秒后过期
     * @param key 指定的键
     * @param value 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun add(key: String, value: ReservationRecord, expire: Long) {
        this.operations.add(key, value)
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
    }

    /**
     * 添加指定的值 [value] 到缓存中, 并指定 [time] 时间后过期
     * @param key 指定的键
     * @param value 指定的值
     * @param time 指定的有效期, 时间
     */
    override fun add(key: String, value: ReservationRecord, time: Duration) {
        this.operations.add(key, value)
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
    }

    /**
     * 批量添加指定的值到缓存的同一个集合中
     * @param key 指定的键
     * @param values 指定的值
     */
    override fun addRange(key: String, values: Iterable<ReservationRecord>) {
        for (value in values)
            this.operations.add(key, value)
    }

    /**
     * 批量添加指定的值到缓存的同一个集合中
     * @param key 指定的键
     * @param values 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun addRange(key: String, values: Iterable<ReservationRecord>, expire: Long) {
        for (value in values)
            this.operations.add(key, value)
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
    }

    /**
     * 批量添加指定的值到缓存的同一个集合中
     * @param key 指定的键
     * @param values 指定的值
     * @param time 指定的有效期, 时间
     */
    override fun addRange(key: String, values: Iterable<ReservationRecord>, time: Duration) {
        for (value in values)
            this.operations.add(key, value)
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
    }

    /**
     * 批量添加指定的值到缓存中, 每一个 [Pair] 将被添加到不同的集合中
     * @param pairs 指定的键值对
     */
    override fun addRange(pairs: Map<String, Iterable<ReservationRecord>>) {
        for (pair in pairs) {
            for (value in pair.value) {
                this.operations.add(pair.key, value)
            }
        }
    }

    /**
     * 批量添加指定的值到缓存中, 每一个 [Pair] 将被添加到不同的集合中
     * @param pairs 指定的键值对
     * @param expire 指定的有效期, 毫秒
     */
    override fun addRange(pairs: Map<String, Iterable<ReservationRecord>>, expire: Long) {
        for (pair in pairs) {
            for (value in pair.value) {
                this.operations.add(pair.key, value)
            }
            this.redisTemplate.expire(pair.key, expire, TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 批量添加指定的值到缓存中, 每一个 [Pair] 将被添加到不同的集合中
     * @param pairs 指定的键值对
     * @param time 指定的有效期, 时间
     */
    override fun addRange(pairs: Map<String, Iterable<ReservationRecord>>, time: Duration) {
        for (pair in pairs) {
            for (value in pair.value) {
                this.operations.add(pair.key, value)
            }
            this.redisTemplate.expire(pair.key, time.toMillis(), TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param value 指定的值
     */
    override fun addOrUpdate(key: String, value: ReservationRecord, comparator: ((ReservationRecord, ReservationRecord) -> Boolean)?) {
        val item = if (comparator == null) value else {
            this.operations.members(key)?.firstOrNull { p -> comparator(p, value) }
        }
        item?.run {
            this@DefaultReservationCacheServiceImpl.operations.remove(key, item)
        }
        this.operations.add(key, value)
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param value 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun addOrUpdate(key: String, value: ReservationRecord, expire: Long, comparator: ((ReservationRecord, ReservationRecord) -> Boolean)?) {
        val item = if(comparator == null) value else {
            this.operations.members(key)?.firstOrNull { p -> comparator(p, value) }
        }
        item?.run {
            this@DefaultReservationCacheServiceImpl.operations.remove(key, item)
        }
        this.operations.add(key, value)
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param value 指定的值
     * @param time 指定的有效期, 时间
     */
    override fun addOrUpdate(key: String, value: ReservationRecord, time: Duration, comparator: ((ReservationRecord, ReservationRecord) -> Boolean)?) {
        val item = if (comparator == null) value else {
            this.operations.members(key)?.firstOrNull { p -> comparator(p, value) }
        }
        item?.run {
            this@DefaultReservationCacheServiceImpl.operations.remove(key, item)
        }
        this.operations.add(key, value)
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param values 指定的值
     */
    override fun addOrUpdateRange(key: String, values: Iterable<ReservationRecord>, comparator: ((ReservationRecord, ReservationRecord) -> Boolean)?) {
        val members = if(comparator == null) emptySet<ReservationRecord>() else {
            this.operations.members(key)
        }
        for (value in values) {
            val item = if(comparator == null) value else {
                members?.firstOrNull { p -> comparator(p, value) }
            }
            item?.run {
                this@DefaultReservationCacheServiceImpl.operations.remove(key, item)
            }
            this.operations.add(key, value)
        }
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param values 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun addOrUpdateRange(key: String, values: Iterable<ReservationRecord>, expire: Long, comparator: ((ReservationRecord, ReservationRecord) -> Boolean)?) {
        val members = if(comparator == null) emptySet<ReservationRecord>() else {
            this.operations.members(key)
        }
        for (value in values) {
            val item = if(comparator == null) value else {
                members?.firstOrNull { p -> comparator(p, value) }
            }
            item?.run {
                this@DefaultReservationCacheServiceImpl.operations.remove(key, item)
            }
            this.operations.add(key, value)
        }
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param values 指定的值
     * @param time 指定的有效期, 时间
     */
    override fun addOrUpdateRange(key: String, values: Iterable<ReservationRecord>, time: Duration, comparator: ((ReservationRecord, ReservationRecord) -> Boolean)?) {
        val members = if(comparator == null) emptySet<ReservationRecord>() else {
            this.operations.members(key)
        }
        for (value in values) {
            val item = if(comparator == null) value else {
                members?.firstOrNull { p -> comparator(p, value) }
            }
            item?.run {
                this@DefaultReservationCacheServiceImpl.operations.remove(key, item)
            }
            this.operations.add(key, value)
        }
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
    }

    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回空的集合
     * @param key 指定的键
     * @return 缓存中键的值
     */
    override fun get(key: String): Set<ReservationRecord> {
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回空的集合
     * @param keys 指定的键
     * @return 缓存中键的值
     */
    override fun getRange(keys: Iterable<String>): Map<String, Set<ReservationRecord>> {
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
    override fun getOrAdd(key: String, value: ReservationRecord): Set<ReservationRecord> {
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
    override fun getOrAdd(key: String, value: ReservationRecord, expire: Long): Set<ReservationRecord> {
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
    override fun getOrAdd(key: String, value: ReservationRecord, time: Duration): Set<ReservationRecord> {
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
    override fun getOrAdd(key: String, valueFactory: (String) -> ReservationRecord, expire: Long?): Set<ReservationRecord> {
        if (!this.redisTemplate.hasKey(key)) {
            val value = valueFactory(key)
            this.operations.add(key, value)
            expire?.run {
                this@DefaultReservationCacheServiceImpl.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
            }
            return setOf(value)
        }
        expire?.run {
            this@DefaultReservationCacheServiceImpl.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        }
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param values 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAddRange(key: String, values: Iterable<ReservationRecord>): Set<ReservationRecord> {
        for (value in values)
            this.operations.add(key, value)
        return this.operations.members(key)?.toSet() ?: emptySet()
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param values 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAddRange(key: String, values: Iterable<ReservationRecord>, expire: Long): Set<ReservationRecord> {
        for (value in values)
            this.operations.add(key, value)
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
    override fun getOrAddRange(key: String, values: Iterable<ReservationRecord>, time: Duration): Set<ReservationRecord> {
        for (value in values)
            this.operations.add(key, value)
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
    override fun getOrAddRange(key: String, valuesFactory: (String) -> Iterable<ReservationRecord>, expire: Long?): Set<ReservationRecord> {
        if (!this.redisTemplate.hasKey(key)) {
            val values = valuesFactory(key)
            for (value in values) {
                this.operations.add(key, value)
            }
            expire?.run {
                this@DefaultReservationCacheServiceImpl.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
            }
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
    override fun remove(key: String, value: ReservationRecord) {
        this.operations.remove(key, value)
    }

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     * @param values 指定的值
     */
    override fun removeRange(key: String, values: Iterable<ReservationRecord>) {
        for (value in values) {
            this.operations.remove(key, value)
        }
    }

    /**
     * 从缓存中删除指定的键
     * @param pairs 指定的键值
     */
    override fun removeRange(pairs: Map<String, Iterable<ReservationRecord>>) {
        for (pair in pairs) {
            this.operations.remove(pair.key, pair.value)
        }
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
    override fun exists(key: String, value: ReservationRecord): Boolean {
        return this.operations.isMember(key, value) ?: false
    }
}