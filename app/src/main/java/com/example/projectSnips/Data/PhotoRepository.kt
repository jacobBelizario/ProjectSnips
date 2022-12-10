package com.example.projectSnips.Data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectSnips.fragments.BodyFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.URLConnection

class PhotoRepository(val snipDisplays: MutableLiveData<ArrayList<Bitmap>>) :ViewModel() {
    val storage = Firebase.storage
    var snipList = arrayListOf<Bitmap>()


    suspend fun convertToBitmap(url: String ) : Bitmap{

        return withContext<Bitmap>(Dispatchers.IO) {
            BitmapFactory.decodeStream(URL(url).openStream()).apply {
                Log.d("coroutine", this.toString())
            }

        }
    }

    fun getLiveData(): MutableLiveData<ArrayList<Bitmap>>{
        Log.d("getLiveData", snipDisplays.value.toString())
        return snipDisplays
    }


    fun getSnipsFrom(path: String){
        val imageFolder = storage.reference.child(path)


        imageFolder.listAll().addOnCompleteListener {
            Log.d("complete", "here")

                if (it.isSuccessful) {
                        for (ref in it.result.component1()){
                            ref.downloadUrl.addOnSuccessListener {url ->
                                viewModelScope.launch(Dispatchers.IO) {
                                    snipList.add(convertToBitmap(url.toString()))
                                    return@launch
                                }.invokeOnCompletion {
                                    BodyFragment().updateUI(snipList)
                                    snipDisplays.postValue(snipList)
                                    //BodyFragment().updateUI(snipDisplays)
                                    Log.d("liveData", snipDisplays.value.toString())
                                    return@invokeOnCompletion
                                }
                            }
                        }






                    Log.d("success", "here")
                }


        }
        Log.d("return", snipList.toString())

    }
}