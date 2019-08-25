package com.aning.meetingroomreservation.model

import com.aning.meetingroomreservation.entity.ReservationRecord


/**
 * 预约记录比较器
 */
public class ReservationRecordComparator : Comparator<ReservationRecord> {
    override fun compare(o1: ReservationRecord?, o2: ReservationRecord?): Int {
        if (o1 == null && o2 == null)
            return 0
        if (o1 == null)
            return -1
        if (o2 == null)
            return 1
        val queue = arrayOf(ReservationStatus.USING.value, ReservationStatus.UNUSED.value)
        val unqueue = arrayOf(ReservationStatus.CANCEL.value, ReservationStatus.USED.value)
        if (o1.status in queue && o2.status in queue) {
            return o1.startTime.compareTo(o2.startTime)
        }
        if (o1.status in unqueue && o2.status in unqueue) {
            return o1.startTime.compareTo(o2.startTime)
        }
        return if (o1.status in queue) {
            -1
        } else {
            1
        }
    }
}