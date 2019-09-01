package com.aning.meetingroomreservation.cache

import java.time.Duration

/**
 * 提供哈希表操作的缓存服务
 */
public interface ICacheHashService<TKey : Any, TValue : Any> {

    /**
     * 添加指定的值 [pair] 到缓存中
     * @param key 指定的键
     * @param pair 指定的值
     */
    public fun add(key: String, pair: Pair<TKey, TValue>)

    /**
     * 添加指定的值 [pair] 到缓存中, 并指定 [expire] 毫秒后过期
     * @param key 指定的键
     * @param pair 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    public fun add(key: String, pair: Pair<TKey, TValue>, expire: Long)

    /**
     * 添加指定的值 [pair] 到缓存中, 并指定 [time] 时间后过期
     * @param key 指定的键
     * @param pair 指定的值
     * @param time 指定的有效期, 时间
     */
    public fun add(key: String, pair: Pair<TKey, TValue>, time: Duration)

    /**
     * 添加指定的值到缓存中
     * @param key 指定的键
     * @param pairs 指定的值
     */
    public fun add(key: String, pairs: Map<TKey, TValue>)

    /**
     * 添加指定的值到缓存中, 并指定 [expire] 毫秒后过期
     * @param key 指定的键
     * @param pairs 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    public fun add(key: String, pairs: Map<TKey, TValue>, expire: Long)

    /**
     * 添加指定的值到缓存中, 并指定 [time] 时间后过期
     * @param key 指定的键
     * @param pairs 指定的值
     * @param time 指定的有效期, 时间
     */
    public fun add(key: String, pairs: Map<TKey, TValue>, time: Duration)

    /**
     * 添加指定的值到缓存中, 并指定 [expire] 毫秒后过期
     * @param key 指定的键
     * @param pairs 指定的值
     * @param expire 指定的有效期, 毫秒
     */
    public fun add(key: String, pairs: Iterable<Pair<TKey, TValue>>, expire: Long)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     */
    public fun addOrUpdate(key: String, addValue: Pair<TKey, TValue>, updateValue: Pair<TKey, TValue>)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     * @param expire 指定的有效期, 毫秒
     */
    public fun addOrUpdate(key: String, addValue: Pair<TKey, TValue>, updateValue: Pair<TKey, TValue>, expire: Long)

    /**
     * 添加或更新指定的值, 当键不存在时添加, 存在时更新
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValue 指定更新的值
     * @param time 指定的有效期, 时间
     */
    public fun addOrUpdate(key: String, addValue: Pair<TKey, TValue>, updateValue: Pair<TKey, TValue>, time: Duration)

    /**
     * 添加或更新指定的值
     * @param key 指定的键
     * @param addValue 指定添加的值
     * @param updateValueFactory 指定更新的值生成方法
     * @param expire 指定的有效期, 毫秒
     */
    public fun addOrUpdate(key: String, addValue: Pair<TKey, TValue>, updateValueFactory: (String) -> Pair<TKey, TValue>, expire: Long? = null)

    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回空的 [Map] 实例
     * @param key 指定的键
     * @return 缓存中键的值
     */
    public fun get(key: String): Map<TKey, TValue>?

    /**
     * 根据指定的键和字段名称, 获取对应的值, 当键不存在时返回`null`
     * @param key 指定的键
     * @param field 指定的字段名称
     * @return 缓存中键的值, 或`null`
     */
    public fun get(key: String, field: TKey): TValue?

    /**
     * 根据指定的键, 获取对应的值, 当键不存在时返回空的 [List] 实例
     * @param key 指定的键
     * @param fields 指定的字段名称
     * @return 缓存中键的值
     */
    public fun getRange(key: String, fields: Iterable<TKey>): List<TValue>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param pair 当键不存在时, 当前值会被添加到缓存中
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, pair: Pair<TKey, TValue>): TValue?

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param pair 当键不存在时, 当前值会被添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, pair: Pair<TKey, TValue>, expire: Long): TValue?

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param pair 当键不存在时, 当前值会被添加到缓存中
     * @param time 指定的有效期, 时间
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, pair: Pair<TKey, TValue>, time: Duration): TValue?

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param field 指定的字段名称
     * @param pairFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAdd(key: String, field: TKey, pairFactory: (String, TKey) -> Pair<TKey, TValue>, expire: Long? = null): TValue?

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param pairs 当键不存在时, 当前值会被添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAddRange(key: String, pairs: Map<TKey, TValue>, expire: Long): List<TValue>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param pairs 当键不存在时, 当前值会被添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAddRange(key: String, pairs: Iterable<Pair<TKey, TValue>>, expire: Long): List<TValue>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param fields 指定的字段名称
     * @param pairsFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public fun getOrAddRange(key: String, fields: Iterable<TKey>, pairsFactory: (String, Iterable<TKey>) -> Iterable<Pair<TKey, TValue>>, expire: Long? = null): List<TValue>

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     */
    public fun remove(key: String)

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     * @param field 指定的字段名称
     */
    public fun remove(key: String, field: TKey)

    /**
     * 从缓存中删除指定的键
     * @param keys 指定的键
     */
    public fun removeRange(keys: Iterable<String>)

    /**
     * 从缓存中删除指定的键
     * @param key 指定的键
     * @param fields 指定的字段名称
     */
    public fun removeRange(key: String, vararg fields: TKey)

    /**
     * 获取指定的键在缓存中是否存在
     * @param key 指定的键
     * @return 指定的键是否存在
     */
    public fun exists(key: String): Boolean

    /**
     * 获取指定的键在缓存中是否存在
     * @param key 指定的键
     * @param field 指定的字段名称
     * @return 指定的键是否存在
     */
    public fun exists(key: String, field: TKey): Boolean
}