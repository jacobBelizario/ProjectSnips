package com.example.projectSnips.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectSnips.databinding.LoginScreenBinding

class LoginFragment : Fragment() {

    val TAG:String = "SCREEN1-FRAGMENT"

    private var _binding: LoginScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            if(binding.etUsername.text.toString().isEmpty()){
                binding.etUsername.error = "Please enter a valid email"
            }
            if(binding.etPassword.text.toString().isEmpty()){
                binding.etPassword.error = "Please enter a password"
            }
        }
        binding.btnSignup.setOnClickListener {
            val action = LoginFragmentDirections.actionScreen1FragmentToScreen2Fragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}