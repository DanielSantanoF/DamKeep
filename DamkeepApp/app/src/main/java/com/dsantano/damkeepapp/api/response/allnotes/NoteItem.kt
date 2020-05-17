package com.dsantano.damkeepapp.api.response.allnotes

data class NoteItem(
    val content: String,
    val createdAt: String,
    val id: String,
    val lastUpdate: String,
    val title: String
)