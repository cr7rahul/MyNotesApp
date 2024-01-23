package com.example.mynotesapp.di

import android.content.Context
import androidx.room.Room
import com.example.mynotesapp.room.AppDatabase
import com.example.mynotesapp.room.dao.NoteItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNoteItemDao(appDatabase: AppDatabase): NoteItemDao {
        return appDatabase.noteItemDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "NoteDb"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}