package com.example.projectSnips.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectSnips.Data.UserRepository
import com.example.projectSnips.databinding.LoginScreenBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    lateinit var sharedPrefs : SharedPreferences
    val TAG:String = "SCREEN1-FRAGMENT"
    lateinit var userRepository : UserRepository
    private lateinit var mAuth: FirebaseAuth

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
        mAuth = FirebaseAuth.getInstance()
        userRepository = UserRepository(this.requireContext())
        FirebaseApp.initializeApp(this.requireContext())

        //read from shared preferences to store default value of user when logged in
        sharedPrefs = this.requireActivity().getSharedPreferences("com_example_projectSnips",
            AppCompatActivity.MODE_PRIVATE
        )
        if(sharedPrefs.contains("KEY_LOGGEDIN_EMAIL")){
            binding.etUsername.setText(sharedPrefs.getString("KEY_LOGGEDIN_EMAIL",""))
        }


        binding.btnLogin.setOnClickListener {
            validateData()
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


    private fun validateData() {
        var validData = true
        var email = ""
        var password = ""
        if (binding.etUsername.text.toString().isEmpty()) {
            binding.etUsername.error = "Email Cannot be Empty"
            validData = false
        } else {
            email = binding.etUsername.text.toString()
        }
        if (binding.etPassword.text.toString().isEmpty()) {
            binding.etPassword.error ="Password Cannot be Empty"
            validData = false
        }else{
            password = binding.etPassword.text.toString()
        }
        if (validData) {
            Log.d("USER","$email $password")
            signIn(email, password)
        } else {
            Toast.makeText(this.requireContext(), "Please provide correct inputs", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this.requireActivity()
            ) { task ->
                if (task.isSuccessful) {
                    sharedPrefs = this.requireActivity().getSharedPreferences("com_example_projectSnips",
                        AppCompatActivity.MODE_PRIVATE
                    )
                 userRepository.searchUserWithEmail(email)
                    writeToPrefs("EMAIL",email)
                    writeToPrefs("PASSWORD",password)

                    val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                    findNavController().navigate(action)


                } else {
                    Log.e(
                        "TEST_LOGIN",
                        "onComplete: " + task.exception + task.exception!!
                            .localizedMessage
                    )
                }
            }
    }

    fun writeToPrefs(type:String,value:String){
        var keyName = "KEY_LOGGEDIN_${type}"
        with(sharedPrefs.edit()){
            putString(keyName, value)
            apply()
        }
    }
}