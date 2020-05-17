package com.dsantano.damkeepapp.ui.newnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dsantano.damkeepapp.R
import com.dsantano.damkeepapp.api.ApiService
import com.dsantano.damkeepapp.api.generators.ApiGenerator
import com.dsantano.damkeepapp.api.response.allnotes.NoteItem
import com.dsantano.damkeepapp.api.response.note.SendToNewNote
import com.dsantano.damkeepapp.common.Constants
import com.dsantano.damkeepapp.common.MyApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewNoteActivity : AppCompatActivity() {

    lateinit var apiService: ApiService
    var apiGenerator: ApiGenerator = ApiGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        apiService = apiGenerator.getApiService()

        val edTitle: EditText = findViewById(R.id.editTextTitleNew)
        val edContent: EditText = findViewById(R.id.editTextContentNew)
        val btnCreate: Button = findViewById(R.id.buttonNewNote)


        btnCreate.setOnClickListener{ v ->
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
                val call: Call<NoteItem> = apiService.postNewNote(sendToNewNote)
                call.enqueue(object: Callback<NoteItem> {
                    override fun onResponse(
                        call: Call<NoteItem>,
                        response: Response<NoteItem>
                    ) {
                        if(response.isSuccessful) {
                            Toast.makeText(MyApp.instance, "Note created", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(MyApp.instance, "Error at create the new note", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<NoteItem>, t: Throwable) {
                        Toast.makeText(MyApp.instance, "Error at create the new note", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}
