package com.example.mynotesapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotesapp.room.data.NoteItem
import com.example.mynotesapp.room.interactors.DeleteNotesUseCase
import com.example.mynotesapp.room.interactors.InsertNotesInfoUseCase
import com.example.mynotesapp.room.interactors.RetrieveNoteDetailsUseCase
import com.example.mynotesapp.room.interactors.RetrieveNotesUseCase
import com.example.mynotesapp.room.interactors.UpdateNotesInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val insertNotesInfoUseCase: InsertNotesInfoUseCase,
    private val updateNotesInfoUseCase: UpdateNotesInfoUseCase,
    private val deleteAllNotesUseCase: DeleteNotesUseCase,
    private val retrieveNotesUseCase: RetrieveNotesUseCase,
    private val retrieveNoteDetailsUseCase: RetrieveNoteDetailsUseCase
) : ViewModel() {

    private val _mutableInsertNote: MutableLiveData<Unit> = MutableLiveData()
    val mutableInsertNote: LiveData<Unit> = _mutableInsertNote

    private val _mutableUpdateNote: MutableLiveData<Unit> = MutableLiveData()
    val mutableUpdateNote: LiveData<Unit> = _mutableUpdateNote

    private val _mutableDeleteNote: MutableLiveData<Unit> = MutableLiveData()
    val mutableDeleteNote: LiveData<Unit> = _mutableDeleteNote

    private val _mutableRetrieveNoteList = MutableStateFlow(RetrieveNotesState.Success(emptyList()))
    val mutableRetrieveNoteList: StateFlow<RetrieveNotesState> = _mutableRetrieveNoteList

    private val _mutableRetrieveNoteListError =
        MutableStateFlow(RetrieveNotesState.Error(Throwable()))
    val mutableRetrieveNoteListError: StateFlow<RetrieveNotesState> = _mutableRetrieveNoteListError

    private val _mutableRetrieveNoteDetails = MutableStateFlow(
        RetrieveNoteDetailsState.Success(
            NoteItem(noteTitle = "", noteDescription = "")
        )
    )
    val mutableRetrieveNoteDetails: StateFlow<RetrieveNoteDetailsState> =
        _mutableRetrieveNoteDetails

    private val _mutableRetrieveNoteDetailsError =
        MutableStateFlow(RetrieveNoteDetailsState.Error(Throwable()))
    val mutableRetrieveNoteDetailsError: StateFlow<RetrieveNoteDetailsState> = _mutableRetrieveNoteDetailsError

    fun insertNote(noteItem: NoteItem) = viewModelScope.launch {
        _mutableInsertNote.value = insertNotesInfoUseCase.invoke(noteItem)
    }

    fun updateNote(noteItem: NoteItem) = viewModelScope.launch {
        _mutableUpdateNote.value = updateNotesInfoUseCase.invoke(noteItem)
    }

    fun deleteAllNote() = viewModelScope.launch {
        _mutableDeleteNote.value = deleteAllNotesUseCase.invoke()
    }

    fun retrieveNotesList() = viewModelScope.launch {
        retrieveNotesUseCase.invoke()
            .catch {
                _mutableRetrieveNoteListError.value = RetrieveNotesState.Error(it)
            }
            .collect {
                _mutableRetrieveNoteList.value = RetrieveNotesState.Success(it)
            }
    }

    fun retrieveNoteDetails(id: Int) = viewModelScope.launch {
        retrieveNoteDetailsUseCase.invoke(id)
            .catch {
                _mutableRetrieveNoteDetailsError.value = RetrieveNoteDetailsState.Error(it)
            }
            .collect {
                _mutableRetrieveNoteDetails.value = RetrieveNoteDetailsState.Success(it)
            }
    }
}

sealed class RetrieveNotesState {
    data class Success(val result: List<NoteItem>) : RetrieveNotesState()
    data class Error(val error: Throwable) : RetrieveNotesState()
}

sealed class RetrieveNoteDetailsState {
    data class Success(val result: NoteItem) : RetrieveNoteDetailsState()
    data class Error(val error: Throwable) : RetrieveNoteDetailsState()
}