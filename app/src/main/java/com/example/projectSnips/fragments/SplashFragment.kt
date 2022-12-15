package com.example.projectSnips.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.R


class SplashFragment : Fragment() {
    val TAG:String = "SCREEN1-FRAGMENT"
    private val splashTimeOut:Long = 3000
    lateinit var sharedPrefs : SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefs = this.requireActivity().getSharedPreferences("com_example_projectSnips",
            AppCompatActivity.MODE_PRIVATE
        )
        var data = Datasource.getInstance()
        Handler(Looper.getMainLooper()).postDelayed({
        if(sharedPrefs.contains("KEY_LOGGEDIN_EMAIL")){
            val action = SplashFragmentDirections.actionSplashFragmentToScreen1Fragment()
               findNavController().navigate(action)
        } else {
            val action = SplashFragmentDirections.actionSplashFragmentToScreen2Fragment()
            findNavController().navigate(action)
        }
    }, splashTimeOut)
    }


}