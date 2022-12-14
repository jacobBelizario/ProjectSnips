package com.example.projectSnips.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.databinding.SnipItemBinding
import com.example.projectSnips.interfaces.OnSnipClickListener


class SnipAdapter(private val context: Context,
                  private val dataSet: List<Photos>,
                  private val clickListener: OnSnipClickListener,
                  private val screenName:String
                  //private val dataSet: ArrayList<Int>
) : RecyclerView.Adapter<SnipAdapter.SnipViewHolder>() {

    class SnipViewHolder(var binding: SnipItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            currentSnip: Photos,
            clickListener: OnSnipClickListener,
            screenName: String
        ) {

            if(currentSnip.email == Datasource.getInstance().email && screenName == "personal") {
                binding.tvCaption.text = currentSnip.caption
                binding.tvEmail.visibility = View.GONE
                Glide.with(binding.root).load(currentSnip.url).into(binding.ivSnip)
            }else {
                binding.tvCaption.text = currentSnip.caption
                binding.tvEmail.text = currentSnip.email
                Glide.with(binding.root).load(currentSnip.url).into(binding.ivSnip)
            }


            itemView.setOnClickListener {
                //dialog
                print("dialog")
                clickListener.onSnipClicked(currentSnip)

            }

            itemView.setOnLongClickListener {
                print("LONG")
                clickListener.onSnipLongClicked(currentSnip)
                true
            }

           // itemView.setOnLongClickListener {
                //
           // }

           // itemView.setOn



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
        holder.bind(dataSet[position], clickListener,screenName)
    }

    //identifies number of items
    override fun getItemCount(): Int {
        Log.d("onItemCount", "$dataSet")
        return dataSet.size
    }


}
