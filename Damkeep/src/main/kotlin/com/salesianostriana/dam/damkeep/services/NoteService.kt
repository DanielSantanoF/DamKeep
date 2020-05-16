package com.salesianostriana.dam.damkeep.services

import com.salesianostriana.dam.damkeep.dtos.CreateNoteDTO
import com.salesianostriana.dam.damkeep.dtos.UpdateNoteDTO
import com.salesianostriana.dam.damkeep.entities.Note
import com.salesianostriana.dam.damkeep.entities.User
import com.salesianostriana.dam.damkeep.repositories.NoteRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class NoteService(
        private val repo: NoteRepository
) {

    fun create(newNote : CreateNoteDTO, user: User): Optional<Note> {
        return Optional.of(
                with(newNote) {
                    repo.save(Note(title, content, user))
                }
        )
    }

}