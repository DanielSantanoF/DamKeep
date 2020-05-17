package com.dsantano.damkeepapp.ui.notes

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dsantano.damkeepapp.R
import com.dsantano.damkeepapp.api.ApiService
import com.dsantano.damkeepapp.api.generators.ApiGenerator
import com.dsantano.damkeepapp.api.response.allnotes.NoteItem
import com.dsantano.damkeepapp.common.Constants
import com.dsantano.damkeepapp.common.MyApp
import com.dsantano.damkeepapp.ui.notedetail.NoteDetailActivity
import com.dsantano.damkeepapp.viewModel.NoteViewModel

import kotlinx.android.synthetic.main.fragment_note.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyNoteRecyclerViewAdapter() : RecyclerView.Adapter<MyNoteRecyclerViewAdapter.ViewHolder>() {

    private var notes: List<NoteItem> = ArrayList()
    private val mOnClickListener: View.OnClickListener
    var apiService: ApiService
    var apiGenerator: ApiGenerator = ApiGenerator()

    init {
        apiService = apiGenerator.getApiService()
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as NoteItem

            val intent = Intent(MyApp.instance, NoteDetailActivity::class.java)
            intent.putExtra(Constants.NOTE_ID, item.id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;

            ContextCompat.startActivity(MyApp.instance, intent, null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notes[position]
        holder.txtTitle.text = item.title
        holder.txtContent.text = item.content

        holder.btnDeleteNote.setOnClickListener { v ->
            val call: Call<ResponseBody> = apiService.deleteNote(item.id)
            call.enqueue(object: Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful) {
                        Toast.makeText(MyApp.instance, "Note deleted", Toast.LENGTH_LONG).show()
                        //TODO KOTLIN NO ME DEJA HACER ALGO TAN SENCILLO COMO ELIMINAR UN ELMENTO DE UNA LISTA CON UN METODO
                        notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(MyApp.instance, "Error deleting the note", Toast.LENGTH_LONG).show()
                }
            })
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = notes.size

    fun setData(noteList: List<NoteItem>) {
        notes = noteList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val txtTitle: TextView = mView.textViewTitleNoteList
        val txtContent: TextView = mView.textViewContentNoteList
        val btnDeleteNote: ImageButton = mView.imageButtonDeleteNote

    }

}
