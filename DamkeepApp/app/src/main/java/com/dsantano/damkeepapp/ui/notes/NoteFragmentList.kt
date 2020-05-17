package com.dsantano.damkeepapp.ui.notes

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dsantano.damkeepapp.R
import com.dsantano.damkeepapp.common.MyApp
import com.dsantano.damkeepapp.ui.newnote.NewNoteActivity

import com.dsantano.damkeepapp.viewModel.NoteViewModel

class NoteFragmentList : Fragment() {

    private lateinit var noteAdapter: MyNoteRecyclerViewAdapter
    private lateinit var noteViewModel: NoteViewModel
    private var columnCount = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_list, container, false)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        noteAdapter = MyNoteRecyclerViewAdapter()

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = noteAdapter
            }
        }

        loadNotes()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes(){
        noteViewModel.getAllMyNotesFromApi().observe(viewLifecycleOwner, Observer {
            noteAdapter.setData(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item!!.itemId
        if (id == R.id.add_new_note){
            val intent = Intent(MyApp.instance, NewNoteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            ContextCompat.startActivity(MyApp.instance, intent, null)
        }
        return super.onOptionsItemSelected(item)
    }

}
