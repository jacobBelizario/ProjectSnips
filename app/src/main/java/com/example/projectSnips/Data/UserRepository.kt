package com.example.projectSnips.Data

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap

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
        try {
            val data: MutableMap<String, Any> = HashMap()

            data[FIELD_USER_EMAIL] = newUser.email;
            data[FIELD_PASSWORD] = newUser.password;

            db.collection(COLLECTION_NAME).add(data).addOnSuccessListener { docRef ->
                Log.d("ADD", "addUserToDB: Document added with ID ${docRef.id}")
                editor.putString("USER_DOC_ID", docRef.id)
                editor.commit()

            }.addOnFailureListener {
                Log.e(TAG, "addUserToDB: ${it}")
            }

        } catch (ex: Exception) {
            Log.e(TAG, "addUserToDB: ${ex.toString()}")
        }
    }
}