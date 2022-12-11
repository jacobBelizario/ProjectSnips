package com.example.projectSnips.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.databinding.FragmentHeaderBinding

// TODO: Rename parameter arguments, choose names that match

class HeaderFragment : Fragment() {
    private var _binding: FragmentHeaderBinding? = null
    private val binding get() = _binding!!
    lateinit var sharedPrefs : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHeaderBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //read from shared preferences to store default value of user when logged in
        sharedPrefs = this.requireActivity().getSharedPreferences("com_example_projectSnips",
            AppCompatActivity.MODE_PRIVATE
        )
//        if(sharedPrefs.contains("KEY_LOGGEDIN_EMAIL")){
//            binding.etUsername.setText(sharedPrefs.getString("KEY_LOGGEDIN_EMAIL",""))
//        }
        binding.tvHeaderHeading.text = Datasource.getInstance().heading
        binding.tvUser.text = sharedPrefs.getString("KEY_LOGGEDIN_EMAIL","")


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}