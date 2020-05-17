package com.dsantano.damkeepapp.ui.notedetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dsantano.damkeepapp.R
import com.dsantano.damkeepapp.api.ApiService
import com.dsantano.damkeepapp.api.generators.ApiGenerator
import com.dsantano.damkeepapp.api.response.allnotes.NoteItem
import com.dsantano.damkeepapp.api.response.note.SendToNewNote
import com.dsantano.damkeepapp.api.response.register.RegisterResponse
import com.dsantano.damkeepapp.api.response.register.SendToRegister
import com.dsantano.damkeepapp.common.Constants
import com.dsantano.damkeepapp.common.MyApp
import com.dsantano.damkeepapp.ui.auth.LoginActivity
import com.dsantano.damkeepapp.viewModel.NoteViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditNoteActivity : AppCompatActivity() {

    lateinit var apiService: ApiService
    var apiGenerator: ApiGenerator = ApiGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        apiService = apiGenerator.getApiService()

        val noteId:String = intent.getStringExtra(Constants.NOTE_ID)
        val noteTitle:String = intent.getStringExtra(Constants.NOTE_TITLE)
        val noteContent:String = intent.getStringExtra(Constants.NOTE_CONTENT)

        val edTitle: EditText = findViewById(R.id.editTextTitleEdit)
        val edContent: EditText = findViewById(R.id.editTextContentEdit)
        val btnEdit: Button = findViewById(R.id.buttonEdditNote)

        edTitle.setText(noteTitle)
        edContent.setText(noteContent)

        btnEdit.setOnClickListener{ v ->
            val title: String = edTitle.text.toString()
            val content: String = edContent.text.toString()
            val sendToNewNote: SendToNewNote
            if(title.isEmpty() || content.isEmpty()){
                if (title.isEmpty()) {
                    edTitle.error = "Title Empty"
                }
                if (content.isEmpty()) {
                    edContent.error = "Content Empty"
                }
            } else {
                sendToNewNote = SendToNewNote(title, content)
                val call: Call<NoteItem> = apiService.putNote(noteId, sendToNewNote)
                call.enqueue(object: Callback<NoteItem> {
                    override fun onResponse(
                        call: Call<NoteItem>,
                        response: Response<NoteItem>
                    ) {
                        if(response.isSuccessful) {
                            Toast.makeText(MyApp.instance, "Note edited", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(MyApp.instance, "Error at edit", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<NoteItem>, t: Throwable) {
                        Toast.makeText(MyApp.instance, "Error at edit", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}
