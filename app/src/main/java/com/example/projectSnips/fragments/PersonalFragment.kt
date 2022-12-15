package com.example.projectSnips.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.Data.PhotoRepository
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.OnSnipClickListener
import com.example.projectSnips.SnipAdapter
import com.example.projectSnips.databinding.FragmentPersonalBinding

class PersonalFragment : Fragment(), OnSnipClickListener, LifecycleOwner {

    lateinit var photoRepository: PhotoRepository
    var adapterSnips: SnipAdapter? = null
    private var _binding: FragmentPersonalBinding? = null
    private val binding get() = _binding!!


    override fun onResume() {
        super.onResume()
        photoRepository = PhotoRepository(this.requireContext())
        photoRepository.getPersonalPublicSnips(Datasource.getInstance().loggedInUser)
        photoRepository.getPersonalPrivateSnips(Datasource.getInstance().loggedInUser)

        photoRepository.publicPhotos.observe(this) { photoList ->
            if (photoList != null) {
                binding.pbSpinner.visibility = View.GONE
                adapterSnips = SnipAdapter(requireContext(), photoList, this)
                //adapterSnips = view?.let { SnipAdapter(it.context, Datasource.getInstance().datalist, ) }
                binding.snipDisplay.setHasFixedSize(true)
                binding.snipDisplay.layoutManager = GridLayoutManager(activity, 3)
                binding.snipDisplay.adapter = adapterSnips
            }
        }

        photoRepository.privatePhotos.observe(this) { photoList ->
            if (photoList != null) {
                binding.pbSpinner.visibility = View.GONE
                adapterSnips = SnipAdapter(requireContext(), photoList, this)
                //adapterSnips = view?.let { SnipAdapter(it.context, Datasource.getInstance().datalist, ) }
                binding.snipDisplayPrivate.setHasFixedSize(true)
                binding.snipDisplayPrivate.layoutManager = GridLayoutManager(activity, 3)
                binding.snipDisplayPrivate.adapter = adapterSnips
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


    }


}