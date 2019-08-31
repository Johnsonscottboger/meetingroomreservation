package com.aning.meetingroomreservation.entity

import java.io.Serializable
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.text.StringBuilder

/**
 * 会议室
 */
public data class MeetingRoom(
        /**
         * 主键
         */
        var id: String = "",

        /**
         * 名称
         */
        var name: String = "",

        /**
         * 位置
         */
        var location: String = "",

        /**
         * 备注
         */
        var comments: String = "",

        /**
         * 创建时间
         */
        var createDateTime : Date = Date()
) : Serializable