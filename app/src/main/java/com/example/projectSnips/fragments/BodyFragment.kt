package com.example.projectSnips.fragments


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectSnips.Data.Datasource
import com.example.projectSnips.Data.PhotoRepository
import com.example.projectSnips.Data.Photos
import com.example.projectSnips.OnSnipClickListener
import com.example.projectSnips.SnipAdapter
import com.example.projectSnips.databinding.FragmentBodyBinding
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener

class BodyFragment : Fragment(), OnSnipClickListener, LifecycleOwner{

    lateinit var photoRepository: PhotoRepository
    var adapterSnips: SnipAdapter? = null
    private var _binding: FragmentBodyBinding? = null
    private val binding get() = _binding!!
    val sortItems = arrayListOf("Newest","Oldest","Popular","Unpopular")

    fun onUpdate() {
        photoRepository.getAllSnips()

        Log.d("CURRENT USER", Datasource.getInstance().loggedInUser)
        photoRepository.allPhotos.observe(viewLifecycleOwner) { photoList ->
            if (photoList != null) {
                var list = photoList
                Log.d("onResume", binding.spinSort.selectedItem.toString()+"")
                if (binding.spinSort.selectedItem!= null){
                    list = photoRepository.sortDataBySelection(photoList, binding.spinSort.selectedItem.toString())
                }
                binding.pbSpinner.visibility = View.GONE
                adapterSnips = SnipAdapter(requireContext(), list, this,"public")
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

        photoRepository = PhotoRepository(this.requireContext())

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.pbSpinner.visibility = View.VISIBLE
        //load spinner values
        var spinnerAdapter = ArrayAdapter(requireContext().applicationContext,android.R.layout.simple_spinner_item,sortItems)
        binding.spinSort.adapter = spinnerAdapter


        binding.update.setOnRefreshListener {
            Log.d("REFRESHING", binding.update.isRefreshing.toString())
            binding.update.isRefreshing = false
            onUpdate()
        }

        binding.spinSort.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                onUpdate()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //never be unselected
            }
        }




    }

    override fun onSnipClicked(snip: Photos) {


        Log.d("onClick", snip.owner)

        photoRepository.allPhotos.observe(viewLifecycleOwner){ photoList ->
            var list = photoList
            Log.d("onResume", binding.spinSort.selectedItem.toString()+"")
            if (binding.spinSort.selectedItem!= null){
                list = photoRepository.sortDataBySelection(list, binding.spinSort.selectedItem.toString())
            }

            SnipViewFragment(snip, requireContext(), list).show(
                childFragmentManager, SnipViewFragment.TAG)
        }



    }

    override fun onSnipLongClicked(snip: Photos) {
        Log.d("onLongClick", snip.owner)

        if (snip.owner != Datasource.getInstance().loggedInUser){
            AlertDialog.Builder(requireContext())
                .setMessage("Would you like to save this snip to My Snips?")
                .setPositiveButton("Yes") { dialogInterface, i ->
                    snip.owner = Datasource.getInstance().loggedInUser
                    snip.visibility = "private"
                    photoRepository.addPrivatePhotoToDB(snip)

                }
                .setNegativeButton("Cancel") {dialogInterface, i ->

                }
                .create().show()
        }
        else{
            AlertDialog.Builder(requireContext())
                .setMessage("This is already one of your Snips")
                .setPositiveButton("Ok") { dialogInterface, i ->

                }.create().show()
        }

    }


}