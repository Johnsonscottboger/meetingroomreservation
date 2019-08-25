package com.aning.meetingroomreservation.exception

/**
 * 未认证异常
 */
public class UnauthorizedException(message: String?) : Exception(message) {
    constructor() : this("Unauthorized")
}