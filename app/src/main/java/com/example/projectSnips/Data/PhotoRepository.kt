package com.example.projectSnips.Data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
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
                snip.id = document.id
                Datasource.getInstance().datalist.add(snip)
            }
                allPhotos.postValue(Datasource.getInstance().datalist)
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
                    snip.id = document.id
                    Log.d("personalSnip", snip.toString())
                    Datasource.getInstance().datalist.add(snip)
                }
                allPhotos.postValue(Datasource.getInstance().datalist)
            }.addOnFailureListener{ exception ->
                Log.w("ERROR", "Error getting documents: ", exception)
            }
    }

    fun updateLikesInDB(photos: Photos){
        var data: MutableMap<String, Any> = HashMap()

        data["caption"] = photos.caption
        data["url"] = photos.url
        data["email"] = photos.email
        data["likes"] = photos.likes
        data["visibility"] = photos.visibility
        data["owner"] = photos.owner

        try {
            db.collection(COLLECTION_NAME).document(photos.id).set(data)
        }
        catch (ex: Exception){
            Log.e("TAG", "addLikes: $ex")
        }

    }

    fun addPhotoToDb(newPhotos: Photos) {
        try {
            val data: MutableMap<String, Any> = HashMap()

            data["caption"] = newPhotos.caption;
            data["url"] = newPhotos.url;
            data["email"] = newPhotos.email;
            data["likes"] = newPhotos.likes
            data["visibility"] = newPhotos.visibility;
            data["owner"] = newPhotos.owner

            db.collection(COLLECTION_NAME).add(data).addOnSuccessListener { docRef ->

            }.addOnFailureListener {
                Log.e("TAG", "addPhotoToDB: ${it}")
            }

        } catch (ex: Exception) {
            Log.e("TAG", "addPhotoToDB: ${ex.toString()}")
        }

    }

    fun updateSnip(snip: Photos){
        val data: MutableMap<String, Any> = HashMap()
        data["caption"] = snip.caption;
        data["url"] = snip.url;
        data["email"] = snip.email;
        data["likes"] = snip.likes
        data["visibility"] = snip.visibility;
        data["owner"] = snip.owner

        try {
            db.collection(COLLECTION_NAME).document(snip.id).set(data)
        }
        catch (ex: Exception){
            Log.e("TAG", "updateSnip: $ex")
        }
    }

}