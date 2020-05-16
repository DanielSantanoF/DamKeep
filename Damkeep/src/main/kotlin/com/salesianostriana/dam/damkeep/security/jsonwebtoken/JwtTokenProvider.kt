package com.salesianostriana.dam.damkeep.security.jsonwebtoken

import com.salesianostriana.dam.damkeep.entities.User
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JwtTokenProvider {

    companion object {
        const val TOKEN_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
        const val TOKEN_TYPE = "JWT"
    }

    private val jwtSecreto : String = "mJI.w|g!kCv(5bLr0A@\"wTC,N9mNM]Dd^19h0[?!KB1~I~kfA(,;T<S][_Pm_v(asdfghasdfg"

    //On days
    private val jwtDuracionToken : Long = 3
    private val jwtDuracionRefreshToken : Long = 10

    private val parser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecreto.toByteArray())).build()

    private val logger : Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    fun generateToken(authentication : Authentication) : String {
        val user : User = authentication.principal as User
        return generateTokens(user, false)
    }

    fun generateToken(user : User) = generateTokens(user, false)

    fun generateRefreshToken(authentication: Authentication) : String {
        val user : User = authentication.principal as User
        return generateTokens(user, true)
    }

    fun generateRefreshToken(user : User) = generateTokens(user, true)

    private fun generateTokens(user : User, isRefreshToken : Boolean) : String {
        val tokenExpirationDate =
                Date.from(Instant.now().plus(if (isRefreshToken) jwtDuracionRefreshToken else jwtDuracionToken, ChronoUnit.DAYS))
        val builder = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecreto.toByteArray()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(user.id.toString())
                .setExpiration(tokenExpirationDate)
                .setIssuedAt(Date())
                .claim("refresh", isRefreshToken)

        if (!isRefreshToken) {
            builder
                    .claim("fullname", user.fullName)
                    .claim("roles", user.roles.joinToString())

        }
        return builder.compact()
    }

    fun getUserIdFromJWT(token: String): UUID = UUID.fromString(parser.parseClaimsJws(token).body.subject)

    fun validateRefreshToken(token : String) = validateToken(token, true)

    fun validateAuthToken(token : String) = validateToken(token, false)


    private fun validateToken(token : String, isRefreshToken: Boolean) : Boolean {
        try {
            val claims = parser.parseClaimsJws(token)
            if (isRefreshToken != claims.body["refresh"])
                throw UnsupportedJwtException("Unnapropiated token")
            return true
        } catch (ex : Exception) {
            with(logger) {
                when (ex) {
                    is SignatureException -> info("Error of the jwtoken sign ${ex.message}")
                    is MalformedJwtException -> info("Invalid jwtoken ${ex.message}")
                    is ExpiredJwtException -> info("Jwtoken expired ${ex.message}")
                    is UnsupportedJwtException -> info("Jwtoken not supported ${ex.message}")
                    is IllegalArgumentException -> info("Jwtoken incomplete (claims empty) ${ex.message}")
                    else -> info("Unexpected Error")
                }
            }

        }

        return false

    }

}