package com.aning.meetingroomreservation.util

import java.net.InetAddress
import java.net.UnknownHostException
import javax.servlet.http.HttpServletRequest

public class IPUtil {

    companion object {
        /**
         * 获取当前 [request] 的 IP 地址
         * @param request 指定的请求实例
         */
        public fun getIPAddress(request: HttpServletRequest): String {
            val unknown = "unknown"
            try {
                var header = request.getHeader("x-forwarded-for")
                if (header.isNullOrBlank() || unknown.equals(header, true))
                    header = request.getHeader("Proxy-Client-IP")
                if (header.isNullOrBlank() || unknown.equals(header, true))
                    header = request.getHeader("WL-Proxy-Client-IP")
                if (header.isNullOrBlank() || unknown.equals(header, true)) {
                    header = request.remoteAddr
                    if (header.equals("127.0.0.1")) {
                        try {
                            header = InetAddress.getLocalHost().hostAddress
                        } catch (ex: UnknownHostException) {
                            ex.printStackTrace()
                        }
                    }
                }
                if (!header.isNullOrEmpty() && header.length > 15) {
                    val index = header.indexOf(',')
                    if (index > 0) {
                        header = header.substring(0, index)
                    }
                }

                return header
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return String()
        }
    }
}