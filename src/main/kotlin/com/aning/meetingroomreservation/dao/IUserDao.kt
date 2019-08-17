package com.aning.meetingroomreservation.dao

import com.aning.meetingroomreservation.entity.User
import java.lang.reflect.Member

/**
 * 提供对 [User] 实体操作的方法
 */
public interface IUserDao {

    /**
     * 添加 [User] 记录
     * @param user 添加的 [User] 实例
     */
    public fun add(user: User)

    /**
     * 添加 [User] 记录
     * @param users 添加的 [User] 集合
     */
    public fun addRange(users: List<User>)

    /**
     * 删除 [User] 记录
     * @param user 删除的 [User] 实例
     */
    public fun delete(user: User)

    /**
     * 修改 [User] 记录
     * @param user 修改的 [User] 实例
     */
    public fun update(user: User)

    /**
     * 添加或修改 [User] 记录
     * @param user 添加或修改的 [User] 实例
     */
    public fun addOrUpdate(user: User)

    /**
     * 获取所有 [User] 记录
     * @return 所有 [User] 记录
     */
    public fun getAll(): List<User>

    /**
     * 获取指定 [id] 的 [User] 记录
     * @param id 指定的 [User] 主键
     * @return [User] 记录
     */
    public fun getById(id: String): User?

    /**
     * 获取指定 [ip] 的 [User] 记录
     * @param ip 指定的 [User] IP地址
     * @return [User] 记录
     */
    public fun getByIP(ip: String): User?

    /**
     * 获取指定 [name] 的 [User] 记录
     * @param name 指定的 [User] 名称
     * @return [User] 记录
     */
    public fun getByName(name:String):List<User>
}