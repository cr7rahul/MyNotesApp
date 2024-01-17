package com.example.mynotesapp.room.interactors

import com.example.mynotesapp.room.repository.NotesRepository
import javax.inject.Inject

class DeleteNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {

    suspend operator fun invoke() {
        notesRepository.deleteNote()
    }
}