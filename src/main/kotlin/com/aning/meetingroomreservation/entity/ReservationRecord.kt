package com.aning.meetingroomreservation.entity

import java.io.Serializable
import java.util.*
import kotlin.reflect.full.memberProperties

/**
 * 会议室预约记录
 */
public data class ReservationRecord(
        /**
         * 主键
         */
        var id: String = "",

        /**
         * IP 地址
         */
        var ip: String = "",

        /**
         * 用户
         */
        var userId: String,

        /**
         * 会议室
         */
        var meetingRoomId: String,

        /**
         * 开始时间
         */
        var startTime: Date,

        /**
         * 结束时间
         */
        var endTime : Date,

        /**
         * 备注
         */
        var comments : String?,

        /**
         * 状态
         * -1 - 取消预订
         * 0 - 未开始
         * 1 - 使用中,
         * 2 - 已结束
         */
        var status: Int = 0,

        /**
         * 预约时间
         */
        var reserveTime: Date = Date()
): Serializable {
        constructor() : this( "", "", "", "", Date(), Date(), "")
}