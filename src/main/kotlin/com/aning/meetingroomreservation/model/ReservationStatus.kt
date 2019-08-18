package com.aning.meetingroomreservation.model

/**
 * 预约会议室的状态
 */
enum class ReservationStatus(val value: Int, val desc: String) {
    /**
     * 已取消
     */
    CANCEL(-1, "已取消"),

    /**
     * 等待中
     */
    UNUSED(0, "等待中"),

    /**
     * 使用中
     */
    USING(1, "使用中"),

    /**
     * 已结束
     */
    USED(2, "已结束");

    companion object {
        fun valueOf(value: Int): ReservationStatus {
            val values = values()
            for (item in values) {
                if (item.value == value)
                    return item
            }
            throw  EnumConstantNotPresentException(ReservationStatus::class.java, value.toString())
        }
    }
}