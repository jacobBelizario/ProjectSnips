package com.example.projectSnips.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectSnips.databinding.FragmentScreen2Binding


class Screen2Fragment : Fragment() {

    val TAG:String = "SCREEN2-FRAGMENT"
    //for binding
    private var _binding: FragmentScreen2Binding? = null
    private val binding get() = _binding!!
    //for recieving data from safe args
//    private val args:Screen2FragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScreen2Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnScreen3.setOnClickListener {
            Log.d(TAG, "Button screen 3 pressed")
            val action = Screen2FragmentDirections.actionScreen2FragmentToScreen3Fragment()
            findNavController().navigate(action)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}