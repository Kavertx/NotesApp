package com.example.notesattempt

import android.app.Application
import com.example.notesattempt.data.NoteRoomDatabase

class NoteApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database: NoteRoomDatabase by lazy { NoteRoomDatabase.getDatabase(this) }
}