package com.example.projectSnips.fragments


import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.projectSnips.Data.Datasource
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
import io.grpc.Context
import java.io.File

class BodyFragment : Fragment(), LifecycleOwner{

    lateinit var photoRepository: PhotoRepository
    var adapterSnips: SnipAdapter? = null
    private var _binding: FragmentBodyBinding? = null
    private val binding get() = _binding!!


    override fun onResume() {
        super.onResume()
        photoRepository = PhotoRepository(this.requireContext())
        photoRepository.getAllSnips()

        photoRepository.allPhotos.observe(this) { photoList ->
            if (photoList != null) {
                binding.pbSpinner.visibility = View.GONE
                adapterSnips = view?.let { SnipAdapter(it.context, Datasource.getInstance().datalist) }
                Log.d("ISTHISIT","${Datasource.getInstance().datalist}")
                binding.snipDisplay.setHasFixedSize(true)
                binding.snipDisplay.layoutManager = GridLayoutManager(activity, 3)
                binding.snipDisplay.adapter = adapterSnips
            }
        }
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
        binding.pbSpinner.visibility = View.VISIBLE
    }

}