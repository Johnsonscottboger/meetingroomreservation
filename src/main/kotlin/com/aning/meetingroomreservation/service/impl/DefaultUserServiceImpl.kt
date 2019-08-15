package com.aning.meetingroomreservation.service.impl

import com.aning.meetingroomreservation.dao.IUserDao
import com.aning.meetingroomreservation.entity.User
import com.aning.meetingroomreservation.service.IUserService
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
public class DefaultUserServiceImpl : IUserService {

    @Resource
    private lateinit var _dao : IUserDao

    /**
     * 添加 [User] 记录
     * @param user 添加的 [User] 实例
     */
    override fun add(user: User) {
        this._dao.add(user)
    }

    /**
     * 删除 [User] 记录
     * @param user 删除的 [User] 实例
     */
    override fun delete(user: User) {
        this._dao.delete(user)
    }

    /**
     * 修改 [User] 记录
     * @param user 修改的 [User] 实例
     */
    override fun update(user: User) {
        this._dao.update(user)
    }

    /**
     * 添加或修改 [User] 记录
     * @param user 添加或修改的 [User] 实例
     */
    override fun addOrUpdate(user: User) {
        this._dao.addOrUpdate(user)
    }

    /**
     * 获取所有 [User] 记录
     * @return 所有 [User] 记录
     */
    override fun getAll(): List<User> {
        return this._dao.getAll()
    }

    /**
     * 获取指定 [id] 的 [User] 记录
     * @param id 指定的 [User] 主键
     * @return [User] 记录
     */
    override fun getById(id: String): User? {
        return this._dao.getById(id)
    }
}