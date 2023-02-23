package com.bignerdranch.android.note.repository

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.note.MainActivity
import com.bignerdranch.android.note.model.Note
import com.bignerdranch.android.note.model.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class FirebaseRepository() {
    private var auth: FirebaseAuth = Firebase.auth
    val database = Firebase.database.reference
    val myRef = database.child("note")
    private var currentUser = auth.currentUser

    fun getCurrentUser(): FirebaseUser? {
        return currentUser
    }

    fun signIn(email: String, password: String): Boolean {
        var result = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    result = true
                }
            }
        return result
    }

    fun registration(email: String, password: String): Boolean {
        var result = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    result = true
                }
            }
        return result
    }

    fun signOut() {
        auth.signOut()
    }

    fun writeDatabase(note: Note) {
        myRef.child(currentUser!!.uid).push().setValue(note)
    }

    fun readDatabase(): MutableLiveData<Response> {
        val mutableLiveData = MutableLiveData<Response>()
        if (currentUser != null) {

            myRef.child(currentUser!!.uid).get().addOnCompleteListener { task ->
                val response = Response()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.notes = it.children.map { note ->
                            note.getValue(Note::class.java)!!
                        }
                    }
                } else {
                    response.exception = task.exception
                }
                mutableLiveData.value = response
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000000L)
        }
        return mutableLiveData

    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            myRef.child(currentUser!!.uid).removeValue().await()
        }
    }

    fun update(note: Note, date: String) {
        myRef.child(currentUser!!.uid).orderByChild("date").equalTo(date)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Log.d("Firebase", dataSnapshot.toString())
                        var data = dataSnapshot.value.toString()
                        data = data.split("=").get(0).substring(1)
                        Log.d("Firebase", data)
                        myRef.child(currentUser!!.uid).child(data).child("data")
                            .setValue(note.data)
                        myRef.child(currentUser!!.uid).child(data).child("date")
                            .setValue(note.date)
                        myRef.child(currentUser!!.uid).child(data).child("characters")
                            .setValue(note.characters)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException())
                    }
                }
            )
    }


    fun deleteNews(note: Note) {
        myRef.child(currentUser!!.uid).orderByChild("date").equalTo(note.date)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Log.d("Firebase", dataSnapshot.toString())
                        var data = dataSnapshot.value.toString()
                        data = data.split("=").get(0).substring(1)
                        Log.d("Firebase", data)
                        myRef.child(currentUser!!.uid).child(data).removeValue()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException())
                    }
                }
            )
    }
}