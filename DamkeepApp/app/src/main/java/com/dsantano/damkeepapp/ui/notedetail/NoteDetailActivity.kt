package com.dsantano.damkeepapp.ui.notedetail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dsantano.damkeepapp.R
import com.dsantano.damkeepapp.api.response.allnotes.NoteItem
import com.dsantano.damkeepapp.common.Constants
import com.dsantano.damkeepapp.common.MyApp
import com.dsantano.damkeepapp.viewModel.NoteViewModel


class NoteDetailActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private var item: NoteItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        val noteId:String = intent.getStringExtra(Constants.NOTE_ID)

        val txtName: TextView = findViewById(R.id.textViewTitleDetail)
        val txtContent: TextView = findViewById(R.id.textViewContentDetail)
        val txtCreatedAT: TextView = findViewById(R.id.textViewCreatedAtDetail)
        val txtLastUpdatedAT: TextView = findViewById(R.id.textViewLastUpdatedAtDetail)
        val btnEdit: Button = findViewById(R.id.buttonEditDetail)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        noteViewModel.getNoteDetailFromApi(noteId).observe(this, Observer {
            item = it
            txtName.text = it.title
            txtContent.text = it.content
            txtCreatedAT.text = "Created at: ${it.createdAt}"
            txtLastUpdatedAT.text = "Last update: ${it.lastUpdate}"

        })

        btnEdit.setOnClickListener { v ->
            val intent = Intent(MyApp.instance, EditNoteActivity::class.java)
            intent.putExtra(Constants.NOTE_ID, item?.id)
            intent.putExtra(Constants.NOTE_TITLE, item?.title)
            intent.putExtra(Constants.NOTE_CONTENT, item?.content)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ContextCompat.startActivity(MyApp.instance, intent, null)
            finish()
        }
    }

}
