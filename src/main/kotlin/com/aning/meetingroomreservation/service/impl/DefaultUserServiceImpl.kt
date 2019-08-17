package com.aning.meetingroomreservation.service.impl

import com.aning.meetingroomreservation.dao.IUserDao
import com.aning.meetingroomreservation.entity.User
import com.aning.meetingroomreservation.service.IUserService
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
public class DefaultUserServiceImpl : IUserService {

    @Resource
    private lateinit var _dao: IUserDao

    /**
     * 添加 [User] 记录
     * @param user 添加的 [User] 实例
     */
    override fun add(user: User) {
        this._dao.add(user)
    }

    /**
     * 添加 [User] 记录
     * @param users 添加的 [User] 集合
     */
    override fun addRange(users: List<User>) {
        this._dao.addRange(users)
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

    /**
     * 获取指定 [ip] 的 [User] 记录
     * @param ip 指定的 [User] IP地址
     * @return [User] 记录
     */
    override fun getByIP(ip: String): User? {
        return this._dao.getByIP(ip)
    }

    /**
     * 获取指定 [name] 的 [User] 记录
     * @param name 指定的 [User] 名称
     * @return [User] 记录
     */
    override fun getByName(name: String): List<User> {
        return this._dao.getByName(name)
    }
}