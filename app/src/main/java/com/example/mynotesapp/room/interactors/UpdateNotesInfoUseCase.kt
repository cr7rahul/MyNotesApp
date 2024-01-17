package com.example.mynotesapp.room.interactors

import com.example.mynotesapp.room.data.NoteItem
import com.example.mynotesapp.room.repository.NotesRepository
import javax.inject.Inject

class UpdateNotesInfoUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {

    suspend operator fun invoke(noteItem: NoteItem) {
        return notesRepository.updateNote(noteItem)
    }
}