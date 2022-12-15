package com.example.projectSnips.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.Data.PhotoRepository
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.OnSnipClickListener
import com.example.projectSnips.SnipAdapter
import com.example.projectSnips.databinding.FragmentBodyBinding
import kotlinx.coroutines.launch

class BodyFragment : Fragment(), OnSnipClickListener, LifecycleOwner{

    lateinit var photoRepository: PhotoRepository
    var adapterSnips: SnipAdapter? = null
    private var _binding: FragmentBodyBinding? = null
    private val binding get() = _binding!!


    override fun onResume() {
        super.onResume()
        photoRepository = PhotoRepository(this.requireContext())
        photoRepository.getAllSnips()

        Log.d("CURRENT USER", Datasource.getInstance().loggedInUser)

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
        _binding = FragmentBodyBinding.inflate(inflater, container, false)
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

        binding.update.setOnRefreshListener {
            Log.d("REFRESHING", binding.update.isRefreshing.toString())
            binding.update.isRefreshing = false
            onResume()
        }


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