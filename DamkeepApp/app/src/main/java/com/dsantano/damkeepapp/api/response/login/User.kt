package com.dsantano.damkeepapp.api.response.login

data class User(
    val fullName: String,
    val id: String,
    val roles: String,
    val username: String
)