package com.salesianostriana.dam.damkeep.InitData

import com.salesianostriana.dam.damkeep.entities.Note
import com.salesianostriana.dam.damkeep.entities.User
import com.salesianostriana.dam.damkeep.repositories.NoteRepository
import com.salesianostriana.dam.damkeep.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class InitDataComponent(
        val noteRepository: NoteRepository,
        val userRepository: UserRepository,
        private val encoder: PasswordEncoder
) {

    @PostConstruct
    fun initData() {
        val userUser = User("username", encoder.encode("123456"), "usernameFullName", "USER")
        val userAdmin = User("admin", encoder.encode("123456"), "adminnameFullName", "ADMIN")
        userRepository.save(userUser)
        userRepository.save(userAdmin)

        val allNotes = listOf(
                Note("Cumpleaños carlos", "El cumpleaños de carlos es el 32 de diciembre", userUser),
                Note("Comprar para tarta", "Harina, leche y huevos", userUser),
                Note("Baneos pendientes", "Banear al usuario pepito32", userAdmin),
                Note("Nota de admin", "Muchisisimos secretos de admin estan en esta nota", userAdmin)
        )
        noteRepository.saveAll(allNotes)

    }
}