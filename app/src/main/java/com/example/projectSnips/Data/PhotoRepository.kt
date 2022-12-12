package com.example.projectSnips.Data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PhotoRepository(val context: Context) :ViewModel() {
    private val COLLECTION_NAME = "photos"
    private val db = Firebase.firestore
    var allPhotos : MutableLiveData<List<Photos>> = MutableLiveData<List<Photos>>()

    fun getAllSnips() {
        //clear datasource whenever u run this function
        Datasource.getInstance().datalist = arrayListOf()
        //
        db.collection(COLLECTION_NAME)
            .whereEqualTo("visibility", "public")
            .get()
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

    fun getPersonalSnips(owner: String) {
        //clear datasource whenever u run this function
        Datasource.getInstance().datalist = arrayListOf()
        //
        db.collection(COLLECTION_NAME)
            .whereEqualTo("owner", owner)
            .get()
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