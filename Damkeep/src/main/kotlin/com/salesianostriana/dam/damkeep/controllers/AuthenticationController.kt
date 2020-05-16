package com.salesianostriana.dam.damkeep.controllers

import com.salesianostriana.dam.damkeep.dtos.UserDTO
import com.salesianostriana.dam.damkeep.dtos.toUserDTO
import com.salesianostriana.dam.damkeep.entities.User
import com.salesianostriana.dam.damkeep.security.jsonwebtoken.BearerTokenExtractor
import com.salesianostriana.dam.damkeep.security.jsonwebtoken.JwtTokenProvider
import com.salesianostriana.dam.damkeep.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
class AuthenticationController(
        private val authenticationManager: AuthenticationManager,
        private val jwtTokenProvider: JwtTokenProvider,
        private val bearerTokenExtractor: BearerTokenExtractor,
        private val userService: UserService
) {

    @PostMapping("/auth/login")
    fun login(@Valid @RequestBody loginRequest : LoginRequest) : ResponseEntity<JwtUserResponse> {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        loginRequest.username, loginRequest.password
                )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val user = authentication.principal as User
        val jwtToken = jwtTokenProvider.generateToken(user)
        val jwtRefreshToken = jwtTokenProvider.generateRefreshToken(user)

        return ResponseEntity.status(HttpStatus.CREATED).body(JwtUserResponse(jwtToken, jwtRefreshToken, user.toUserDTO()))

    }

    @PostMapping("/auth/token")
    fun refreshToken(request : HttpServletRequest) : ResponseEntity<JwtUserResponse> {

        val refreshToken = bearerTokenExtractor.getJwtFromRequest(request).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing refesh jwtoken")
        }

        try {
            if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
                val userId = jwtTokenProvider.getUserIdFromJWT(refreshToken)
                val user: User = userService.findById(userId).orElseThrow {
                    UsernameNotFoundException("User not found from id")
                }
                val jwtToken = jwtTokenProvider.generateToken(user)
                val jwtRefreshToken = jwtTokenProvider.generateRefreshToken(user)

                return ResponseEntity.status(HttpStatus.CREATED).body(JwtUserResponse(jwtToken, jwtRefreshToken, user.toUserDTO()))
            }
        } catch (ex : Exception) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error unvalid jwtoken")
        }

        // In any other case
        return ResponseEntity.badRequest().build()

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/me")
    fun me(@AuthenticationPrincipal user : User) = user.toUserDTO()

}


data class LoginRequest(
        @NotBlank val username : String,
        @NotBlank val password: String
)

data class JwtUserResponse(
        val token: String,
        val refreshToken: String,
        val user : UserDTO
)