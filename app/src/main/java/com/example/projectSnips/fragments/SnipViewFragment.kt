package com.example.projectSnips.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.projectSnips.Data.*
import com.example.projectSnips.OnSnipClickListener
import com.example.projectSnips.R
import com.example.projectSnips.databinding.SnipItemBinding
import com.example.projectSnips.databinding.SnipPopupBinding

class SnipViewFragment(val snip: Photos, val context1: Context) : DialogFragment() {

    class SnipPopoutViewHolder(var binding: SnipPopupBinding) : ViewHolder(binding.root) {

        fun bind(snip: Photos) : View
        {
            binding.titlePopout.text = "${ snip.email.split("@")[0] }'s ${snip.visibility} snip"
            binding.captionPopout.text = snip.caption
            binding.likeCounter.text = snip.likes.toString()
            Log.d("bind", Datasource.getInstance().likedPhotos.toString())
            Log.d("bind", snip.id)
            if (Datasource.getInstance().likedPhotos.contains(LikedPhoto(snip.id, LikeType.LIKED))){
                binding.likePopout.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24)
            }
            else if (Datasource.getInstance().likedPhotos.contains(LikedPhoto(snip.id, LikeType.DISLIKED))){
                binding.dislikePopout.setImageResource(R.drawable.ic_baseline_thumb_down_alt_24)
            }
            //if (snip.)
            binding.likePopout.setOnClickListener{
                if (!Datasource.getInstance().likedPhotos.contains(LikedPhoto(snip.id, LikeType.LIKED))) { //already like pressed
                    if (Datasource.getInstance().likedPhotos.contains(LikedPhoto(snip.id, LikeType.DISLIKED))){
                        Datasource.getInstance().likedPhotos.removeAt(Datasource.getInstance().likedPhotos.indexOf(
                            LikedPhoto(snip.id, LikeType.DISLIKED)
                        ))
                        snip.likes += 1
                    }
                    snip.likes += 1
                    binding.dislikePopout.setImageResource(R.drawable.ic_baseline_thumb_down_off_alt_24)
                    binding.likePopout.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24)
                    binding.likeCounter.text = snip.likes.toString()
                    Datasource.getInstance().likedPhotos.add(LikedPhoto(snip.id, LikeType.LIKED))
                }
                else{
                    snip.likes -= 1
                    binding.likePopout.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
                    binding.likeCounter.text = snip.likes.toString()
                    Datasource.getInstance().likedPhotos.removeAt(Datasource.getInstance().likedPhotos.indexOf(
                        LikedPhoto(snip.id, LikeType.LIKED)
                    ))
                }
                Log.d("ONLIKE", Datasource.getInstance().likedPhotos.toString())
            }
            binding.dislikePopout.setOnClickListener{

               if (!Datasource.getInstance().likedPhotos.contains(LikedPhoto(snip.id, LikeType.DISLIKED))) { //already dislike pressed
                   if (Datasource.getInstance().likedPhotos.contains(LikedPhoto(snip.id, LikeType.LIKED))){
                       Datasource.getInstance().likedPhotos.removeAt(Datasource.getInstance().likedPhotos.indexOf(
                           LikedPhoto(snip.id, LikeType.LIKED)
                       ))
                       snip.likes -= 1
                   }
                   snip.likes -= 1
                   binding.likePopout.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
                   binding.dislikePopout.setImageResource(R.drawable.ic_baseline_thumb_down_alt_24)
                   binding.likeCounter.text = snip.likes.toString()
                   Datasource.getInstance().likedPhotos.add(LikedPhoto(snip.id, LikeType.DISLIKED))
                }
               else{
                   snip.likes += 1
                   binding.dislikePopout.setImageResource(R.drawable.ic_baseline_thumb_down_off_alt_24)
                   binding.likeCounter.text = snip.likes.toString()
                   Datasource.getInstance().likedPhotos.removeAt(Datasource.getInstance().likedPhotos.indexOf(
                       LikedPhoto(snip.id, LikeType.DISLIKED)
                   ))
               }
                Log.d("ONDISLIKE", Datasource.getInstance().likedPhotos.toString())
            }
            Glide.with(binding.root).load(snip.url).into(binding.snipPopup)
            Datasource.getInstance().datalist[Datasource.getInstance().datalist.indexOf(snip)].likes = snip.likes

            return binding.root
        }
    }

    @SuppressLint("DialogFragmentCallbacksDetector")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setView(SnipPopoutViewHolder(SnipPopupBinding.inflate(LayoutInflater.from(context1))).bind(snip))
            .setOnCancelListener {
                Log.d(TAG, "OnDismiss: here")
                onCancel(it)
            }
            .create()

    override fun onCancel(dialog: DialogInterface) {
        UserRepository(requireContext()).updateLikesByOwner()
        PhotoRepository(requireContext()).updateLikesInDB(snip)
        super.onCancel(dialog)
    }

    companion object {
        const val TAG = "SnipViewDialog"
    }
}