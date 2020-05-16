package com.salesianostriana.dam.damkeep.dtos

import com.salesianostriana.dam.damkeep.entities.Note
import com.salesianostriana.dam.damkeep.entities.User
import java.time.LocalDate
import java.util.*

data class NoteDto(
        var title: String,
        var content: String,
        var createdAt: LocalDate? = null,
        var lastUpdate: LocalDate? = null,
        val id: UUID? = null
)

fun Note.toNoteDTO() = NoteDto(title, content, createdAt, lastUpdate, id)

data class CreateNoteDTO(
        var title: String,
        var content: String
)

data class UpdateNoteDTO(
        var title: String,
        var content: String
)