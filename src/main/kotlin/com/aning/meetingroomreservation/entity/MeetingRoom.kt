package com.aning.meetingroomreservation.entity

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
        var comments: String = ""
)