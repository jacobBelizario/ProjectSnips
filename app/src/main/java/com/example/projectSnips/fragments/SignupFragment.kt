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
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.Data.User
import com.example.projectSnips.network.UserRepository
import com.example.projectSnips.databinding.SignupScreenBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class SignupFragment : Fragment() {
    lateinit var sharedPrefs : SharedPreferences
    val TAG:String = "SCREEN2-FRAGMENT"
    //for binding
    private var _binding: SignupScreenBinding? = null
    private val binding get() = _binding!!
    lateinit var userRepository : UserRepository
    private lateinit var mAuth: FirebaseAuth
    //for recieving data from safe args
//    private val args:Screen2FragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignupScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        userRepository = UserRepository(this.requireContext())
        FirebaseApp.initializeApp(this.requireContext())


        binding.btnSignup.setOnClickListener {
            validateData()
        }

        binding.btnLogin.setOnClickListener {
            val action = SignupFragmentDirections.actionScreen2FragmentToScreen1Fragment()
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
            createAccount(email, password)
        } else {
            Toast.makeText(this.requireContext(), "Please provide correct inputs", Toast.LENGTH_SHORT).show()
        }
    }
    private fun createAccount(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this.requireActivity()
            ) { task ->
                if (task.isSuccessful) {
                    sharedPrefs = this.requireActivity().getSharedPreferences("com_example_projectSnips",
                        AppCompatActivity.MODE_PRIVATE
                    )
                   userRepository.addUserToDB(User(email = email, password = password, photoList = arrayListOf()))
                    writeToPrefs("EMAIL",email)
                    writeToPrefs("PASSWORD",password)

                    Datasource.getInstance().email = email

                    //get that user and store the data in the singleton
                    val action = SignupFragmentDirections.actionSignupFragmentToHomeFragment()
                    findNavController().navigate(action)
                } else {
                    Log.e(
                        "TEST_SIGNUP",
                        "onComplete: Failed to create user with email and password" + task.exception + task.exception!!
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