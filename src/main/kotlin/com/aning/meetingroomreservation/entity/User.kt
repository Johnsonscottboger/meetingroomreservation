package com.aning.meetingroomreservation.entity

import kotlin.reflect.full.memberProperties

/**
 * 用户
 */
public data class User(
        /**
         * 主键
         */
        var id: String = "",

        /**
         * IP 地址
         */
        var ip: String = "",

        /**
         * 名称
         */
        var name: String = "",

        /**
         * 部门名称
         */
        var department: String = ""
)