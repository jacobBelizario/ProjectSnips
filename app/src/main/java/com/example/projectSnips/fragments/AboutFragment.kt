package com.example.projectSnips.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.projectSnips.Data.*
import com.example.projectSnips.databinding.AboutBinding

class AboutFragment(val aboutContext: Context):DialogFragment() {

    class AboutPopOut(var binding: AboutBinding) : RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("ClickableViewAccessibility")
        fun bind() : View
        {
            return binding.root
        }
    }



    @SuppressLint("DialogFragmentCallbacksDetector")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setView(AboutPopOut(AboutBinding.inflate(LayoutInflater.from(aboutContext))).bind())
            .setOnCancelListener {
                Log.d(SnipViewFragment.TAG, "OnDismiss: here")
            }
            .create()

    companion object {
        const val TAG = "AboutDialog"
    }
}