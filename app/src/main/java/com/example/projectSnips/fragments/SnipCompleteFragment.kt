package com.example.projectSnips.fragments

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.Data.PhotoRepository
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.databinding.FragmentSnipCompleteBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*


class SnipCompleteFragment : Fragment() {
    private var _binding: FragmentSnipCompleteBinding? = null
    private val binding get() = _binding!!
    lateinit var sharedPrefs : SharedPreferences
    lateinit var photoRepository: PhotoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSnipCompleteBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var imageUri = Datasource.getInstance().imageURI
        photoRepository = PhotoRepository(this.requireContext())
        Glide.with(binding.root).load(imageUri).into(binding.ivSelectedImg)

        binding.btnSaveSnip.setOnClickListener {
            saveToCloud()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun saveToCloud() {
        if(binding.etCaption.text.isEmpty()){
            binding.etCaption.error = "Caption cannot be empty"
        } else {
            upload()
        }
    }

    fun upload(){
        var imageUri = Datasource.getInstance().imageURI.toUri()
        val progressDialog = ProgressDialog(this.requireActivity())
        progressDialog.setMessage("UPLOADING...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val formatter = SimpleDateFormat("yyyyMMDDHHmmss", Locale.getDefault())
        val now = Date()
        val imageName = binding.etCaption.text
        var filename = "${formatter.format(now)}"
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
                    var owner = Datasource.getInstance().loggedInUser
                    var visibility = "public"
                    if(binding.swPrivate.isChecked){
                        visibility = "private"
                    }
                    photoRepository.addPhotoToDb(Photos(id= filename,caption = binding.etCaption.text.toString(),url= url.toString(),email= email!!,visibility =visibility, owner = Datasource.getInstance().loggedInUser))
                    binding.etCaption.text = null
                    val action = SnipCompleteFragmentDirections.actionSnipCompleteFragmentToBodyFragment()
                    findNavController().navigate(action)
                }
            }.addOnFailureListener{
                if(progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this.requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
            }
    }

}