package com.example.notes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo val noteName: String,
    @ColumnInfo val noteText: String
    )