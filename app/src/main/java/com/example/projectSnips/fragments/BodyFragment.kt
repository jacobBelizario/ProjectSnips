package com.example.projectSnips.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.projectSnips.Data.PhotoRepository
import com.example.projectSnips.R
import com.example.projectSnips.SnipAdapter
import com.example.projectSnips.databinding.FragmentBodyBinding
import com.example.projectSnips.databinding.LoginScreenBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.firebase.storage.ktx.component3
import com.google.firebase.storage.ktx.storage
import java.io.File

class BodyFragment : Fragment(){

    lateinit var storage:FirebaseStorage
    private var storageLocation: String = "public/"
    lateinit var snipsList : ArrayList<Bitmap>
    var adapter: SnipAdapter? = null

    private var _binding: FragmentBodyBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        snipsList = ArrayList()

        Log.d("onCreate", snipsList.toString())
        }

    override fun onResume() {
        super.onResume()
        PhotoRepository().snipDisplays.observe(this.viewLifecycleOwner, Observer {
            if (it != null){
                for(snip in it){
                    snipsList.add(snip)
                    adapter?.notifyDataSetChanged()
                }
            }

        })
        Log.d("onResume", snipsList.toString())
    }

    fun updateUI(snips: ArrayList<Bitmap>){

        //snips.observe(this, Observer {
            //if (it != null){

                    //snipsList.clear()
                    //binding.snipDisplay.adapter = SnipAdapter(requireContext(), snips)
                    snipsList = snips
                    Log.d("updateUI", adapter.toString())
                    //adapter = SnipAdapter(requireContext(), snips)
                    //binding.snipDisplay.adapter = adapter
                    adapter?.notifyDataSetChanged()


          //  }
       // })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("onCreateView", "here")
        _binding = FragmentBodyBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        snipsList = ArrayList()

        adapter = SnipAdapter(view.context, snipsList)
        Log.d("onViewCreated", adapter.toString())
        binding.snipDisplay.setHasFixedSize(true)
        binding.snipDisplay.layoutManager = LinearLayoutManager(view.context)
        binding.snipDisplay.adapter = adapter

        PhotoRepository().getSnipsFrom("/public")



    }


}