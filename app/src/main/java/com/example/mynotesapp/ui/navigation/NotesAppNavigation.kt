package com.example.mynotesapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynotesapp.ui.NotesInfoScreen
import com.example.mynotesapp.ui.NotesListScreen
import com.example.mynotesapp.ui.viewmodel.NotesViewModel

@Composable
fun NotesAppNavigation(viewModel: NotesViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NotesAppScreens.NotesList.route
    ) {
        composable(route = NotesAppScreens.NotesList.route) {
            NotesListScreen(navController, viewModel = viewModel)
        }
        composable(route = NotesAppScreens.NoteDetails.route) {
            NotesInfoScreen(navController, viewModel = viewModel)
        }
    }
}

sealed class NotesAppScreens(val route: String) {
    object NotesList : NotesAppScreens("notes_list")
    object NoteDetails : NotesAppScreens("notes_info")
}