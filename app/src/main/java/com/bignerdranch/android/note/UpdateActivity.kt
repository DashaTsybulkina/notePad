package com.bignerdranch.android.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.note.model.Note
import com.bignerdranch.android.note.viewModel.UpdateViewModel
import kotlinx.android.synthetic.main.activity_update.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class UpdateActivity : AppCompatActivity() {

    lateinit var date: Date
    private lateinit var viewModel: UpdateViewModel
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        viewModel = ViewModelProvider(this).get(UpdateViewModel::class.java)

        setSupportActionBar(toolbar2)
        getDate()
        updateNote.addTextChangedListener(textWatcher)

        val bundle:Bundle?=intent.extras
        if(bundle != null){
            note = bundle.getSerializable("note") as Note
            loadNote()
        }

        updateData.setOnClickListener {
            updateNote()
        }

        toolbar2.setNavigationOnClickListener {
            backToHomePage()
        }
    }

    private fun backToHomePage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun loadNote()
    {
        updateNote.setText(note.data)
        updateCharacter.text="${note.characters}"
    }

    private fun updateNote()
    {
        note.data=updateNote.text.toString()
        val oldDate = note.date
        note.date=updateDate.text.toString()
        note.characters=updateNote.text.toString().length.toLong()
        viewModel.update(note, oldDate)
        val handler = android.os.Handler()
        handler.postDelayed({val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()}, 1000)

    }

    private fun getDate() {
        date= Calendar.getInstance().time
        updateDate.text=date.toString()
    }

    private val textWatcher= object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateCharacter.text= " | Character "+s?.length.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.update_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.send-> {
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.putExtra(Intent.EXTRA_TEXT, updateNote.text.toString())
                this.startActivity(Intent.createChooser(emailIntent,
                    "Отправка письма..."))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}