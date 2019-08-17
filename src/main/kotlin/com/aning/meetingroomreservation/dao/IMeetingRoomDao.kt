package com.aning.meetingroomreservation.dao

import com.aning.meetingroomreservation.entity.MeetingRoom

/**
 * 提供对 [MeetingRoom] 实体操作的方法
 */
interface IMeetingRoomDao {

    /**
     * 添加 [MeetingRoom] 记录
     * @param meetingRoom 添加的 [MeetingRoom] 实例
     */
    public fun add(meetingRoom: MeetingRoom)

    /**
     * 删除 [MeetingRoom] 记录
     * @param meetingRoom 删除 [MeetingRoom] 实例
     */
    public fun delete(meetingRoom: MeetingRoom)

    /**
     * 删除 [MeetingRoom] 记录
     * @param meetingRoom 删除的 [MeetingRoom] 实例
     */
    public fun update(meetingRoom: MeetingRoom)

    /**
     * 添加或修改 [MeetingRoom] 记录
     * @param meetingRoom 添加或修改的 [MeetingRoom] 实例
     */
    public fun addOrUpdate(meetingRoom: MeetingRoom)

    /**
     * 获取所有 [MeetingRoom] 记录
     * @return 所有 [MeetingRoom] 记录
     */
    public fun getAll(): List<MeetingRoom>

    /**
     * 获取指定 [id] 的 [MeetingRoom] 记录
     * @param id 指定的 [MeetingRoom] 主键
     * @return [MeetingRoom] 记录
     */
    public fun getById(id: String): MeetingRoom?

    /**
     * 获取指定 [name] 的 [MeetingRoom] 记录, 支持模糊查找
     * @param name 指定的 [MeetingRoom] 名称
     * @return [MeetingRoom] 记录
     */
    public fun getByName(name: String) : List<MeetingRoom>
}