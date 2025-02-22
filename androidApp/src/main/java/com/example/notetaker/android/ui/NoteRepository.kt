package com.example.notetaker.android.ui

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.example.notetaker.android.ui.NoteTakingScreen

class NoteRepository {
    private val db = FirebaseFirestore.getInstance()

    fun saveNote(noteTitle: String, noteText: String, callback: (Boolean, String) -> Unit) {
        val note = hashMapOf(
            "title" to noteTitle,
            "content" to noteText,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("notes")
            .add(note)
            .addOnSuccessListener {
                Log.d("Firestore", "Note saved successfully!")
                callback(true, "Note saved successfully!")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saving note", e)
                callback(false, "Error saving note: ${e.message}")
            }
    }

    class NoteRepository {
        private val firestore = FirebaseFirestore.getInstance()

//        fun updateNote(noteId: String, title: String, content: String, callback: (Boolean, String) -> Unit) {
//            val noteRef = firestore.collection("notes").document(noteId)
//            // Directly update the fields
//            noteRef.update("title", title, "content", content)
//                .addOnSuccessListener {
//                    callback(true, "Note updated successfully!")
//                }
//                .addOnFailureListener { e ->
//                    callback(false, "Error updating note: ${e.message}")
////                }
//    }
    }
    fun updateNote(noteId: String, title: String, content: String, callback: (Boolean, String) -> Unit) {
        val updatedNote = hashMapOf(
            "title" to title,
            "content" to content,
            "timestamp" to System.currentTimeMillis()
        )
    }
    fun fetchNotes(callback: (List<Map<String, Any>>) -> Unit) {
        db.collection("notes")
            .get()
            .addOnSuccessListener { result ->
                val notesList = mutableListOf<Map<String, Any>>()
                for (document in result) {
                    val note = document.data.toMutableMap() // Convert to mutableMap
                    note["id"] = document.id  // This is the HashMap representing the note
                    notesList.add(note)
                }
                callback(notesList) // Return the list of notes to the UI
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting notes", exception)
            }
    }

}