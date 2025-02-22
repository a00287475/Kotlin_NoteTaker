package com.example.notetaker.android.ui

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.notetaker.android.ui.NoteRepository

@Composable
fun EditNoteScreen(navController: NavController, noteId: String, initialTitle: String, initialContent: String) {
    var noteTitle by remember { mutableStateOf(initialTitle) }
    var noteText by remember { mutableStateOf(initialContent) }
    val context = LocalContext.current
    val noteRepository = remember { NoteRepository() }  // Use the NoteRepository

    // Intercept back button press
    BackHandler(onBack = {
        if (noteTitle.isNotEmpty() && noteText.isNotBlank()) {
            // Reuse the same logic from the Save Changes button
            noteRepository.updateNote(noteId, noteTitle, noteText) { success, message ->
                if (success) {
                    Toast.makeText(context, "Note saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to save note", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Note cannot be empty!", Toast.LENGTH_SHORT).show()
        }

        // Navigate back after saving
        navController.popBackStack()
    })


    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Edit Note", style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary))

        Spacer(modifier = Modifier.height(8.dp))

        // Note Title Field
        TextField(
            value = noteTitle,
            onValueChange = { noteTitle = it },
            placeholder = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Note Content Field
        TextField(
            value = noteText,
            onValueChange = { noteText = it },
            placeholder = { Text("Note Content") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = {
                if (noteTitle.isNotEmpty() && noteText.isNotBlank()) {
                    noteRepository.saveNote(noteTitle, noteText) { success, message ->
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        if (success) noteText = "" // Clear input after saving
                        if (success) noteTitle = ""
                    }
                } else {
                    Toast.makeText(context, "Note cannot be empty!", Toast.LENGTH_SHORT).show()
                }
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Save Changes")
        }
    }
}


