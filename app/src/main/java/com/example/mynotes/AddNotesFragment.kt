package com.example.mynotes

import android.content.ContentValues
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_add_notes.*

class AddNotesFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_notes, container, false)
    }
    var id:Int?=0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buAddNotes.setOnClickListener{
            addNotes()
        }
        setHasOptionsMenu(true)

        id= arguments?.getInt("ID")
        if (id != 0 ){
            val title = arguments?.getString("Title")
            etTitle.setText(title)
            val des = arguments?.getString("Description")
            etDescription.setText(des)
        }
    }

    private fun addNotes() {
        val dbManager= this.activity?.let { DbManager(it) }

        val values= ContentValues()
        values.put("Title",etTitle.text.toString())
        values.put("Description",etDescription.text.toString())
        if(id!=0){
            val selectionArgs = arrayOf(id.toString())
            val id = dbManager?.Update(values,"ID = ?",selectionArgs)
            if (id != null) {
                if (id > 0) {
                    Toast.makeText(this.activity, " note is updated", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this.activity, " cannot update note ", Toast.LENGTH_LONG).show()
                }
            }
        }else {
            val id = dbManager?.Insert(values)
            if (id != null) {
                if (id > 0) {
                    Toast.makeText(this.activity, " note is added", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this.activity, " cannot add note ", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.addnotes_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.backBtn ->{
                requireView().findNavController().navigate(R.id.notesListFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}