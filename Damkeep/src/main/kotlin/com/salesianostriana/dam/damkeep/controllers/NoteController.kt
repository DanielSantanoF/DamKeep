package com.salesianostriana.dam.damkeep.controllers

import com.salesianostriana.dam.damkeep.dtos.CreateNoteDTO
import com.salesianostriana.dam.damkeep.dtos.NoteDto
import com.salesianostriana.dam.damkeep.dtos.UpdateNoteDTO
import com.salesianostriana.dam.damkeep.dtos.toNoteDTO
import com.salesianostriana.dam.damkeep.entities.Note
import com.salesianostriana.dam.damkeep.repositories.NoteRepository
import com.salesianostriana.dam.damkeep.repositories.UserRepository
import com.salesianostriana.dam.damkeep.services.NoteService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/note")
class NoteController(
        private val noteRepository: NoteRepository,
        private val userRepository: UserRepository,
        private val noteService: NoteService
) {

    @PostMapping("/new")
    fun newNote(@RequestBody newNote: CreateNoteDTO){
        noteService.create(newNote)
    }

    @PutMapping("/{id}")
    fun updateNota(@RequestBody updateNote : UpdateNoteDTO) : NoteDto {
        return noteRepository.findById( updateNote.id ).map { it ->
            val noteUpdated : Note = it.copy( title = updateNote.title, content = updateNote.content)
            noteRepository.save(noteUpdated).toNoteDTO()
        }.orElseThrow() {
            ResponseStatusException( HttpStatus.NOT_FOUND, "Not results found" )
        }
    }

}

