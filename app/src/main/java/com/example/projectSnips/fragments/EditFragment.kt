package com.example.projectSnips.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectSnips.databinding.FragmentEditBinding
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView


//PhotoEditorView mPhotoEditorView = findViewById(R.id.photoEditorView);
//
//mPhotoEditorView.getSource().setImageResource(R.drawable.got);
class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var photoEditorView = PhotoEditorView(this.requireContext())
        photoEditorView.source.setImageResource(com.example.projectSnips.R.drawable.add_btn)


        var photoEditor = PhotoEditor.Builder(this.requireContext(),photoEditorView)
            .setPinchTextScalable(true)
            .setClipSourceImage(true)
            .build();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}