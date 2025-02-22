package com.example.notetaker.android.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.notetaker.android.ui.NoteRepository

@Composable
fun NoteDashboard(navController: NavController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val notes = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var selectedNotes by remember { mutableStateOf<Set<Map<String, Any>>>(emptySet()) }

    val noteRepository = remember { NoteRepository() }

    // Fetch notes from Firestore
    LaunchedEffect(Unit) {
        noteRepository.fetchNotes { fetchedNotes ->
            notes.value = fetchedNotes
        }
    }

    // Function to delete selected notes
    fun deleteSelectedNotes(value: Any?) {
        // Remove selected notes from Firestore
        selectedNotes.forEach { note ->
            val noteId = note["id"]?.toString()
            if (noteId != null) {
                db.collection("notes").document(noteId).delete()
            }
        }

        // Remove selected notes from UI
        notes.value = notes.value.filterNot { it in selectedNotes }

        // Clear selection
        selectedNotes = emptySet()

        Toast.makeText(context, "Selected notes deleted", Toast.LENGTH_SHORT).show()
    }

    // Layout for the NoteDashboard
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Notes",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Divider (Line) below the title
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // New Note Button
        Button(
            onClick = { navController.navigate("notetaking_screen")  },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = "New Note")
        }

        // Edit and Delete buttons for selected notes
        if (selectedNotes.isNotEmpty()) {
            Button(
                onClick = { /* Edit functionality, navigate to edit screen */ },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text(text = "Edit Note")
            }

            Button(
                onClick = { deleteSelectedNotes(selectedNotes) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Delete Notes")
            }
        }

        // If there are no notes, show a placeholder message
        if (notes.value.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.White)) {
                Text("No notes available. Please add some notes.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes.value) { note ->
                    NoteItem(
                        note = note,
                        isSelected = selectedNotes.contains(note),
                        onSelect = { isSelected ->
                            selectedNotes = if (isSelected) {
                                selectedNotes + note
                            } else {
                                selectedNotes - note
                            }
                        }
                    )
                }
            }
        }
    }
}

// Note Item UI
@Composable
fun NoteItem(note: Map<String, Any>, isSelected: Boolean, onSelect: (Boolean) -> Unit) {
    val backgroundColor = if (isSelected) Color.LightGray else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.secondary)
            .background(backgroundColor)
            .selectable(selected = isSelected, onClick = { onSelect(!isSelected) }),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note["title"]?.toString() ?: "Untitled",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note["content"]?.toString() ?: "No content available",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            val timestamp = note["timestamp"]?.toString()?.let {
                val date = java.util.Date(it.toLong())
                date.toString()
            } ?: "Unknown date"
            Text(
                text = "Created on: $timestamp",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )
        }
    }
}

//fun fetchNotes(callback: (List<Map<String, Any>>) -> Unit) {
//    db.collection("notes")
//        .get()
//        .addOnSuccessListener { result ->
//            val notesList = mutableListOf<Map<String, Any>>()
//            for (document in result) {
//                val note = document.data.toMutableMap() // Convert to mutableMap
//                note["id"] = document.id  // 🔥 Add document ID for reference
//                notesList.add(note)
//            }
//            callback(notesList)
//        }
//        .addOnFailureListener { exception ->
//            Log.e("Firestore", "Error getting notes", exception)
//        }
//}

class NoteDashboard {
    private val noteRepository = NoteRepository() // ✅ Use Repository

    fun loadNotes() {
        noteRepository.fetchNotes { notes ->
            notes.forEach { note ->
                Log.d("NoteDashboard", "Note: ${note["title"]}")
            }
        }
    }
}




