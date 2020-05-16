package com.salesianostriana.dam.damkeep.controllers

import com.salesianostriana.dam.damkeep.dtos.CreateNoteDTO
import com.salesianostriana.dam.damkeep.dtos.NoteDto
import com.salesianostriana.dam.damkeep.dtos.UpdateNoteDTO
import com.salesianostriana.dam.damkeep.dtos.toNoteDTO
import com.salesianostriana.dam.damkeep.entities.Note
import com.salesianostriana.dam.damkeep.entities.User
import com.salesianostriana.dam.damkeep.repositories.NoteRepository
import com.salesianostriana.dam.damkeep.services.NoteService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*


@RestController
@RequestMapping("/note")
class NoteController(
        private val noteRepository: NoteRepository,
        private val noteService: NoteService
) {

    @GetMapping("/{id}")
    fun getNoteById(@PathVariable id : UUID) : Optional<Note> {
        return noteRepository.findById(id)
    }

    @GetMapping("/personal")
    fun getAllUserNotes( @AuthenticationPrincipal user : User ): List<NoteDto> {
        return noteRepository.findAllNoteByUser( user ).map { it.toNoteDTO() }
    }

    @PostMapping("/new")
    fun newNote(@AuthenticationPrincipal user : User, @RequestBody newNote: CreateNoteDTO) : Optional<Note> {
        return noteService.create(newNote, user)
    }

    @PutMapping("/{id}")
    fun updateNote(@RequestBody updateNote : UpdateNoteDTO, @PathVariable id : UUID) : NoteDto {
        return noteRepository.findById( id ).map { it ->
            val noteUpdated : Note = it.copy( title = updateNote.title, content = updateNote.content)
            noteRepository.save(noteUpdated).toNoteDTO()
        }.orElseThrow() {
            ResponseStatusException( HttpStatus.NOT_FOUND, "Not results found" )
        }
    }

    @DeleteMapping("/{id}")
    fun deleteNote(@PathVariable id : UUID) : ResponseEntity<Void> {
        noteRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }

}