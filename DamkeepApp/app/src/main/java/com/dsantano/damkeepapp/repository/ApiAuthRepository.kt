package com.dsantano.damkeepapp.repository

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.dsantano.damkeepapp.api.ApiService
import com.dsantano.damkeepapp.api.generators.ApiGenerator
import com.dsantano.damkeepapp.api.response.allnotes.AllNotes
import com.dsantano.damkeepapp.api.response.allnotes.NoteItem
import com.dsantano.damkeepapp.common.MyApp
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiAuthRepository {

    var apiService: ApiService
    var apiGenerator: ApiGenerator = ApiGenerator()
    var noteList: MutableLiveData<AllNotes>
    var noteDetail: MutableLiveData<NoteItem>

    init {
        apiService = apiGenerator.getApiService()
        noteList = MutableLiveData<AllNotes>()
        noteDetail = MutableLiveData<NoteItem>()
    }

    fun getAllMyNotes(): MutableLiveData<AllNotes> {

        val call: Call<AllNotes> = apiService.getAllMyNotes()
        call.enqueue(object: Callback<AllNotes> {
            override fun onResponse(
                call: Call<AllNotes>,
                response: Response<AllNotes>
            ) {
                if(response.isSuccessful) {
                    noteList.value = response.body()
                }
            }

            override fun onFailure(call: Call<AllNotes>, t: Throwable) {
                Toast.makeText(MyApp.instance, "Error at load your notes", Toast.LENGTH_LONG).show()
            }
        })

        return noteList
    }

    fun getNoteDetail(id: String): MutableLiveData<NoteItem> {

        val call: Call<NoteItem> = apiService.getNoteById(id)
        call.enqueue(object: Callback<NoteItem> {
            override fun onResponse(
                call: Call<NoteItem>,
                response: Response<NoteItem>
            ) {
                if(response.isSuccessful) {
                    noteDetail.value = response.body()
                }
            }

            override fun onFailure(call: Call<NoteItem>, t: Throwable) {
                Toast.makeText(MyApp.instance, "Error at load Note detail", Toast.LENGTH_LONG).show()
            }
        })

        return noteDetail
    }


}