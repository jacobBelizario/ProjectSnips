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
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.URLConnection

class PhotoRepository(val context: Context) :ViewModel() {
    private val COLLECTION_NAME = "photos"
    private val db = Firebase.firestore
    var allPhotos : MutableLiveData<List<Photos>> = MutableLiveData<List<Photos>>()

    fun getAllSnips() {
        //clear datasource whenever u run this function
        Datasource.getInstance().datalist = arrayListOf()
        //
        db.collection(COLLECTION_NAME).get()
            .addOnSuccessListener { documents ->
            for(document in documents) {
                var snip = document.toObject(Photos::class.java)
                Datasource.getInstance().datalist.add(snip)
                allPhotos.postValue(Datasource.getInstance().datalist)
            }
        }.addOnFailureListener{ exception ->
            Log.w("ERROR", "Error getting documents: ", exception)
        }
    }

}