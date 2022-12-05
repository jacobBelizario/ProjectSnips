package com.example.projectSnips.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectSnips.databinding.FragmentScreen1Binding

class Screen1Fragment : Fragment() {

    val TAG:String = "SCREEN1-FRAGMENT"

    private var _binding: FragmentScreen1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScreen1Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.btnScreen2.setOnClickListener {
//            Log.d("TAG", "Go to screen 2 pressed")
//            val nametosend = binding.etName.text.toString()
//            val hourlyRate = binding.etHourlyRate.text.toString().toFloatOrNull()
//
//            if(hourlyRate == null) {
//                binding.etHourlyRate.error = "Enter valid data"
//                return@setOnClickListener
//            }
//            val action = Screen1FragmentDirections.actionScreen1FragmentToScreen2Fragment()
//            findNavController().navigate(action)
//        }
//        binding.btnScreen3.setOnClickListener {
//            Log.d("TAG", "Go to screen 3 pressed")
//            val action = Screen1FragmentDirections.actionScreen1FragmentToScreen3Fragment()
//            findNavController().navigate(action)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}