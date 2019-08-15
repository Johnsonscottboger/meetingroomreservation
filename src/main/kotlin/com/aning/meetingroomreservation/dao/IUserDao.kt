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
}