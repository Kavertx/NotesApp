package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notes.databinding.NoteListFragmentBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NoteListFragment : Fragment() {

    private val viewModel: NotesViewModel by activityViewModels {
        NotesViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }
    private var _binding: NoteListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NoteListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = NoteListAdapter{
            val action = NoteListFragmentDirections.actionNoteListFragmentToNoteDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter= adapter
        viewModel.allNotes.observe(this.viewLifecycleOwner) { notes ->
            notes.let {
                adapter.submitList(it)
            }
        }
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2) // works
        binding.floatingActionButton.setOnClickListener {
            val action = NoteListFragmentDirections.actionNoteListFragmentToAddNoteFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
    }
}
