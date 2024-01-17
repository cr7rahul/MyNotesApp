package com.example.mynotesapp.room.interactors

import com.example.mynotesapp.room.data.NoteItem
import com.example.mynotesapp.room.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RetrieveNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {

    suspend operator fun invoke(): Flow<List<NoteItem>> {
        return notesRepository.retrieveNotes()
    }
}