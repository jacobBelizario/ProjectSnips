package com.example.projectSnips.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.projectSnips.Data.*
import com.example.projectSnips.R
import com.example.projectSnips.databinding.SnipPopupBinding
import com.github.satoshun.coroutine.autodispose.view.autoDisposeScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.sql.Time
import java.time.Instant
import java.time.LocalTime

class SnipViewFragment(val snip: Photos, val context1: Context, val list: List<Photos>) : DialogFragment()  {


    //lateinit var motion:GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //motion = GestureDetectorCompat(context1, SnipGestureListener())
    }


    class SnipPopoutViewHolder(var binding: SnipPopupBinding) : ViewHolder(binding.root) {


        @SuppressLint("ClickableViewAccessibility")
        fun bind(startSnip: Photos, list: List<Photos>) : View
        {
            var snip = startSnip
            Log.d("ummmmmm", list.toString())

            var startPos:Float = 0f
            var endPos:Float
            binding.root.setOnTouchListener { view, motionEvent ->
                //startPos = motionEvent.rawX

                if (motionEvent.action == MotionEvent.ACTION_DOWN){
                    Log.d("start", (startPos).toString())
                    startPos = motionEvent.rawX
                }
                if (motionEvent.action == MotionEvent.ACTION_UP){
                    endPos = motionEvent.rawX
                    Log.d("end", (endPos).toString())
                    if (list.last() != snip){
                        binding.nextSnip.visibility = View.VISIBLE
                        if ((endPos-startPos) < -200) {
                            Log.d("swipe", (endPos-startPos).toString())
                            val newSnip = list[list.indexOf(snip)+1]
                            binding.likePopout.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
                            binding.dislikePopout.setImageResource(R.drawable.ic_baseline_thumb_down_off_alt_24)
                            actuallyBind(newSnip, list)
                            snip = newSnip
                        }
                    }
                    else{
                        binding.nextSnip.visibility = View.INVISIBLE
                    }

                    if (list.first() != snip){
                        binding.lastSnip.visibility = View.VISIBLE
                        if ((endPos-startPos) > 200) {
                            val newSnip = list[list.indexOf(snip)-1]
                            binding.likePopout.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
                            binding.dislikePopout.setImageResource(R.drawable.ic_baseline_thumb_down_off_alt_24)
                            actuallyBind(newSnip, list)
                            snip = newSnip
                        }
                    }
                    else{
                        binding.lastSnip.visibility = View.INVISIBLE
                    }
                    startPos = 0f
                }
                //val startPos = motionEvent.rawX
                //binding.root.autoDisposeScope.launch {
                    //delay(25)
                    //val endPos = motionEvent.rawX
                    //Log.d("swipe", "${endPos - startPos}")
                    //if ((endPos - startPos) > 0){ //swipe left (go next/ up in list)
                        //if (snip != Datasource.getInstance().datalist.last()){
                           // Log.d("swipe", "here")
                            //swap bindings
                           // val newSnip = Datasource.getInstance().datalist[Datasource.getInstance().datalist.indexOf(snip)+1]
                            //Glide.with(binding.root).load(newSnip.url).into(binding.snipPopup)
                            //bind(newSnip)
                            //return@launch
                       // }
                    //}
                    //else if((endPos - startPos) < 0) //swipe right (go back/ down in list)
                    //    if (snip != Datasource.getInstance().datalist.first()){
                     //       Log.d("swipe", "here")
                     //   }
                   // true
                //}

                true
            }

            println("END")
            actuallyBind(snip, list)
            return binding.root
        }

        fun actuallyBind(snip: Photos, list: List<Photos>){


            if (list.last() != snip){
                binding.nextSnip.visibility = View.VISIBLE
                binding.nextSnip.setOnClickListener {
                    val newSnip = list[list.indexOf(snip)+1]
                    binding.likePopout.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
                    binding.dislikePopout.setImageResource(R.drawable.ic_baseline_thumb_down_off_alt_24)
                    actuallyBind(newSnip, list)
                }
            }
            else{
                binding.nextSnip.visibility = View.INVISIBLE
            }

            if (list.first() != snip){
                binding.lastSnip.visibility = View.VISIBLE
                binding.lastSnip.setOnClickListener {
                    val newSnip = list[list.indexOf(snip)-1]
                    binding.likePopout.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
                    binding.dislikePopout.setImageResource(R.drawable.ic_baseline_thumb_down_off_alt_24)
                    actuallyBind(newSnip, list)
                }
            }
            else{
                binding.lastSnip.visibility = View.INVISIBLE
            }

            if(snip.visibility.equals("private")){
                binding.titlePopout.text = "${ snip.email.split("@")[0] }'s snip"
                binding.captionPopout.text = snip.caption
                Glide.with(binding.root).load(snip.url).into(binding.snipPopup)
                binding.likePopout.visibility = View.GONE
                binding.dislikePopout.visibility = View.GONE
                binding.likeCounter.visibility = View.GONE

            }else{

                binding.titlePopout.text = "${ snip.email.split("@")[0] }'s snip"
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
            }
        }

    }

    @SuppressLint("DialogFragmentCallbacksDetector")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setView(SnipPopoutViewHolder(SnipPopupBinding.inflate(LayoutInflater.from(context1))).bind(snip, list))
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