package com.example.mynotesapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mynotesapp.R
import com.example.mynotesapp.domain.data.PriorityItem
import com.example.mynotesapp.room.data.NoteItem
import com.example.mynotesapp.ui.navigation.NotesAppScreens
import com.example.mynotesapp.ui.viewmodel.NotesViewModel
import com.example.mynotesapp.ui.viewmodel.RetrieveNoteDetailsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesInfoScreen(navController: NavController, viewModel: NotesViewModel, noteId: Int) {

    val newsViewModel by viewModel.mutableInsertNote.observeAsState(initial = Unit)
    val retrieveNewsDetails by viewModel.mutableRetrieveNoteDetails.collectAsState()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        if (noteId != 0)
            viewModel.retrieveNoteDetails(noteId)
    }

    var noteTitle by remember { mutableStateOf("") }
    var noteDescription by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Add Notes")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(NotesAppScreens.NotesList.route) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
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

            retrieveNewsDetails.let { details ->
                when (details) {
                    is RetrieveNoteDetailsState.Error -> {
                        scope.launch {
                            details.error.localizedMessage?.let { it1 ->
                                snackBarHostState.showSnackbar(
                                    it1
                                )
                            }
                        }
                    }

                    is RetrieveNoteDetailsState.Success -> {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = if (noteId != 0) details.result.noteTitle else noteTitle,
                            onValueChange = { noteTitle = it },
                            label = { Text(text = "Title") },
                            maxLines = 2,
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, top = 10.dp, end = 0.dp, bottom = 10.dp),
                            value = if (noteId != 0) details.result.noteDescription else noteDescription,
                            onValueChange = { noteDescription = it },
                            label = { Text(text = "Description") },
                            maxLines = 4,
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Normal
                            )
                        )

                        // Drop Down to select priority
                        var isExpanded by remember {
                            mutableStateOf(false)
                        }
                        ExposedDropdownMenuBox(
                            expanded = isExpanded,
                            onExpandedChange = { newValue ->
                                isExpanded = newValue
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.White)
                        ) {
                            TextField(
                                value = priority,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                                },
                                placeholder = {
                                    Text(text = "Select an Option")
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .background(color = Color.White)
                            )

                            ExposedDropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = Color.White)
                            ) {
                                val items = listOf(
                                    PriorityItem("High", Color.Red),
                                    PriorityItem("Medium", Color.Magenta),
                                    PriorityItem("Low", Color.Yellow),
                                )
                                for (priorityItem in items) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = priorityItem.priority
                                            )
                                        },
                                        onClick = {
                                            priority = priorityItem.priority
                                            isExpanded = false
                                        },
                                        trailingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_circle_24),
                                                contentDescription = "Icon",
                                                tint = priorityItem.colorCode
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(color = Color.White)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 0.dp, top = 10.dp, end = 0.dp, bottom = 0.dp
                    ),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {

                ElevatedButton(
                    onClick = {
                        viewModel.insertNote(
                            noteItem = NoteItem(
                                noteTitle = noteTitle,
                                noteDescription = noteDescription,
                                priority = priority
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
        viewModel = viewModel(),
        noteId = 0
    )
}