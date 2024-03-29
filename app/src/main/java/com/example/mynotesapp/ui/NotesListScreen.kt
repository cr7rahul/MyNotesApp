package com.example.mynotesapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mynotesapp.R
import com.example.mynotesapp.room.data.NoteItem
import com.example.mynotesapp.ui.navigation.NotesAppScreens
import com.example.mynotesapp.ui.viewmodel.NotesViewModel
import com.example.mynotesapp.ui.viewmodel.RetrieveNotesState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(navController: NavController, viewModel: NotesViewModel) {

    val notesViewModel by viewModel.mutableRetrieveNoteList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.retrieveNotesList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Home")
                },
                colors = topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painterResource(id = R.drawable.baseline_search_24),
                            contentDescription = "Add Notes",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NotesAppScreens.NoteDetails.route + "/0")
                },
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Notes")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            notesViewModel.let {
                when (it) {
                    is RetrieveNotesState.Error -> {

                    }

                    is RetrieveNotesState.Success -> {
                        if (it.result.isEmpty()) {
                            NoNotes()
                        } else {
                            NoteContent(it.result, navController, viewModel)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteContent(items: List<NoteItem>, navController: NavController, viewModel: NotesViewModel) {
    val deleteNotesViewModel by viewModel.mutableDeleteNote.observeAsState()

    // Alert Dialog State
    val openAlertDialog = remember {
        mutableStateOf(false)
    }
    // Note ID State
    val noteId = remember {
        mutableIntStateOf(0)
    }

    /*LaunchedEffect(Unit){
        viewModel.deleteNotesById(noteId.intValue)
    }*/

    if (openAlertDialog.value) {
        EditDeleteDialog(
            onEditRequest = {
                navController.navigate(NotesAppScreens.NoteDetails.route + "/${noteId.intValue}")
                openAlertDialog.value = false
            },
            onDeleteRequest = {
                viewModel.deleteNotesById(noteId.intValue)
                deleteNotesViewModel.let {
                    openAlertDialog.value = false
                }
                viewModel.retrieveNotesList()
            },
            dialogTitle = "Edit/Remove Note",
            dialogText = "You can Edit/ Remove the note",
            icon = Icons.Default.Info
        )
    }
    LazyColumn {
        items(items) { noteItem ->
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp),
                onClick = {
                    openAlertDialog.value = true
                    noteId.intValue = noteItem.id
                }
            ) {
                NotesTitle(item = noteItem.noteTitle)
                NoteDescription(item = noteItem.noteDescription)
            }
        }

    }
}

@Composable
fun NotesTitle(item: String) {
    val gradientColors = listOf(
        Color.Cyan, Color.Blue, Color.Magenta
    )
    Text(
        text = item,
        modifier = Modifier
            .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp),
        color = Color.Black,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = TextStyle(
            brush = Brush.linearGradient(
                colors = gradientColors
            )
        )
    )
}

@Composable
fun NoteDescription(item: String) {
    Text(
        text = item,
        modifier = Modifier
            .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp),
        color = Color.DarkGray,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun NoNotes() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No Notes added. \nClick on the Add note button to Add new Note",
            modifier = Modifier
                .padding(10.dp)
                .wrapContentWidth(Alignment.CenterHorizontally),
            fontSize = 18.sp
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun NotesListScreenPreview() {
    NoNotes()
}