package com.example.notes

import androidx.lifecycle.*
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import kotlinx.coroutines.launch

class NotesViewModel(private val noteDao: NoteDao): ViewModel(){

    val allNotes: LiveData<List<Note>> = noteDao.getNotes().asLiveData()
    private fun getNewNoteEntry(noteName: String, noteText: String): Note{
        return Note(noteName= noteName,
                    noteText = noteText)
    }

    private fun insert(note: Note){
    viewModelScope.launch {
        noteDao.insert(note)
        }
    }

    fun addNewNote(noteName: String, noteText: String){
        val newNote: Note=getNewNoteEntry(noteName, noteText)
        insert(newNote)
    }

    fun isEntryValid(noteName: String, noteText: String): Boolean{
        return noteName.isNotBlank() && noteText.isNotBlank()
    }
    fun retrieveNote(id: Int): LiveData<Note>{
        return  noteDao.getNote(id).asLiveData()
    }
    private fun getUpdatedNoteEntry(
        noteId: Int,
        noteName: String,
        noteText: String
    ): Note{
        return Note(
            noteId,
            noteName,
            noteText
        )
    }
    fun updateNote(
        noteId: Int,
        noteName: String,
        noteText: String
    ){
        val updatedNote = getUpdatedNoteEntry(noteId,noteName,noteText)
        updateNote(updatedNote)
         }
    private fun updateNote(note: Note){
        viewModelScope.launch{
            noteDao.update(note)
        }
    }
    fun deleteNote(note: Note){
        viewModelScope.launch{
            noteDao.delete(note)
        }
    }

}
class NotesViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}