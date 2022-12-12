package com.example.projectSnips.Data

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository (private val context: Context) {
    private val TAG = this.toString()
    private val db = Firebase.firestore
    private val COLLECTION_NAME = "users"
    private val FIELD_USER_EMAIL = "email"
    private val FIELD_PASSWORD = "password"
    private val sharedPreference =
        context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    private var editor = sharedPreference.edit()

    fun addUserToDB(newUser: User) {
        var success = false
        try {
            val data: MutableMap<String, Any> = HashMap()

            data[FIELD_USER_EMAIL] = newUser.email;
            data[FIELD_PASSWORD] = newUser.password;

            db.collection(COLLECTION_NAME).add(data).addOnSuccessListener { docRef ->

                //
                editor.putString("USER_REFERENCE", docRef.toString()).apply()

            }.addOnFailureListener {
                Log.e(TAG, "addUserToDB: ${it}")
            }

        } catch (ex: Exception) {
            Log.e(TAG, "addUserToDB: ${ex.toString()}")
        }

    }


    fun searchUserWithEmail(email : String){

        try{
            db.collection(COLLECTION_NAME)
                .whereEqualTo(FIELD_USER_EMAIL, email)
                .addSnapshotListener(EventListener { snapshot, error ->
                    if (error != null){
                        Log.e(TAG, "searchUserWithEmail: Listening to collection documents FAILED ${error}")
                        return@EventListener
                    }
                    if (snapshot != null){

                    }else{
                        Log.e(TAG, "searchUserWithEmail: No Documents received from collection")
                    }
                })

        }catch(ex: Exception){
            Log.e(TAG, "${ex}")
        }
    }
}