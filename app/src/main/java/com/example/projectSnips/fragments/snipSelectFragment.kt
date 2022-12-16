package com.example.projectSnips.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectSnips.databinding.FragmentSnipSelectBinding


class snipSelectFragment : Fragment() {
    private var _binding: FragmentSnipSelectBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSnipSelectBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnTakePhoto.setOnClickListener {
            val action = snipSelectFragmentDirections.actionSnipSelectFragmentToSnipFragment()
            findNavController().navigate(action)
        }
        binding.btnChooseStorage.setOnClickListener {
            val action = snipSelectFragmentDirections.actionSnipSelectFragmentToStorageFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}