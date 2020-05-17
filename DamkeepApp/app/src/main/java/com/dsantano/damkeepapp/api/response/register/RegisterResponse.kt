package com.dsantano.damkeepapp.api.response.register

data class RegisterResponse(
    val fullName: String,
    val id: String,
    val roles: String,
    val username: String
)