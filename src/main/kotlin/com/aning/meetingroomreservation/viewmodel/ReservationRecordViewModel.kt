package com.aning.meetingroomreservation.viewmodel

public data class ReservationRecordViewModel(
        var id: String = "",
        var ip: String = "",
        var userId: String = "",
        var meetingRoomId: String = "",
        var date: String = "",
        var startTime: String = "",
        var endTime: String = "",
        var status: Int = 0,
        var statusDesc: String = "",
        var name: String? = "",
        var comments: String? = ""
)