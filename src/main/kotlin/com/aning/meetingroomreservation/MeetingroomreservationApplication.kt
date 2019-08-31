package com.aning.meetingroomreservation

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@MapperScan(basePackages = ["com.aning.meetingroomreservation.dao"])
class MeetingroomreservationApplication

fun main(args: Array<String>) {
    runApplication<MeetingroomreservationApplication>(*args)
}
