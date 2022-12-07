package com.example.projectSnips.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.R
import javax.sql.DataSource


class SplashFragment : Fragment() {
    val TAG:String = "SCREEN1-FRAGMENT"
    private val splashTimeOut:Long = 3000
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var data = Datasource.getInstance()
        Handler(Looper.getMainLooper()).postDelayed({
            if(data.user == ""){
                val action = SplashFragmentDirections.actionSplashFragmentToScreen2Fragment()
                findNavController().navigate(action)
            }else{
                val action = SplashFragmentDirections.actionSplashFragmentToScreen1Fragment()
                findNavController().navigate(action)
            }
        }, splashTimeOut)

//        binding.btnSignup.setOnClickListener {
//            val action = LoginFragmentDirections.actionScreen1FragmentToScreen2Fragment()
//            findNavController().navigate(action)
//        }
    }


}