package com.example.projectSnips

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.projectSnips.databinding.SnipItemBinding
import com.google.firebase.storage.StorageReference
import java.net.URL


class SnipAdapter(private val context: Context,
                  private val dataSet: ArrayList<Bitmap>
) : RecyclerView.Adapter<SnipAdapter.SnipViewHolder>() {

    class SnipViewHolder(var binding: SnipItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentSnip: Bitmap) {
            //associate individual view and data
            Log.d("bind", "here")

                //Log.d("bind", "$it")
                //viewModelScope.launch {  }//remake thread & repository
                //try {
                    binding.snipImageview.setImageBitmap(currentSnip)
                //}
                //catch (ex: Exception){
                //    Log.e("bind", "$ex")
                //}

                //binding.snipImageview.setImageURI(it)


            //use this approach to use the object and perform the data here in the bind() function
            itemView.setOnClickListener {
//                Log.e("FruitViewHolder", "bind: ${currentFruit.title} selected", )
            }

            //use this approach to send the object to MainActivity and perform the operation there
            //           itemView.setOnClickListener {
            //               clickListener.onSnipClicked(currentSnip)
            //          }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnipViewHolder {
        Log.d("onCreateViewHolder", "here")
        return SnipViewHolder(SnipItemBinding.inflate(LayoutInflater.from(context), parent, false))

    }

    //binds the data with view
    override fun onBindViewHolder(holder: SnipViewHolder, position: Int) {
        Log.d("onBindViewHolder", "here")
        holder.bind(dataSet[position])
    }

    //identifies number of items
    override fun getItemCount(): Int {
        return dataSet.size
    }
}
