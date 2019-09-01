package com.aning.meetingroomreservation.cache.impl

import com.aning.meetingroomreservation.cache.ICacheHashService
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.Resource

/**
 * 使用 Redis 实现的哈希表缓存服务
 */
@Service
public class RedisHashServiceImpl<TKey : Any, TValue : Any> : ICacheHashService<TKey, TValue> {
    @Resource
    private lateinit var redisTemplate: RedisTemplate<String, TValue>

    private val operations: HashOperations<String, TKey, TValue> by lazy {
        this.redisTemplate.opsForHash<TKey, TValue>()
    }

    /**
     * 添加指定的值 [pair] 到缓存中
     * @param key 指定的键
     * @param pair 指定的值
     */
    override fun add(key: String, pair: Pair<TKey, TValue>) {
        this.operations.put(key, pair.first, pair.second)
    }

    /**
     * 添加指定的值 [pair] 到缓存中, 并指定 [expire] 毫秒后过期
     * @param key 指定的键
     * @param pair 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun add(key: String, pair: Pair<TKey, TValue>, expire: Long) {
        this.operations.put(key, pair.first, pair.second)
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
    }

    /**
     * 添加指定的值 [pair] 到缓存中, 并指定 [time] 时间后过期
     * @param key 指定的键
     * @param pair 指定的值
     * @param time 指定的有效期, 时间
     */
    override fun add(key: String, pair: Pair<TKey, TValue>, time: Duration) {
        this.operations.put(key, pair.first, pair.second)
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
    }

    /**
     * 添加指定的值到缓存中
     * @param key 指定的键
     * @param pairs 指定的值
     */
    override fun add(key: String, pairs: Map<TKey, TValue>) {
        this.operations.putAll(key, pairs)
    }

    /**
     * 添加指定的值到缓存中, 并指定 [expire] 毫秒后过期
     * @param key 指定的键
     * @param pairs 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun add(key: String, pairs: Map<TKey, TValue>, expire: Long) {
        this.operations.putAll(key, pairs)
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
    }

    /**
     * 添加指定的值到缓存中, 并指定 [time] 时间后过期
     * @param key 指定的键
     * @param pairs 指定的值
     * @param time 指定的有效期, 时间
     */
    override fun add(key: String, pairs: Map<TKey, TValue>, time: Duration) {
        this.operations.putAll(key, pairs)
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
    }

    /**
     * 添加指定的值到缓存中, 并指定 [expire] 毫秒后过期
     * @param key 指定的键
     * @param pairs 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun add(key: String, pairs: Iterable<Pair<TKey, TValue>>, expire: Long) {
        this.operations.putAll(key, pairs.toMap())
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     */
    override fun addOrUpdate(key: String, addValue: Pair<TKey, TValue>, updateValue: Pair<TKey, TValue>) {
        val r = this.operations.putIfAbsent(key, addValue.first, addValue.second)
        if (!r) {
            this.operations.delete(key, addValue.first)
            this.operations.putIfAbsent(key, updateValue.first, updateValue.second)
        }
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     * @param expire 指定的有效期, 毫秒
     */
    override fun addOrUpdate(key: String, addValue: Pair<TKey, TValue>, updateValue: Pair<TKey, TValue>, expire: Long) {
        val r = this.operations.putIfAbsent(key, addValue.first, addValue.second)
        if (!r) {
            this.operations.delete(key, addValue.first)
            this.operations.putIfAbsent(key, updateValue.first, updateValue.second)
        }
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
    }

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     * @param time 指定的有效期, 时间
     */
    override fun addOrUpdate(key: String, addValue: Pair<TKey, TValue>, updateValue: Pair<TKey, TValue>, time: Duration) {
        val r = this.operations.putIfAbsent(key, addValue.first, addValue.second)
        if (!r) {
            this.operations.delete(key, addValue.first)
            this.operations.putIfAbsent(key, updateValue.first, updateValue.second)
        }
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
    }

    /**
     * 添加或更新指定的值
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValueFactory 指定更新的值生成方法
     * @param expire 指定的有效期, 毫秒
     */
    override fun addOrUpdate(key: String, addValue: Pair<TKey, TValue>, updateValueFactory: (String) -> Pair<TKey, TValue>, expire: Long?) {
        val r = this.operations.putIfAbsent(key, addValue.first, addValue.second)
        if (!r) {
            this.operations.delete(key, addValue.first)
            val updateValue = updateValueFactory(key)
            this.operations.putIfAbsent(key, updateValue.first, updateValue.second)
        }
        expire?.run {
            this@RedisHashServiceImpl.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回空的 [Map] 实例
     * @param key 指定的键
     * @return 缓存中键的值
     */
    override fun get(key: String): Map<TKey, TValue>? {
        return this.operations.entries(key)
    }

    /**
     * 根据指定的键和字段名称, 获取对应的值, 当键不存在时返回`null`
     * @param key 指定的键
     * @param field 指定的字段名称
     * @return 缓存中键的值, 或`null`
     */
    override fun get(key: String, field: TKey): TValue? {
        return this.operations.get(key, field)
    }

    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回空的 [List] 实例
     * @param key 指定的键
     * @param fields 指定的字段名称
     * @return 缓存中键的值
     */
    override fun getRange(key: String, fields: Iterable<TKey>): List<TValue> {
        return this.operations.multiGet(key, fields.toList())
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param pair 当键不存在时, 当前值会被添加到缓存中
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, pair: Pair<TKey, TValue>): TValue? {
        this.operations.putIfAbsent(key, pair.first, pair.second)
        return this.operations.get(key, pair.first)
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param pair 当键不存在时, 当前值会被添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, pair: Pair<TKey, TValue>, expire: Long): TValue? {
        this.operations.putIfAbsent(key, pair.first, pair.second)
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        return this.operations.get(key, pair.first)
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param pair 当键不存在时, 当前值会被添加到缓存中
     * @param time 指定的有效期, 时间
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, pair: Pair<TKey, TValue>, time: Duration): TValue? {
        this.operations.putIfAbsent(key, pair.first, pair.second)
        this.redisTemplate.expire(key, time.toMillis(), TimeUnit.MILLISECONDS)
        return this.operations.get(key, pair.first)
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param field 指定的字段名称
     * @param pairFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAdd(key: String, field: TKey, pairFactory: (String, TKey) -> Pair<TKey, TValue>, expire: Long?): TValue? {
        val value = this.operations.get(key, field)
        val result = value ?: this.run {
            val addValue = pairFactory(key, field)
            this.operations.putIfAbsent(key, addValue.first, addValue.second)
            addValue.second
        }
        expire?.run {
            this@RedisHashServiceImpl.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        }
        return result
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param pairs 当键不存在时, 当前值会被添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAddRange(key: String, pairs: Map<TKey, TValue>, expire: Long): List<TValue> {
        for (pair in pairs) {
            this.operations.putIfAbsent(key, pair.key, pair.value)
        }
        return this.operations.values(key)
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param pairs 当键不存在时, 当前值会被添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAddRange(key: String, pairs: Iterable<Pair<TKey, TValue>>, expire: Long): List<TValue> {
        for (pair in pairs) {
            this.operations.putIfAbsent(key, pair.first, pair.second)
        }
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        return this.operations.values(key)
    }

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param fields 指定的字段名称
     * @param pairsFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    override fun getOrAddRange(key: String, fields: Iterable<TKey>, pairsFactory: (String, Iterable<TKey>) -> Iterable<Pair<TKey, TValue>>, expire: Long?): List<TValue> {
        val values = this.operations.entries(key)
        val expect = fields.subtract(values.map { p -> p.key })
        val newValues = pairsFactory(key, expect)
        for (pair in newValues) {
            this.operations.putIfAbsent(key, pair.first, pair.second)
        }
        expire?.run {
            this@RedisHashServiceImpl.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
        }
        values.values.addAll(newValues.map { p -> p.second })
        return values.values.toList()
    }

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     */
    override fun remove(key: String) {
        this.operations.delete(key, this.operations.keys(key))
    }

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     * @param field 指定的字段名称
     */
    override fun remove(key: String, field: TKey) {
        this.operations.delete(key, field)
    }

    /**
     * 从缓存中删除指定的键
     * @param keys 指定的键
     */
    override fun removeRange(keys: Iterable<String>) {
        for (key in keys){
            remove(key)
        }
    }

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     * @param fields 指定的字段名称
     */
    override fun removeRange(key: String, vararg fields: TKey) {
        this.operations.delete(key, fields)
    }

    /**
     * 获取指定的键在缓存中是否存在
     * @param key 指定的键
     * @return 指定的键是否存在
     */
    override fun exists(key: String): Boolean {
        return this.redisTemplate.hasKey(key)
    }

    /**
     * 获取指定的键在缓存中是否存在
     * @param key 指定的键
     * @param field 指定的字段名称
     * @return 指定的键是否存在
     */
    override fun exists(key: String, field: TKey): Boolean {
        return this.operations.hasKey(key, field)
    }
}