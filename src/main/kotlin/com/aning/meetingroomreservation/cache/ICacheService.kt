package com.aning.meetingroomreservation.cache

import java.time.Duration

/**
 * 提供缓存服务
 */
public interface ICacheService<T : Any> {

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
     * 批量添加指定的值到缓存中
     * @param pairs 指定的键值对集合
     */
    public fun addRange(pairs: Map<String, T>)

    /**
     * 批量添加指定的值到缓存中, 并指定 [expire] 毫秒后过期
     * @param pairs 指定的键值对集合
     * @param expire 指定的有效期, 毫秒
     */
    public fun addRange(pairs: Map<String, T>, expire: Long)

    /**
     * 批量添加指定的值到缓存中, 并指定 [time] 时间后过期
     * @param pairs 指定的键值对集合
     * @param time 指定的有效期, 时间
     */
    public fun addRange(pairs: Map<String, T>, time: Duration)

    /**
     * 批量添加指定的值到缓存中
     * @param pairs 指定的键值对集合
     */
    public fun addRange(pairs: Iterable<Pair<String, T>>)

    /**
     * 批量添加指定的值到缓存中, 并指定 [expire] 毫秒后过期
     * @param pairs 指定的键值对集合
     * @param expire 指定的有效期, 毫秒
     */
    public fun addRange(pairs: Iterable<Pair<String, T>>, expire: Long)

    /**
     * 批量添加指定的值到缓存中, 并指定 [time] 时间后过期
     * @param pairs 指定的键值对集合
     * @param time 指定的有效期, 时间
     */
    public fun addRange(pairs: Iterable<Pair<String, T>>, time: Duration)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     */
    public fun addOrUpdate(key: String, addValue: T, updateValue: T)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     * @param expire 指定的有效期, 毫秒
     */
    public fun addOrUpdate(key: String, addValue: T, updateValue: T, expire: Long)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     * @param time 指定的有效期, 时间
     */
    public fun addOrUpdate(key: String, addValue: T, updateValue: T, time: Duration)

    /**
     * 添加或更新指定的值
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValueFactory 指定更新的值生成方法
     */
    public fun addOrUpdate(key: String, addValue: T, updateValueFactory: (String) -> T)

    /**
     * 添加或更新指定的值
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValueFactory 指定更新的值生成方法
     * @param expire 指定的有效期, 毫秒
     */
    public fun addOrUpdate(key: String, addValue: T, updateValueFactory: (String) -> T, expire: Long)

    /**
     * 添加或更新指定的值
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValueFactory 指定更新的值生成方法
     * @param time 指定的有效期, 时间
     */
    public fun addOrUpdate(key: String, addValue: T, updateValueFactory: (String) -> T, time: Duration)


    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回`null`
     * @param key 指定的键
     * @return 缓存中键的值, 或`null`
     */
    public fun get(key: String): T?

    /**
     * 根据指定的键, 获取对应的值, 只返回存在的值
     * @param keys 指定的键
     * @return 缓存中键的值
     */
    public fun getRange(keys: Iterable<String>): List<T>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, value: T): T?

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, value: T, expire: Long): T?

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param value 当键不存在时, 当前值会被添加到缓存中
     * @param time 指定的有效期, 时间
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, value: T, time: Duration): T?

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valueFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, valueFactory: (String) -> T): T?

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valueFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, valueFactory: (String) -> T, expire: Long): T?

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valueFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param time 指定的有效期, 时间
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, valueFactory: (String) -> T, time: Duration): T?

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     */
    public fun remove(key: String)

    /**
     * 从缓存中删除指定的键
     * @param keys 指定的键
     */
    public fun removeRange(keys: Iterable<String>)

    /**
     * 获取指定的键在缓存中是否存在
     * @param key 指定的键
     * @return 是否存在
     */
    public fun exists(key: String): Boolean
}