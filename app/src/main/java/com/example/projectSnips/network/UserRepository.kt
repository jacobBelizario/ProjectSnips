package com.example.projectSnips.network

import android.content.Context
import android.util.Log
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.Data.LikeType
import com.example.projectSnips.Data.LikedPhoto
import com.example.projectSnips.Data.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository (private val context: Context) {
    private val TAG = this.toString()
    private val db = Firebase.firestore
    private val COLLECTION_NAME = "users"
    private val FIELD_USER_EMAIL = "email"
    private val FIELD_PASSWORD = "password"
    private val FIELD_LIKED_SNIPS = "liked_snips"
    private val sharedPreference =
        context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    private var editor = sharedPreference.edit()

    fun updateLikesByOwner(){
        var data: MutableMap<String, Any> = HashMap()

        //data["id"] = Datasource.getInstance().likedPhotos
        //data["likedType"] = newUser.password;

        Log.d(TAG, "updateLikes: ${Datasource.getInstance().loggedInUser}")
        //updates whole document
        //db.collection(COLLECTION_NAME).document(Datasource.getInstance().loggedInUser).collection(FIELD_LIKED_SNIPS).add(Datasource.getInstance().likedPhotos)

        try {
            var likedPhotoListFromDB: MutableList<LikedPhoto>
            db.collection(COLLECTION_NAME).document(Datasource.getInstance().loggedInUser).collection(FIELD_LIKED_SNIPS).get().addOnSuccessListener {
                likedPhotoListFromDB = it.toObjects(LikedPhoto::class.java)

                for (index in it.documents.indices){
                    Log.d("place: ", it.documents[index].data!!["likedPhoto"].toString())
                    if (it.documents[index].data!!["likedPhoto"] == "DISLIKED"){
                        likedPhotoListFromDB[index].likeType = LikeType.DISLIKED
                    }
                }
                if (likedPhotoListFromDB != null){
                    for (likedPhoto in likedPhotoListFromDB){

                        if (Datasource.getInstance().likedPhotos.contains(likedPhoto)) { //still exists
                            Log.d(TAG, "STILL EXISTS")
                            if (Datasource.getInstance().likedPhotos[Datasource.getInstance().likedPhotos.indexOf(
                                    likedPhoto
                                )].likeType != likedPhoto.likeType) { //state changed
                                Log.d(TAG, "STATE CHANGE")
                                val newData =
                                    Datasource.getInstance().likedPhotos[Datasource.getInstance().likedPhotos.indexOf(
                                        likedPhoto
                                    )]
                                db.collection(COLLECTION_NAME)
                                    .document(Datasource.getInstance().loggedInUser)
                                    .collection(FIELD_LIKED_SNIPS).whereEqualTo("id", likedPhoto.id)
                                    .get().addOnSuccessListener { docRef ->
                                    db.collection(COLLECTION_NAME)
                                        .document(Datasource.getInstance().loggedInUser)
                                        .collection(FIELD_LIKED_SNIPS)
                                        .document(docRef.documents[0].id).set(
                                        mapOf(
                                            "id" to newData.id,
                                            "likedPhoto" to newData.likeType
                                        )
                                    )
                                }
                            }
                            else{
                                //nothing changed
                            }
                        }
                        else{ //like removed
                            Log.d(TAG, "REMOVE LIKE")
                            db.collection(COLLECTION_NAME)
                                .document(Datasource.getInstance().loggedInUser)
                                .collection(FIELD_LIKED_SNIPS).whereEqualTo("id", likedPhoto.id)
                                .get().addOnSuccessListener { docRef ->
                                    db.collection(COLLECTION_NAME)
                                        .document(Datasource.getInstance().loggedInUser)
                                        .collection(FIELD_LIKED_SNIPS)
                                        .document(docRef.documents[0].id)
                                        .delete()

                                        //DELETE FROM PHOTOS REFERENCE
                                        try {
                                            db.collection("photos").document(likedPhoto.id).collection("likers").document(
                                                Datasource.getInstance().loggedInUser).delete()
                                        }
                                        catch (ex:Exception){
                                            Log.e("deletedFromPhotosLikers", ex.toString())
                                        }
                                }


                    }

                }
                    for (likedPhoto in Datasource.getInstance().likedPhotos){
                        if (!likedPhotoListFromDB.contains(likedPhoto)){
                            Log.d(TAG, "ADD ENTRY")
                            db.collection(COLLECTION_NAME).document(Datasource.getInstance().loggedInUser).collection(FIELD_LIKED_SNIPS).add(mapOf("id" to likedPhoto.id, "likedPhoto" to likedPhoto.likeType))
                            //ADD TO PHOTOS REFERENCE
                            try {
                                db.collection("photos").document(likedPhoto.id).collection("likers").document(
                                    Datasource.getInstance().loggedInUser).set(mapOf("id" to Datasource.getInstance().loggedInUser))
                            }
                            catch (ex:Exception){
                                Log.e("addedToPhotosLikers", ex.toString())
                            }
                        }

                    }
                }
            }
        }
        catch (ex: Exception){
            Log.e(TAG, "updateLikes: $ex")
        }
    }

    fun addUserToDB(newUser: User) {
        try {
            val data: MutableMap<String, Any> = HashMap()

            data[FIELD_USER_EMAIL] = newUser.email;
            data[FIELD_PASSWORD] = newUser.password;

            db.collection(COLLECTION_NAME).add(data).addOnSuccessListener { docRef ->
                editor.putString("USER_REFERENCE", docRef.toString()).apply()
                Datasource.getInstance().loggedInUser = docRef.id
                Log.d("addUserToDB", Datasource.getInstance().loggedInUser)

            }.addOnFailureListener {
                Log.e(TAG, "addUserToDB: $it")
            }

        } catch (ex: Exception) {
            Log.e(TAG, "addUserToDB: $ex")
        }

    }

    fun pullLikedPhotos(){
        try {
            db.collection(COLLECTION_NAME).document(Datasource.getInstance().loggedInUser).collection(FIELD_LIKED_SNIPS).get().addOnSuccessListener {
                val likedPhotoListFromDB = it.toObjects(LikedPhoto::class.java)
                for (index in it.documents.indices){
                    Log.d("place: ", it.documents[index].data!!["likedPhoto"].toString())
                    if (it.documents[index].data!!["likedPhoto"] == "DISLIKED"){
                        likedPhotoListFromDB[index].likeType = LikeType.DISLIKED
                    }
                }
                if (likedPhotoListFromDB != null){
                    for (likedPhoto in likedPhotoListFromDB){
                        Datasource.getInstance().likedPhotos.add(LikedPhoto(likedPhoto.id, likedPhoto.likeType))
                    }

                }
            }
        }
        catch (ex:Exception){
            Log.e("pullPhotos", ex.toString())
        }

    }


    fun searchUserWithEmail(email : String){
        try{
            db.collection(COLLECTION_NAME)
                .whereEqualTo(FIELD_USER_EMAIL, email)
                .get()
                .addOnSuccessListener { docRef ->
                    Datasource.getInstance().loggedInUser = docRef.documents[0].id
                    pullLikedPhotos()
                }
        }
        catch(ex: Exception){
            Log.e(TAG, "$ex")
        }
    }
}