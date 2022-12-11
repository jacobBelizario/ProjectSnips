package com.example.projectSnips.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectSnips.R


class HomeFragment : Fragment() {
//    private var _binding: FragmentHomeBinding? = null
//    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val navHostFragment =
//            parentFragmentManager.findFragmentById(binding.fragmentBody.id) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        view?.findViewById<BottomNavigationView>(binding.bottomNavigationView.id)
//            ?.setupWithNavController(navController)

        val navHostFragment = parentFragmentManager
    }


}