package com.example.projectSnips.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.Data.PhotoRepository
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.databinding.FragmentStorageBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*


open class StorageFragment : Fragment() {
    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!
    private val pickImage = 100
    lateinit var imageUri: Uri
    lateinit var photoRepository: PhotoRepository
    lateinit var sharedPrefs : SharedPreferences
    val REQUEST_CODE_PERMISSIONS = 123
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
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

        if(allPermissionGranted()){

            photoRepository = PhotoRepository(this.requireContext())
            binding.ivSelectedImg.setOnClickListener {
                viewGalery()
            }
            binding.btnSaveSnip.setOnClickListener {
                saveToCloud()
            }
        }else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),REQUIRED_PERMISSIONS,REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onResume() {
        super.onResume()
        binding.ivSelectedImg.setOnClickListener {
            viewGalery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data!!
            Log.d("URI","$imageUri")
            binding.ivSelectedImg.setImageURI(imageUri)
            binding.ivSelectedImg.setBackgroundColor(Color.parseColor("#171717"))
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
    fun allPermissionGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                this.requireContext(),it
            ) == PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            if(allPermissionGranted()){
                viewGalery()
                Snackbar.make(binding.flStorage, "Permissions granted storage is now enabled", Snackbar.LENGTH_SHORT).show()
            }else {
                binding.ivSelectedImg.visibility = View.GONE
                binding.btnSaveSnip.setOnClickListener {
                    Snackbar.make(binding.flStorage, "Permissions not granted", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    open fun saveToCloud() {
        if(binding.etCaption.text.isEmpty()){
            binding.etCaption.error = "Caption cannot be empty"
        }
        if( binding.ivSelectedImg.drawable == null){
            Snackbar.make(binding.flStorage, "Must select an image", Snackbar.LENGTH_SHORT).show()
        } else {
            upload()
        }
    }

    open fun upload(){
        val progressDialog = ProgressDialog(this.requireActivity())
        progressDialog.setMessage("UPLOADING...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val formatter = SimpleDateFormat("yyyyMMDDHHmmss", Locale.getDefault())
        val now = Date()
        var filename = "${formatter.format(now)}"
        val storageReference = FirebaseStorage.getInstance().getReference("/public/$filename")
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                binding.ivSelectedImg.setImageURI(null)
                if(progressDialog.isShowing) progressDialog.dismiss()
                var storage = Firebase.storage
                val storageRef = storage.reference
                storageRef.child("/public/$filename").downloadUrl.addOnSuccessListener {
                    url ->
                    sharedPrefs = this.requireActivity().getSharedPreferences("com_example_projectSnips",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    var email: String? = sharedPrefs.getString("KEY_LOGGEDIN_EMAIL","")
                    var visibility = "public"
                    if(binding.swPrivate.isChecked){
                        visibility = "private"
                    }
                    photoRepository.addPhotoToDb(Photos(id = filename, caption = binding.etCaption.text.toString(),url= url.toString(),email= email!!,visibility =visibility, owner = Datasource.getInstance().loggedInUser))
                    binding.etCaption.text = null

                    val action = StorageFragmentDirections.actionStorageFragmentToBodyFragment()
                    findNavController().navigate(action)
                }
            }.addOnFailureListener{
                if(progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this.requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
            }
    }

}