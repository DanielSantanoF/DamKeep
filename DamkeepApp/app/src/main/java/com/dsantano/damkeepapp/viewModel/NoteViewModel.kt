package com.dsantano.damkeepapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dsantano.damkeepapp.api.response.allnotes.AllNotes
import com.dsantano.damkeepapp.api.response.allnotes.NoteItem
import com.dsantano.damkeepapp.repository.ApiAuthRepository

class NoteViewModel: ViewModel() {

    private var apiAuthRepository = ApiAuthRepository()
    private lateinit var noteList: LiveData<AllNotes>
    private lateinit var noteDetail: LiveData<NoteItem>

    init {
    }

    fun getAllMyNotesFromApi(): LiveData<AllNotes> {
        noteList = apiAuthRepository.getAllMyNotes()
        return noteList
    }

    fun getNoteDetailFromApi(id: String): LiveData<NoteItem> {
        noteDetail = apiAuthRepository.getNoteDetail(id)
        return noteDetail
    }

}