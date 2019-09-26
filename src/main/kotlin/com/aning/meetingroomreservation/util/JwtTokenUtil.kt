package com.aning.meetingroomreservation.util

import com.aning.meetingroomreservation.service.IUserService
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.Resource

/**
 * Jwt Token 工具类
 */
@Component
public class JwtTokenUtil {
    companion object {

        private const val EXPIRE_TIME = 10 * 24 * 60 * 60 * 1000

        /**
         * 认证头
         */
        public const val AUTH_HEADER = "Authorization"

        /**
         * 令牌前缀
         */
        public const val TOKEN_PREFIX = "Bearer "

        /**
         * 验证 [token] 是否有效
         * @param token 令牌
         * @param secret 密钥
         * @return 是否有效
         */
        public fun verify(token: String, userName: String, secret: String): Boolean {
            return try {
                val algorithm = Algorithm.HMAC256(secret)
                val verifier = JWT.require(algorithm)
                        .withClaim("username", userName)
                        .build()
                verifier.verify(token)
                true
            } catch (ex: Exception) {
                false
            }
        }

        /**
         * 获取 [token] 中的用户名
         * @param token 令牌
         * @return 用户名
         */
        public fun getUserName(token: String): String? {
            return try {
                val jwt = JWT.decode(token)
                jwt.getClaim("username").asString()
            } catch (ex: Exception) {
                null
            }
        }

        /**
         * 获取 [token] 中的用户Id
         * @param token 令牌
         * @return 用户Id
         */
        public fun getUserId(token: String): String? {
            return try {
                val jwt = JWT.decode(token)
                jwt.getClaim("userid").asString()
            } catch (ex: Exception) {
                null
            }
        }

        /**
         * 生成签名
         * @param userId 用户Id
         * @param userName 用户名
         * @param secret 密钥
         * @return token
         */
        public fun sign(userId: String, userName: String, secret: String): String {
            val date = Date(System.currentTimeMillis() + EXPIRE_TIME)
            val algorithm = Algorithm.HMAC256(secret)
            return JWT.create()
                    .withClaim("userid", userId)
                    .withClaim("username", userName)
                    .withExpiresAt(date)
                    .sign(algorithm)
        }
    }
}