package com.example.projectSnips.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.projectSnips.Data.PhotoRepository
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.databinding.FragmentStorageBinding
import com.google.android.material.snackbar.Snackbar

class UpdateSnipFragment : StorageFragment()  {
    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!

    lateinit var snip: Photos


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
        Glide.with(this).load(imageUri).into(binding.ivSelectedImg)

        binding.etCaption.setText(snip.caption)
        if(snip.visibility == "private") {
            binding.swPrivate.isChecked = true
        }
        photoRepository = PhotoRepository(this.requireContext())

        binding.btnSaveSnip.text = "Apply Changes"
        binding.btnEditSnip.visibility = View.VISIBLE
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
            snip.caption = binding.etCaption.text.toString()
            if(binding.swPrivate.isChecked){
                snip.visibility = "private"
            }else {
                snip.visibility = "public"
            }
            upload()
        }
    }

    override fun upload(){
        Log.d("INSIDE_UPLOAD","$snip")
        snip.caption = binding.etCaption.text.toString()
        //snip.visibility = //binding.switch
        photoRepository.updateSnip(snip)
        Snackbar.make(binding.flStorage, "Successfully edited ${snip.caption}", Snackbar.LENGTH_SHORT).show()
        val action = UpdateSnipFragmentDirections.actionUpdateSnipFragmentToBodyFragment()
        findNavController().navigate(action)
    }

}