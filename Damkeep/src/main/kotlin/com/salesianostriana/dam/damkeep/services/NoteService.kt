package com.salesianostriana.dam.damkeep.services

import com.salesianostriana.dam.damkeep.dtos.CreateNoteDTO
import com.salesianostriana.dam.damkeep.dtos.UpdateNoteDTO
import com.salesianostriana.dam.damkeep.entities.Note
import com.salesianostriana.dam.damkeep.repositories.NoteRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class NoteService(
        private val repo: NoteRepository
) {

    fun findById(id : UUID) = repo.findById(id)

    fun create(newNote : CreateNoteDTO): Optional<Note> {
        return Optional.of(
                with(newNote) {
                    repo.save(Note(title, content, user))
                }
        )
    }

    fun findAllNoteByUser() = repo.findAllNoteByUser()

    fun deleteNoteById(id: UUID) = repo.deleteById(id)

}