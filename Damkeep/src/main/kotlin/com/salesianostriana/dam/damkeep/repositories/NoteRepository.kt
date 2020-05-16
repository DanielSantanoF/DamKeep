package com.salesianostriana.dam.damkeep.repositories

import com.salesianostriana.dam.damkeep.entities.Note
import com.salesianostriana.dam.damkeep.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface NoteRepository : JpaRepository<Note, UUID> {

    @Query("select n from Note n where n.user = :user")
    fun findAllNoteByUser(user : User) : List<Note>

}