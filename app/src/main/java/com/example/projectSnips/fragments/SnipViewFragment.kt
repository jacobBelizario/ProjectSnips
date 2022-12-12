package com.example.projectSnips.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.OnSnipClickListener
import com.example.projectSnips.R
import com.example.projectSnips.databinding.SnipItemBinding
import com.example.projectSnips.databinding.SnipPopupBinding

class SnipViewFragment(val snip: Photos, val context1: Context) : DialogFragment() {

    class SnipPopoutViewHolder(var binding: SnipPopupBinding) : ViewHolder(binding.root) {

        fun bind(snip: Photos) : View
        {
            binding.titlePopout.text = "${ snip.email.split("@")[0] }'s ${snip.visibility} snip"
            Glide.with(binding.root).load(snip.url).into(binding.snipPopup)

            return binding.root
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("${ snip.email.split("@")[0] }'s ${snip.visibility} snip")
           // .setView(imageView)

            .setView(SnipPopoutViewHolder(SnipPopupBinding.inflate(LayoutInflater.from(context1))).bind(snip))
            .setMessage(snip.caption)
            //.setPositiveButton(getString(R.string.ok)) { _,_ -> }
            .create()

    companion object {
        const val TAG = "SnipViewDialog"
    }
}