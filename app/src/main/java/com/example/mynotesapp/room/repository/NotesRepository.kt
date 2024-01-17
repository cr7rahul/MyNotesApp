package com.example.mynotesapp.room.repository

import com.example.mynotesapp.room.dao.NoteItemDao
import com.example.mynotesapp.room.data.NoteItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val notesDao: NoteItemDao
) {

    /**
     * Retrieve Note List
     */
    suspend fun retrieveNotes(): Flow<List<NoteItem>> = flow {
        emit(
            notesDao.retrieveNotes()
        )
    }
        .shareIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed()
        )

    /**
     * Retrieve Note Details
     * @param id - selected note item id
     */
    suspend fun retrieveNoteDetails(id: Int): Flow<NoteItem> = flow {
        emit(
            notesDao.retrieveNoteDetails(id)
        )
    }.shareIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed()
    )

    /**
     * Insert Note
     */
    suspend fun insertNote(noteItem: NoteItem) = notesDao.insertNote(noteItem)

    /**
     * Update Note
     */
    suspend fun updateNote(noteItem: NoteItem) = notesDao.updateNote(noteItem)

    /**
     * Delete all notes
     */
    suspend fun deleteNote() = notesDao.deleteNote()
}