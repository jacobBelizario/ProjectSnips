package com.example.projectSnips

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.databinding.SnipItemBinding


class SnipAdapter(private val context: Context,
                  private val dataSet: ArrayList<Photos>
                  //private val dataSet: ArrayList<Int>
) : RecyclerView.Adapter<SnipAdapter.SnipViewHolder>() {

    class SnipViewHolder(var binding: SnipItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            currentSnip: Photos
        ) {

            Log.d("bind", "here")

                    binding.tvOwner.text = currentSnip.owner
                    binding.tvCaption.text = currentSnip.caption
                    Glide.with(binding.root).load(currentSnip.url).into(binding.ivSnip)

            itemView.setOnClickListener {
                //dialog
            }



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
        Log.d("onItemCount", "$dataSet")
        return dataSet.size
    }
}
