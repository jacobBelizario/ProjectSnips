package com.example.projectSnips.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.R
import com.example.projectSnips.databinding.FragmentSnipBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SnipFragment : Fragment() {
    private var _binding: FragmentSnipBinding? = null
    private val binding get() = _binding!!
    private lateinit var outputDirectory: File

    //permissions
    private lateinit var imageCapture : ImageCapture
    val REQUEST_CODE_PERMISSIONS = 123
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSnipBinding.inflate(inflater, container, false)
        return binding.root


    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull().let {mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if(mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(allPermissionGranted()){
            Log.d("PERMIS","WE HAVE PERMISSIONS")
            startCamera()
        }else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),REQUIRED_PERMISSIONS,REQUEST_CODE_PERMISSIONS
            )
        }
        outputDirectory = getOutputDirectory()

        binding.btnCameraCapture.setOnClickListener {
             takePhoto()
        }
    }

    private fun takePhoto() {
        outputDirectory = getOutputDirectory()
        val imageCapture = imageCapture?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("yyyy_MM_DD_HH_mm_ss",
                Locale.getDefault()).format(System.currentTimeMillis())+".jpeg")

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOption,ContextCompat.getMainExecutor(requireContext()),
            object: ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedURI = Uri.fromFile(photoFile)
                    Datasource.getInstance().imageURI = savedURI.toString()
                    val action = SnipFragmentDirections.actionSnipFragmentToSnipCompleteFragment()
                    findNavController().navigate(action)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("E","$exception")
                }

            }
        )
    }

    override fun onResume() {
        super.onResume()
        if(allPermissionGranted()){
            Log.d("PERMIS","WE HAVE PERMISSIONS")
            startCamera()
        }else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),REQUIRED_PERMISSIONS,REQUEST_CODE_PERMISSIONS
            )
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       if(requestCode == REQUEST_CODE_PERMISSIONS) {
           if(allPermissionGranted()){

           }
       }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun allPermissionGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                this.requireContext(),it
            ) == PackageManager.PERMISSION_GRANTED
        }

    private fun startCamera(){
        // TODO: Code to open camera
        Log.d("msg", "startCamera(): Starting camera...")
        this.imageCapture = ImageCapture.Builder().build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            //bind the preview using the cameraProvider
            bindPreview(cameraProvider)
        },ContextCompat.getMainExecutor(this.requireContext()))



    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {

        val preview = Preview.Builder().build().also { preview ->
            preview.setSurfaceProvider(binding.cvPreview.surfaceProvider)
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
        }catch(ex: Exception){
            Log.e("msg","failed: $ex")
        }
    }
}