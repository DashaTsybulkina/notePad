package com.bignerdranch.android.note.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.note.R
import com.bignerdranch.android.note.databinding.ListItemBinding
import com.bignerdranch.android.note.listener.Listener
import com.bignerdranch.android.note.model.Note

class NoteAdapter(private val listener: Listener): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var noteList: List<Note> = ArrayList()

    inner class NoteViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root){

        init {
            itemView.setOnClickListener {
                listener.onClickListener(adapterPosition)
            }
        }

        fun bind(note: Note){
            binding.data.text = note.data
            binding.date.text = note.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note=noteList[position]
        holder.bind(note)
    }

    fun setData(noteList: List<Note>)
    {
        this.noteList=noteList
        Log.e("ADAPTER", noteList.size.toString())
        notifyDataSetChanged()
    }

    override fun getItemCount():Int =noteList.size
}