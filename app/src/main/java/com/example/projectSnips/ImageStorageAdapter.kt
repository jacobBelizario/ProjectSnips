package com.example.projectSnips

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectSnips.databinding.SnipItemBinding


class ImageStorageAdapter(private val context: Context,
                          private val dataSet: ArrayList<String>,
                          private val clickListener: OnSnipClickListener
                  //private val dataSet: ArrayList<Int>
) : RecyclerView.Adapter<ImageStorageAdapter.StorageImageViewHolder>() {

    class StorageImageViewHolder(var binding: SnipItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            currentSnip: String,
            clickListener: OnSnipClickListener
        ) {

            Log.d("bind", "here")


//                    Glide.with(binding.root).load(currentSnip.url).into(binding.ivSnip)



            itemView.setOnClickListener {
                //dialog

            }

           // itemView.setOnLongClickListener {
                //
           // }

           // itemView.setOn



        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageImageViewHolder {
        Log.d("onCreateViewHolder", "here")
        return StorageImageViewHolder(SnipItemBinding.inflate(LayoutInflater.from(context), parent, false))



    }

    //binds the data with view
    override fun onBindViewHolder(holder: StorageImageViewHolder, position: Int) {
        Log.d("onBindViewHolder", "here")
        holder.bind(dataSet[position], clickListener)
    }

    //identifies number of items
    override fun getItemCount(): Int {
        Log.d("onItemCount", "$dataSet")
        return dataSet.size
    }
}
