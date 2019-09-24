package com.aning.meetingroomreservation.cache

import kotlinx.coroutines.Deferred

/**
 * 提供`Set`集合异步操作的缓存服务
 */
interface ICacheSetAsyncService<T : Any> {

    /**
     * Spring 实现的异步
     */
    public fun getOrAddRangeSpringAsync(key: String, valuesFactory: (String) -> Iterable<T>, expire: Long? = null): Set<T>

    /**
     * 根据指定的键, 获取或添加对应的值, 当指定的键不存在时, 添加指定的值
     * @param key 指定的键
     * @param valuesFactory 当键不存在时, 调用当前方法获取新值并添加到缓存中
     * @param expire 指定的有效期, 毫秒
     * @return 缓存中键的值, 或指定的新值
     */
    public suspend fun getOrAddRangeAsync(key: String, valuesFactory: (String) -> Iterable<T>, expire: Long? = null): Deferred<Set<T>>
}