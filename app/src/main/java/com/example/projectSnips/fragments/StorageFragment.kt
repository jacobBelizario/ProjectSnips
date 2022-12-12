package com.example.projectSnips.fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectSnips.Data.PhotoRepository
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.databinding.FragmentStorageBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*


class StorageFragment : Fragment() {
    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!
    private val pickImage = 100
    lateinit var imageUri: Uri
    lateinit var photoRepository: PhotoRepository
    lateinit var sharedPrefs : SharedPreferences

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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewGalery()

        photoRepository = PhotoRepository(this.requireContext())
        //get the output directory or storage of pictures
        binding.ivSelectedImg.setOnClickListener {
            viewGalery()
        }

        binding.btnEditSnip.setOnClickListener {
            val action = StorageFragmentDirections.actionStorageFragmentToEditFragment()
            findNavController().navigate(action)
        }

        binding.btnSaveSnip.setOnClickListener {
            saveToCloud()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data!!
            Log.d("URI","$imageUri")
            binding.ivSelectedImg.setImageURI(imageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun viewGalery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    fun saveToCloud() {
        if(binding.etCaption.text.isEmpty()){
            binding.etCaption.error = "Caption cannot be empty"
        } else {
            upload()
        }
    }

    fun upload(){
        val progressDialog = ProgressDialog(this.requireActivity())
        progressDialog.setMessage("UPLOADING...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val formatter = SimpleDateFormat("yyyy_MM_DD_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val imageName = binding.etCaption.text
        var filename = "$imageName${formatter.format(now)}"
        val storageReference = FirebaseStorage.getInstance().getReference("/public/$filename")
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                binding.ivSelectedImg.setImageURI(null)
//                binding.etCaption.text = null
                if(progressDialog.isShowing) progressDialog.dismiss()
                // now comes the fun part of getting the url from the upload and pushing to database
                var storage = Firebase.storage
                val storageRef = storage.reference
                storageRef.child("/public/$filename").downloadUrl.addOnSuccessListener {
                    url ->
                    //now we have the url we now can save to repository
                    //create a new photo object we have everything we need
                    sharedPrefs = this.requireActivity().getSharedPreferences("com_example_projectSnips",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    var email: String? = sharedPrefs.getString("KEY_LOGGEDIN_EMAIL","")
                    photoRepository.addPhotoToDb(Photos(caption = binding.etCaption.text.toString(),url= url.toString(),email= email!!,visibility ="public"))
                    binding.etCaption.text = null

                    // show snackbar to confirm that it is uploaded
                    Snackbar.make(binding.flStorage, "Successfully uploaded $filename", Snackbar.LENGTH_SHORT).show()
                    val action = StorageFragmentDirections.actionStorageFragmentToBodyFragment()
                    findNavController().navigate(action)
                }
            }.addOnFailureListener{
                if(progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this.requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
            }
    }

}