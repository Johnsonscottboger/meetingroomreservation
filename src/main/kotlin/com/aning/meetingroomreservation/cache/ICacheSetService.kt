package com.aning.meetingroomreservation.cache

import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration
import java.util.Comparator

/**
 * 提供 `Set` 集合操作的缓存服务
 */
public interface ICacheSetService<T : Any> {

    /**
     * 添加指定的值 [value] 到缓存中
     * @param key 指定的键
     * @param value 指定的值
     */
    public fun add(key: String, value: T)

    /**
     * 添加指定的值 [value] 到缓存中, 并指定 [expire] 毫秒后过期
     * @param key 指定的键
     * @param value 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    public fun add(key: String, value: T, expire: Long)

    /**
     * 添加指定的值 [value] 到缓存中, 并指定 [time] 时间后过期
     * @param key 指定的键
     * @param value 指定的值
     * @param time 指定的有效期, 时间
     */
    public fun add(key: String, value: T, time: Duration)

    /**
     * 批量添加指定的值到缓存的同一个集合中
     * @param key 指定的键
     * @param values 指定的值
     */
    public fun addRange(key: String, values: Iterable<T>)

    /**
     * 批量添加指定的值到缓存的同一个集合中
     * @param key 指定的键
     * @param values 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    public fun addRange(key: String, values: Iterable<T>, expire: Long)

    /**
     * 批量添加指定的值到缓存的同一个集合中
     * @param key 指定的键
     * @param values 指定的值
     * @param time 指定的有效期, 时间
     */
    public fun addRange(key: String, values: Iterable<T>, time: Duration)

    /**
     * 批量添加指定的值到缓存中, 每一个 [Pair] 将被添加到不同的集合中
     * @param pairs 指定的键值对
     */
    public fun addRange(pairs: Map<String, Iterable<T>>)

    /**
     * 批量添加指定的值到缓存中, 每一个 [Pair] 将被添加到不同的集合中
     * @param pairs 指定的键值对
     * @param expire 指定的有效期, 毫秒
     */
    public fun addRange(pairs: Map<String, Iterable<T>>, expire: Long)

    /**
     * 批量添加指定的值到缓存中, 每一个 [Pair] 将被添加到不同的集合中
     * @param pairs 指定的键值对
     * @param time 指定的有效期, 时间
     */
    public fun addRange(pairs: Map<String, Iterable<T>>, time: Duration)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param value 指定的值
     */
    public fun addOrUpdate(key: String, value: T, comparator: ((T, T) -> Boolean)? = null)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param value 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    public fun addOrUpdate(key: String, value: T, expire: Long, comparator: ((T, T) -> Boolean)? = null)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param value 指定的值
     * @param time 指定的有效期, 时间
     */
    public fun addOrUpdate(key: String, value: T, time: Duration, comparator: ((T, T) -> Boolean)? = null)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param values 指定的值
     */
    public fun addOrUpdateRange(key: String, values: Iterable<T>, comparator: ((T, T) -> Boolean)? = null)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param values 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    public fun addOrUpdateRange(key: String, values: Iterable<T>, expire: Long, comparator: ((T, T) -> Boolean)? = null)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param values 指定的值
     * @param time 指定的有效期, 时间
     */
    public fun addOrUpdateRange(key: String, values: Iterable<T>, time: Duration, comparator: ((T, T) -> Boolean)? = null)

    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回空的集合
     * @param key 指定的键
     * @return 缓存中键的值
     */
    public fun get(key: String): Set<T>

    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回空的集合
     * @param keys 指定的键
     * @return 缓存中键的值
     */
    public fun getRange(keys: Iterable<String>): Map<String, Set<T>>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, value: T): Set<T>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, value: T, expire: Long): Set<T>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @param time 指定的有效期, 时间
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, value: T, time: Duration): Set<T>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valueFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, valueFactory: (String) -> T, expire: Long? = null): Set<T>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param values 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAddRange(key: String, values: Iterable<T>): Set<T>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param values 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAddRange(key: String, values: Iterable<T>, expire: Long): Set<T>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param values 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param time 指定的有效期, 时间
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAddRange(key: String, values: Iterable<T>, time: Duration): Set<T>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valuesFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAddRange(key: String, valuesFactory: (String) -> Iterable<T>, expire: Long? = null): Set<T>

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     */
    public fun remove(key: String)

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     * @param value 指定的值
     */
    public fun remove(key: String, value: T)

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     * @param values 指定的值
     */
    public fun removeRange(key: String, values: Iterable<T>)

    /**
     * 从缓存中删除指定的键
     * @param pairs 指定的键值
     */
    public fun removeRange(pairs: Map<String, Iterable<T>>)

    /**
     * 获取指定的键在缓存中是否存在
     * @param key 指定的键
     * @return 是否存在
     */
    public fun exists(key: String): Boolean

    /**
     * 获取指定的键在缓存中是否存在
     * @param key 指定的键
     * @param value 指定的值
     * @return 是否存在
     */
    public fun exists(key: String, value: T): Boolean
}