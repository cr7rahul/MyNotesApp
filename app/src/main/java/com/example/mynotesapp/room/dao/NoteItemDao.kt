package com.example.mynotesapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mynotesapp.room.data.NoteItem

@Dao
interface NoteItemDao {

    @Query("SELECT * FROM note_item")
    suspend fun retrieveNotes(): List<NoteItem>

    @Query("SELECT id, title, description FROM note_item WHERE id =:id")
    suspend fun retrieveNoteDetails(id: Int): NoteItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteItem: NoteItem)

    @Update
    suspend fun updateNote(noteItem: NoteItem)

    @Query("DELETE FROM note_item")
    suspend fun deleteNote()
}