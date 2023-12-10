package com.example.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.data.Note
import com.example.notes.databinding.FragmentNoteDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NoteDetailFragment : Fragment() {
    private val viewModel: NotesViewModel by activityViewModels {
        NotesViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }
    lateinit var note: Note
    private val navigationArgs: NoteDetailFragmentArgs by navArgs()

    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    private fun bind(note: Note){
        binding.apply {
            noteName.text = note.noteName
            noteText.text = note.noteText
            deleteAction.setOnClickListener { showConfirmationDialog() }
            noteName.setOnClickListener { editNote() }
            noteText.setOnClickListener { editNote() }
        }
    }
    private fun editNote(){
        val action = NoteDetailFragmentDirections.actionNoteDetailFragmentToAddNoteFragment(
            getString(R.string.edit_fragment_title),
            note.id,

        )
        this.findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.noteId
        viewModel.retrieveNote(id).observe(this.viewLifecycleOwner){ selectedNote ->
            note = selectedNote
            bind(note)
        } // TODO: figure out how to make hold clicking a view open delete menu
        // TODO: add a second entity for the deleted items/notes (can leave for last)
        //TODO: editing notes
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteNote()
            }
            .show()
    }

    /**
     * Deletes the current item and navigates to the list fragment.
     */
    private fun deleteNote() {
        viewModel.deleteNote(note)
        findNavController().navigateUp()
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}