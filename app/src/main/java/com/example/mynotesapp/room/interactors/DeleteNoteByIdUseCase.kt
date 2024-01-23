package com.example.mynotesapp.room.interactors

import com.example.mynotesapp.room.repository.NotesRepository
import javax.inject.Inject

class DeleteNoteByIdUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {

    suspend operator fun invoke(id: Int) {
        return notesRepository.deleteNoteById(id)
    }
}