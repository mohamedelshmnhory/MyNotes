package com.example.mynotes

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_notes_list.*
import kotlinx.android.synthetic.main.ticcket.view.*

class NotesListFragment : Fragment() {

    val listNotes = ArrayList<Note>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
//        listNotes.add(Note(1," meet professor","Create any pattern of your own - tiles, texture, skin, wallpaper, comic effect, website background and more.  Change any artwork of pattern you found into different flavors and call them your own."))
//        listNotes.add(Note(2," meet doctor","Create any pattern of your own - tiles, texture, skin, wallpaper, comic effect, website background and more.  Change any artwork of pattern you found into different flavors and call them your own."))
//         listNotes.add(Note(3," meet friend","Create any pattern of your own - tiles, texture, skin, wallpaper, comic effect, website background and more.  Change any artwork of pattern you found into different flavors and call them your own."))
        LoadQuery("%")
        val myAdapter = this.activity?.let { MyNotesAdapter(it,listNotes) }
        lvNotes.adapter = myAdapter
    }

    fun LoadQuery(title:String){
        val dbManager= this.activity?.let { DbManager(it) }
        val projections= arrayOf("ID","Title","Description")
        val selectionArgs= arrayOf(title)
        val cursor= dbManager?.query(projections,"Title like ?",selectionArgs,"Title")
        listNotes.clear()
        if (cursor != null) {
            if(cursor.moveToFirst()){
                do{
                    val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                    val Title=cursor.getString(cursor.getColumnIndex("Title"))
                    val Description=cursor.getString(cursor.getColumnIndex("Description"))

                    listNotes.add(Note(ID,Title,Description))

                }while (cursor.moveToNext())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.noteslist_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addNotesBu ->{
                requireView().findNavController().navigate(R.id.action_notesListFragment_to_addNotesFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    inner  class MyNotesAdapter(context: Context, var listNotesAdapter: ArrayList<Note>) :
        BaseAdapter() {
        override fun getCount(): Int {
            return listNotesAdapter  .size
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
           val myView = layoutInflater.inflate(R.layout.ticcket,null)
            val note = listNotesAdapter[position]
            myView.tvTitle.text = note.nodeName
            myView.tvDes.text = note.nodeDes
            //delete
            myView.ivDelete.setOnClickListener{
                val dbManager=DbManager(this.context!!)
                val selectionArgs= arrayOf(note.nodeID.toString())
                dbManager.Delete("ID=?",selectionArgs)
                LoadQuery("%")
            }
            myView.ivEdit.setOnClickListener{
                GoToUpdate(note)
            }
            return myView
        }

        var context:Context?= context
    }

    fun GoToUpdate(note:Note){
        var bundle = Bundle()
        note.nodeID?.let { bundle.putInt("ID", it) }
        bundle.putInt("ID", note.nodeID!!)
        bundle.putString("Title",note.nodeName!!)
        bundle.putString("Description",note.nodeDes!!)
        view?.findNavController()?.navigate(R.id.action_notesListFragment_to_addNotesFragment,bundle)
    }
}