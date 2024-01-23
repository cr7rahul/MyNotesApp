package com.example.mynotesapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynotesapp.room.dao.NoteItemDao
import com.example.mynotesapp.room.data.NoteItem

@Database(
    entities = [
        NoteItem::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteItemDao(): NoteItemDao
}