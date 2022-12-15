package com.example.projectSnips.Data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PhotoRepository(val context: Context) :ViewModel() {
    private val COLLECTION_NAME = "photos"
    private val db = Firebase.firestore
    var allPhotos : MutableLiveData<List<Photos>> = MutableLiveData<List<Photos>>()
    var publicPhotos : MutableLiveData<List<Photos>> = MutableLiveData<List<Photos>>()
    var privatePhotos : MutableLiveData<List<Photos>> = MutableLiveData<List<Photos>>()

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

    fun getPersonalPublicSnips(owner: String) {
        //clear datasource whenever u run this function
        Datasource.getInstance().publicList = arrayListOf()
        //
        db.collection(COLLECTION_NAME)
            .whereEqualTo("owner", owner)
            .whereEqualTo("visibility", "public")
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    var snip = document.toObject(Photos::class.java)
                    snip.id = document.id
                    Log.d("personalSnip", snip.toString())
                    Datasource.getInstance().publicList.add(snip)
                }
                publicPhotos.postValue(Datasource.getInstance().publicList)
            }.addOnFailureListener{ exception ->
                Log.w("ERROR", "Error getting documents: ", exception)
            }
    }

    fun getPersonalPrivateSnips(owner: String) {
        //clear datasource whenever u run this function
        Datasource.getInstance().privateList = arrayListOf()
        //
        db.collection(COLLECTION_NAME)
            .whereEqualTo("owner", owner)
            .whereEqualTo("visibility", "private")
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    var snip = document.toObject(Photos::class.java)
                    snip.id = document.id
                    Log.d("personalSnip", snip.toString())
                    Datasource.getInstance().privateList.add(snip)
                }
                privatePhotos.postValue(Datasource.getInstance().privateList)
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

            db.collection(COLLECTION_NAME).document(newPhotos.id).set(data).addOnSuccessListener { docRef ->

            }.addOnFailureListener {
                Log.e("TAG", "addPhotoToDB: ${it}")
            }

        } catch (ex: Exception) {
            Log.e("TAG", "addPhotoToDB: ${ex.toString()}")
        }

    }

    //it.toCollection() //interesting (might be useful)

    fun deleteSnip(snip: Photos){
        Log.d("deleteSnip", snip.id)
        try {
            //remove instances of likes from all users
            db.collection(COLLECTION_NAME).document(snip.id).collection("likers").get().addOnSuccessListener { likers ->
                //for each liker
                likers.forEach { liker ->
                    db.collection("users").document(liker.id).collection("liked_snips").get().addOnSuccessListener{ snips ->
                        //check all liked snips in user
                        snips.forEach { snipToBeRemoved ->
                            if (snipToBeRemoved.get("id") == snip.id){
                                //remove reference
                                db.collection("users").document(liker.id).collection("liked_snips").document(snipToBeRemoved.id).delete()
                                return@forEach
                            }
                        }
                    }
                }
            }
            //delete photo from database
            db.collection(COLLECTION_NAME).document(snip.id).collection("likers")
                .get().addOnSuccessListener {
                it.documents.forEach {
                    db.collection(COLLECTION_NAME).document(snip.id).collection("likers").document(it.id).delete()
                }
            }
            db.collection(COLLECTION_NAME).document(snip.id).delete()
            var storage = Firebase.storage
            val storageRef = storage.reference
            storageRef.child("/public/${snip.id}").delete().addOnSuccessListener {
                Log.d("deleteSnip: ", "successfully deleted")
            }

        }
        catch (ex: Exception){
            Log.e("TAG", "deleteSnip: $ex")
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