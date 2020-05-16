package com.salesianostriana.dam.damkeep.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Note(
        @Column(name = "title")
        var title: String,
        @Column(name = "content")
        var content: String,
        @JsonBackReference
        @ManyToOne
        var user: User? = null,
        @CreatedDate
        @Column(name = "created_date", nullable = false, updatable = false)
        var createdAt: LocalDate? = null,
        @LastModifiedDate
        @Column(name = "last_modified_date", nullable = false)
        var lastUpdate: LocalDate? = null,
        @Id @GeneratedValue val id: UUID? = null

)