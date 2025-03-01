package com.example.notetaker.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.notetaker.android.ui.NoteTakingScreen
import com.google.firebase.database.FirebaseDatabase
import com.example.notetaker.android.ui.NoteDashboard
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notetaker.android.ui.EditNoteScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        setContent {
            // Setup the NavController for navigation
            val navController = rememberNavController()
            // Set up the NavHost
            NavHost(navController = navController, startDestination = "note_dashboard") {
                composable("note_dashboard") {
                    NoteDashboard(navController = navController) // Pass navController
                }
                composable("notetaking_screen") {
                    NoteTakingScreen(navController = navController)// Add your Note taking screen here
                }
                composable("edit_note_screen/{noteId}/{title}/{content}") { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                    val title = backStackEntry.arguments?.getString("title") ?: ""
                    val content = backStackEntry.arguments?.getString("content") ?: ""
                    EditNoteScreen(
                        navController = navController,
                        noteId = noteId,
                        initialTitle = title,
                        initialContent = content
                    )
                }
            }
        }
    }
}