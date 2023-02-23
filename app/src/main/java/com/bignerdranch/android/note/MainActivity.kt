package com.bignerdranch.android.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.note.adapter.NoteAdapter
import com.bignerdranch.android.note.listener.Listener
import com.bignerdranch.android.note.model.Note
import com.bignerdranch.android.note.viewModel.MainViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_node.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Listener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerViewAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        initialiseRecyclerView()

        viewModel._notes.observe(this) {
            Log.d("NOTES", it.size.toString());
            recyclerViewAdapter.setData(it)
        }

        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        } else {
            Log.d("FIREBASE", FirebaseAuth.getInstance().currentUser!!.uid)
            //Snackbar.make(activity_main, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
        }
    }

    override fun onResume() {
        super.onResume()
        initActivity()
    }

    private fun initActivity() {
        viewModel.getFirebaseNotes()
        viewModel.getFirebaseNotes().observe(this) {
            if (it.exception != null || it.notes?.size == 0) {
                viewModel.getNotes()
            } else {
                viewModel.updateNotes(it.notes!!)
            }
        }


        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddNodeActivity::class.java)
            startActivity(intent)
        }


        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun initialiseRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerViewAdapter = NoteAdapter(this)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = recyclerViewAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        val search = menu?.findItem(R.id.searchItems)
        val searchView: SearchView? = search?.actionView as SearchView?
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null)
                    getItemsFromDB(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null)
                    getItemsFromDB(newText)
                return true
            }
        })
        return true
    }

    private fun getItemsFromDB(data: String) {
        val searchText = "%$data%"
        viewModel.search(searchText)?.observe(this, Observer {
            recyclerViewAdapter.setData(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }

            R.id.sync -> {
                viewModel.sincToLocalBD()
               // viewModel.syncToFirebase()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    val simpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        viewModel.delete(position)
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                    ItemTouchHelper.LEFT -> {
                        viewModel.delete(position)
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    override fun onClickListener(position: Int) {
        val note = viewModel.getNote(position)
        val intent = Intent(this, UpdateActivity::class.java).putExtra("note", note)
        startActivity(intent)
    }
}