package com.salesianostriana.dam.damkeep.repositories

import com.salesianostriana.dam.damkeep.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {

    fun findByUsername(username : String) : Optional<User>

}