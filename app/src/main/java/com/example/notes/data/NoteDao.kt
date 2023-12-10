package com.example.notes.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * FROM note ORDER BY noteName ASC")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id= :id")
    fun getNote(id: Int): Flow<Note>
}