package com.example.projectSnips.fragments

import android.R
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
    val sortItems = arrayListOf("Newest","Oldest","Popular","Unpopular")

    fun onUpdate(){
        photoRepository.getPersonalPublicSnips(Datasource.getInstance().loggedInUser)

        photoRepository.publicPhotos.observe(viewLifecycleOwner) { photoList ->
            if (photoList != null) {
                var list = photoList
                //Log.d("onResume", binding.spinSortPublic.selectedItem.toString()+"")
                if (binding.spinSortPublic.selectedItem!= null){
                    list = photoRepository.sortDataBySelection(photoList, binding.spinSortPublic.selectedItem.toString())
                }
                binding.pbSpinner.visibility = View.GONE
                adapterSnips = SnipAdapter(requireContext(), list, this,"personal")
                //adapterSnips = view?.let { SnipAdapter(it.context, Datasource.getInstance().datalist, ) }
                binding.snipDisplay.setHasFixedSize(true)
                binding.snipDisplay.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                binding.snipDisplay.adapter = adapterSnips
            }
        }
    }

    fun onUpdatePrivate() {
        photoRepository.getPersonalPrivateSnips(Datasource.getInstance().loggedInUser)

        photoRepository.privatePhotos.observe(viewLifecycleOwner) { photoList ->
            if (photoList != null) {
                var list = photoList
                //Log.d("onResume", binding.spinSortPrivate.selectedItem.toString()+"")
                if (binding.spinSortPrivate.selectedItem!= null){
                    list = photoRepository.sortDataBySelection(photoList, binding.spinSortPrivate.selectedItem.toString())
                }
                binding.pbSpinner.visibility = View.GONE
                adapterSnips = SnipAdapter(requireContext(), list, this,"personal")
                //adapterSnips = view?.let { SnipAdapter(it.context, Datasource.getInstance().datalist, ) }
                binding.snipDisplayPrivate.setHasFixedSize(true)
                binding.snipDisplayPrivate.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
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
        var spinnerAdapter = ArrayAdapter(requireContext().applicationContext,
            R.layout.simple_spinner_item, sortItems)
        binding.spinSortPublic.adapter = spinnerAdapter
        binding.spinSortPrivate.adapter = spinnerAdapter

        binding.spinSortPublic.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                onUpdate()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //never be unselected
            }
        }
        binding.spinSortPrivate.onItemSelectedListener  = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                onUpdatePrivate()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //never be unselected
            }
        }

    }

    override fun onSnipClicked(snip: Photos) {

        Log.d("onClick", snip.owner)

        if (snip.visibility == "private"){
            photoRepository.privatePhotos.observe(viewLifecycleOwner){ photoList ->
                var list = photoList
                //Log.d("onResume", binding.spinSortPrivate.selectedItem.toString()+"")
                if (binding.spinSortPrivate.selectedItem!= null){
                    list = photoRepository.sortDataBySelection(photoList, binding.spinSortPrivate.selectedItem.toString())
                }

                SnipViewFragment(snip, requireContext(), list).show(
                    childFragmentManager, SnipViewFragment.TAG)
            }
        }
        else{
            photoRepository.publicPhotos.observe(viewLifecycleOwner){ photoList ->
                var list = photoList
                //Log.d("onResume", binding.spinSortPublic.selectedItem.toString()+"")
                if (binding.spinSortPublic.selectedItem!= null){
                    list = photoRepository.sortDataBySelection(photoList, binding.spinSortPublic.selectedItem.toString())
                }
                SnipViewFragment(snip, requireContext(), list).show(
                    childFragmentManager, SnipViewFragment.TAG)
            }
        }
    }

    override fun onSnipLongClicked(snip: Photos) {
        Log.d("onLongClick", snip.owner)

        if (snip.email != Datasource.getInstance().email){
            AlertDialog.Builder(requireContext())
                .setMessage("Would you like to remove this snip?")
                .setPositiveButton("Yes"){ dialogInterface, i ->
                    photoRepository.deletePrivateSnip(snip)
                    onUpdatePrivate()
                }
                .setNegativeButton("Cancel"){ dialogInterface, i ->

                }
                .create().show()
        }

        else{
            AlertDialog.Builder(requireContext())
                .setMessage("What would you like to do with this snip?")
                .setPositiveButton("Edit") { dialogInterface, i ->
                    //UpdateSnipFragment(snip)

                    findNavController().navigate(PersonalFragmentDirections.actionPersonalFragmentToUpdateSnipFragment(snip))
                    dialogInterface.dismiss()
                }
                .setNegativeButton("Delete") {dialogInterface, i ->
                    dialogInterface.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setMessage("Are you sure you want to delete this snip?")
                        .setPositiveButton("Yes"){dialogInterface, i ->
                            //delete snip
                            photoRepository.deleteSnip(snip)
                            onUpdate()
                            //dialogInterface.dismiss()
                        }
                        .setNegativeButton("Cancel") {dialogInterface, i ->
                            //dialogInterface.dismiss()
                        }
                        .create().show()
                }
                .create().show()
        }


    }
}