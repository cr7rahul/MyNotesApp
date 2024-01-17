package com.example.mynotesapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mynotesapp.room.data.NoteItem
import com.example.mynotesapp.ui.navigation.NotesAppScreens
import com.example.mynotesapp.ui.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesInfoScreen(navController: NavController, viewModel: NotesViewModel) {

    val newsViewModel by viewModel.mutableInsertNote.observeAsState(initial = Unit)

    LaunchedEffect(Unit){

    }

    var noteTitle by remember { mutableStateOf("") }
    var noteDescription by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Add Notes")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innedPadding ->
        Column(
            modifier = Modifier
                .padding(innedPadding)
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = noteTitle,
                onValueChange = { noteTitle = it },
                label = { Text(text = "Title") },
                maxLines = 2,
                textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, top = 10.dp, end = 0.dp, bottom = 10.dp),
                value = noteDescription,
                onValueChange = { noteDescription = it },
                label = { Text(text = "Description") },
                maxLines = 4,
                textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Normal)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                ElevatedButton(
                    onClick = {
                        viewModel.insertNote(
                            noteItem = NoteItem(
                                noteTitle = noteTitle,
                                noteDescription = noteDescription
                            )
                        )
                        newsViewModel.let {
                            navController.navigate(NotesAppScreens.NotesList.route)
                        }
                    },
                    elevation = ButtonDefaults.buttonElevation(5.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Submit")
                }
            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun NotesInfoScreenPreview() {
    NotesInfoScreen(
        navController = rememberNavController(),
        viewModel = viewModel()
    )
}