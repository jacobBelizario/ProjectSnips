package com.example.projectSnips.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectSnips.databinding.FragmentScreen3Binding


class Screen3Fragment : Fragment() {

    val TAG:String = "SCREEN3-FRAGMENT"
    private var _binding: FragmentScreen3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScreen3Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnScreen2.setOnClickListener {
            Log.d(TAG, "Button go to screen 2 pressed")
//            val action = Screen3FragmentDirections.actionScreen3FragmentToScreen2Fragment()
//            findNavController().navigate(action)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}