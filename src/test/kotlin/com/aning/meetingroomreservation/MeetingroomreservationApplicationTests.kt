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
}
