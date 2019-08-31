package com.aning.meetingroomreservation.annotation.cache

/**
 * 缓存默认有效期, 60 * 60 * 1000 毫秒
 */
private const val DEFAULT_EXPIRE = 60 * 60 * 1000L

/**
 * 指定当前目标优先从缓存中获取数据
 */
@Target(AnnotationTarget.FUNCTION)
annotation class CacheGet(
        val key: String = "",
        val expire: Long = DEFAULT_EXPIRE,
        val keyGenerator: String = ""
)

/**
 * 指定当前目标优先从缓存中获取数据, 如果缓存中不存在指定的键, 将目标返回值作为新值插入缓存
 */
@Target(AnnotationTarget.FUNCTION)
annotation class CacheGetOrAdd(
        val key: String = "",
        val expire: Long = DEFAULT_EXPIRE,
        val keyGenerator: String = ""
)

/**
 * 指定将当前目标的返回值添加到缓存中, 如果键已存在则不进行任何操作
 */
@Target(AnnotationTarget.FUNCTION)
annotation class CacheAdd(
        val key: String = "",
        val expire: Long = DEFAULT_EXPIRE,
        val keyGenerator: String = ""
)

/**
 * 指定将当前目标的返回值添加到缓存中, 如果键已存在则更新缓存值
 */
@Target(AnnotationTarget.FUNCTION)
annotation class CacheAddOrUpdate(
        val key: String = "",
        val expire: Long = DEFAULT_EXPIRE,
        val keyGenerator: String = ""
)

/**
 * 指定删除键及其缓存的值
 */
@Target(AnnotationTarget.FUNCTION)
annotation class CacheRemove(
        val key: String = "",
        val keyGenerator: String = ""
)

