package com.example.projectSnips.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.Data.PhotoRepository
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.Data.UserRepository
import com.example.projectSnips.OnSnipClickListener
import com.example.projectSnips.R
import com.example.projectSnips.SnipAdapter
import com.example.projectSnips.databinding.FragmentBodyBinding
import com.example.projectSnips.databinding.FragmentPersonalBinding

class PersonalFragment : Fragment(), OnSnipClickListener, LifecycleOwner {

    lateinit var photoRepository: PhotoRepository
    var adapterSnips: SnipAdapter? = null
    private var _binding: FragmentPersonalBinding? = null
    private val binding get() = _binding!!


    override fun onResume() {
        super.onResume()
        photoRepository = PhotoRepository(this.requireContext())
        photoRepository.getPersonalSnips(Datasource.getInstance().loggedInUser)

        //UserRepository(requireContext()).updateLikesByOwner()

        photoRepository.allPhotos.observe(this) { photoList ->
            if (photoList != null) {
                binding.pbSpinner.visibility = View.GONE
                adapterSnips = SnipAdapter(requireContext(), photoList, this)
                //adapterSnips = view?.let { SnipAdapter(it.context, Datasource.getInstance().datalist, ) }
                binding.snipDisplay.setHasFixedSize(true)
                binding.snipDisplay.layoutManager = GridLayoutManager(activity, 3)
                binding.snipDisplay.adapter = adapterSnips
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("onCreateView", "here")
        _binding = FragmentPersonalBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.pbSpinner.visibility = View.VISIBLE
    }

    override fun onSnipClicked(snip: Photos) {

        Log.d("onClick", snip.owner)

        SnipViewFragment(snip, requireContext()).show(
            childFragmentManager, SnipViewFragment.TAG)

    }

    override fun onSnipLongClicked(snip: Photos) {
        Log.d("onLongClick", snip.owner)

        AlertDialog.Builder(requireContext())
            .setMessage("What would you like to do with this snip?")
            .setPositiveButton("Edit") { dialogInterface, i ->
                //UpdateSnipFragment(snip)
                findNavController().navigate(PersonalFragmentDirections.actionPersonalFragmentToUpdateSnipFragment(snip))
            }.create().show()
    }


}