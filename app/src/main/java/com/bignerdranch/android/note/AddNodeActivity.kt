package com.bignerdranch.android.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.note.model.Note
import com.bignerdranch.android.note.viewModel.NoteViewModel
import kotlinx.android.synthetic.main.activity_add_node.*
import java.util.*

class AddNodeActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            characters.text = " | Characters " + s?.length.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_node)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        getDate()
        setSupportActionBar(toolbar1)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.up_arrow)
        }
        toolbar1.setNavigationOnClickListener {
            backToHomePage()
        }
        note.addTextChangedListener(textWatcher)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.note_edit_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.save-> {
                val data = note.text.toString()
                if(data.isNotEmpty())
                {
                    noteViewModel.insert(Note(data,currentDate.text.toString(),data.trim().length.toLong()))
                    backToHomePage()
                }
                else{
                    Toast.makeText(applicationContext,"Not symbol",Toast.LENGTH_SHORT).show()
                }
            }
            R.id.send-> {
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.putExtra(Intent.EXTRA_TEXT, note.text.toString())
                this.startActivity(Intent.createChooser(emailIntent,
                    "Отправка письма..."))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun backToHomePage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getDate() {
        val date = Calendar.getInstance().time
        currentDate.text = date.toString()
    }

}