package com.dsantano.damkeepapp.api

import com.dsantano.damkeepapp.api.response.allnotes.AllNotes
import com.dsantano.damkeepapp.api.response.allnotes.NoteItem
import com.dsantano.damkeepapp.api.response.login.LoginResponse
import com.dsantano.damkeepapp.api.response.login.SendToLogin
import com.dsantano.damkeepapp.api.response.login.User
import com.dsantano.damkeepapp.api.response.note.SendToNewNote
import com.dsantano.damkeepapp.api.response.register.RegisterResponse
import com.dsantano.damkeepapp.api.response.register.SendToRegister
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("auth/login")
    fun postLogin(@Body sendToLogin: SendToLogin): Call<LoginResponse>

    @POST("user/register")
    fun postRegister(@Body sendToRegister: SendToRegister): Call<RegisterResponse>

    @GET("user/me")
    fun getMe(): Call<User>

    @GET("note/personal")
    fun getAllMyNotes(): Call<AllNotes>

    @GET("note/{id}")
    fun getNoteById(@Path("id") id: String): Call<NoteItem>

    @POST("note/new")
    fun postNewNote(@Body sendToNewNote: SendToNewNote): Call<NoteItem>

    @PUT("note/{id}")
    fun putNote(@Path("id") id: String, @Body sendToNewNote: SendToNewNote): Call<NoteItem>

    @DELETE("note/{id}")
    fun deleteNote(@Path("id") id: String): Call<ResponseBody>

}