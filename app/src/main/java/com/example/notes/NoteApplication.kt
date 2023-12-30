package com.example.notes

import android.app.Application
import com.example.notes.data.NoteRoomDatabase

class NoteApplication : Application() {
    val database: NoteRoomDatabase by lazy { NoteRoomDatabase.getDatabase(this) }
}