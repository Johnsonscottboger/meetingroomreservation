package com.aning.meetingroomreservation.service.impl

import com.aning.meetingroomreservation.dao.IMeetingRoomDao
import com.aning.meetingroomreservation.entity.MeetingRoom
import com.aning.meetingroomreservation.service.IMeetingRoomService
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
public class DefaultMeetingRoomServiceImpl : IMeetingRoomService {

    @Resource
    private lateinit var _dao: IMeetingRoomDao

    /**
     * 添加 [MeetingRoom] 记录
     * @param meetingRoom 添加的 [MeetingRoom] 实例
     */
    override fun add(meetingRoom: MeetingRoom) {
        this._dao.add(meetingRoom)
    }

    /**
     * 删除 [MeetingRoom] 记录
     * @param meetingRoom 删除 [MeetingRoom] 实例
     */
    override fun delete(meetingRoom: MeetingRoom) {
        this._dao.delete(meetingRoom)
    }

    /**
     * 删除 [MeetingRoom] 记录
     * @param meetingRoom 删除的 [MeetingRoom] 实例
     */
    override fun update(meetingRoom: MeetingRoom) {
        this._dao.update(meetingRoom)
    }

    /**
     * 添加或修改 [MeetingRoom] 记录
     * @param meetingRoom 添加或修改的 [MeetingRoom] 实例
     */
    override fun addOrUpdate(meetingRoom: MeetingRoom) {
        this._dao.addOrUpdate(meetingRoom)
    }

    /**
     * 获取所有 [MeetingRoom] 记录
     * @return 所有 [MeetingRoom] 记录
     */
    override fun getAll() : List<MeetingRoom>{
        return this._dao.getAll()
    }

    /**
     * 获取指定 [id] 的 [MeetingRoom] 记录
     * @param id 指定的 [MeetingRoom] 主键
     * @return [MeetingRoom] 记录
     */
    override fun getById(id: String): MeetingRoom? {
        return this._dao.getById(id)
    }

    /**
     * 获取指定 [name] 的 [MeetingRoom] 记录, 支持模糊查找
     * @param name 指定的 [MeetingRoom] 名称
     * @return [MeetingRoom] 记录
     */
    override fun getByName(name: String): List<MeetingRoom> {
        return this._dao.getByName(name)
    }
}