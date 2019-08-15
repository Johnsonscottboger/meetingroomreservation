package com.aning.meetingroomreservation.model

/**
 * 预约会议室的状态
 */
enum class ReservationStatus(val value: Int, val desc: String) {
    CANCEL(-1, "已取消"),
    UNUSED(0, "未开始"),
    USING(1, "使用中"),
    USED(2, "已结束")
}