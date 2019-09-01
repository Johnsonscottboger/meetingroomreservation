package com.aning.meetingroomreservation

import com.aning.meetingroomreservation.cache.ICacheSetService
import com.aning.meetingroomreservation.entity.ReservationRecord
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class MeetingroomreservationApplicationTests {

    @Autowired
    private lateinit var _cacheService: ICacheSetService<ReservationRecord>

    @Test
    fun contextLoads() {
    }

    val id = "20190901"
    val empty = ""
    val now = Date().apply {
        time = 1567319700000
    }

    @Test
    fun addReservationRecord() {
        val record = ReservationRecord(id, empty, empty, empty, now, now, null, 0, now)
        this._cacheService.addOrUpdateRange("key", listOf(record), 36000)
    }
}
