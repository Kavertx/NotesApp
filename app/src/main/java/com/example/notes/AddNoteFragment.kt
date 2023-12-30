package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.data.Note
import com.example.notes.databinding.FragmentAddNoteBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddNoteFragment : Fragment() {

    private val viewModel: NotesViewModel by activityViewModels {
        NotesViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }
    private val navigationArgs: NoteDetailFragmentArgs by navArgs()
    private val binding get() = _binding!!
    private var _binding: FragmentAddNoteBinding? = null
    lateinit var note: Note
    // This property is only valid between onCreateView and
    // onDestroyView.


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun isEntryValid(): Boolean{
        return   viewModel.isEntryValid(
            noteName = binding.noteName.text.toString(),
            noteText = binding.noteText.text.toString()
        )
    }

    private fun addNewNote(){
        if(isEntryValid()){
            viewModel.addNewNote(
                noteName = binding.noteName.text.toString(),
                noteText = binding.noteText.text.toString())
        }
        val action = AddNoteFragmentDirections.actionAddNoteFragmentToNoteListFragment()
        findNavController().navigate(action)
    }

    private fun updateItem(){
        if (isEntryValid()){
            viewModel.updateNote(
                this.navigationArgs.noteId,
                this.binding.noteName.getText().toString(),
                this.binding.noteText.getText().toString()
            )
            val action = AddNoteFragmentDirections.actionAddNoteFragmentToNoteListFragment()
            findNavController().navigate(action)
        }
    }

    private fun bind(note: Note){
        binding.apply {
            noteName.setText(note.noteName, TextView.BufferType.SPANNABLE)
            noteText.setText(note.noteText, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener{ updateItem()}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.noteId
        if (id>0){
            viewModel.retrieveNote(id).observe(this.viewLifecycleOwner){ selectedNote ->
                note = selectedNote
                bind(note)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewNote()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}