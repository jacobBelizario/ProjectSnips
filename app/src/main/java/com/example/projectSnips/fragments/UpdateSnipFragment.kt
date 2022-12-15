package com.example.projectSnips.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.Data.PhotoRepository
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.R
import com.example.projectSnips.databinding.FragmentEditBinding
import com.example.projectSnips.databinding.FragmentStorageBinding

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class UpdateSnipFragment : StorageFragment()  {
    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!

    lateinit var snip: Photos


    //lateinit var imageUri: Uri
    //lateinit var photoRepository: PhotoRepository
    //lateinit var sharedPrefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStorageBinding.inflate(inflater, container, false)
        val view = binding.root

        val args: UpdateSnipFragmentArgs by navArgs()
        snip = args.snipToEdit
        imageUri = snip.url.toUri()



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
        //viewGalery()

        Glide.with(this).load(imageUri).into(binding.ivSelectedImg)

        binding.etCaption.setText(snip.caption)
        //switch from public to private

        photoRepository = PhotoRepository(this.requireContext())

        binding.btnSaveSnip.text = "Apply Changes"
        binding.btnEditSnip.text = "Back to My Snips"

        binding.btnEditSnip.setOnClickListener {
            val action = UpdateSnipFragmentDirections.actionUpdateSnipFragmentToPersonalFragment()
            findNavController().navigate(action)
        }

        binding.btnSaveSnip.setOnClickListener {
            saveToCloud()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun saveToCloud() {
        if(binding.etCaption.text.isEmpty()){
            binding.etCaption.error = "Caption cannot be empty"
        } else {
            upload()
        }
    }

    override fun upload(){
        Toast.makeText(requireContext(), "UPDATING",Toast.LENGTH_SHORT)
        snip.caption = binding.etCaption.text.toString()
        //snip.visibility = //binding.switch
        photoRepository.updateSnip(snip)
    }

}