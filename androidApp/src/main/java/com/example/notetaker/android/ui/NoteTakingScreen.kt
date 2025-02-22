package com.example.notetaker.android.ui

import android.icu.text.CaseMap.Title
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.DividerDefaults.color
import androidx.compose.material3.SnackbarDefaults.color
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notetaker.android.ui.NoteRepository
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun NoteTakingScreen(navController: NavController) {
    var noteTitle by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val noteRepository = remember { NoteRepository() }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Take Notes", style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary))

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = noteTitle,
            onValueChange = { noteTitle = it },
            placeholder = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = noteText,
            onValueChange = { noteText = it },
            placeholder = { Text("Note Content") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            Text("Save")
        }
    }
}
