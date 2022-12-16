package com.example.projectSnips.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.MainActivity
import com.example.projectSnips.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = this.requireActivity().getSharedPreferences("com_example_projectSnips",
            AppCompatActivity.MODE_PRIVATE
        )
        var email = sharedPreferences.getString("KEY_LOGGEDIN_EMAIL","")
        var password =sharedPreferences.getString("KEY_LOGGEDIN_PASSWORD","")


        Glide.with(binding.root).load("https://firebasestorage.googleapis.com/v0/b/projectsnips-8bfe2.appspot.com/o/static%2Fdefault.jpg?alt=media&token=98b37347-df99-40bc-8bca-c8a979a100b0").into(binding.ivProfilePicture)

        binding.tvEmail.text = email
        binding.tvPassword.text = password

        binding.btnSignOut.setOnClickListener {
            signout()
        }
        binding.btnClearData.setOnClickListener {
            clearData()
            signout()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun signout() {
        Datasource.getInstance().reset()
//        val action = ProfileFragmentDirections.actionProfileFragment2ToSplashFragment3()
//        findNavController().navigate(action)
        val intent = Intent(this.requireContext(), MainActivity::class.java)
        startActivity(intent)
    }
    fun clearData(){
        sharedPreferences.edit().clear().commit()
    }


}