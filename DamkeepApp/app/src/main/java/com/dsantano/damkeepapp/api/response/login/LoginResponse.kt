package com.dsantano.damkeepapp.api.response.login

data class LoginResponse(
    val refreshToken: String,
    val token: String,
    val user: User
)