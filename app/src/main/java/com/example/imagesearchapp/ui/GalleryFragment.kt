package com.example.imagesearchapp.ui

import android.location.GnssAntennaInfo.Listener
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.imagesearchapp.R
import com.example.imagesearchapp.adapter.UnsplashPhotoAdapter
import com.example.imagesearchapp.adapter.UnsplashPhotoLoadStateAdapter
import com.example.imagesearchapp.data.UnsplashPhoto
import com.example.imagesearchapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment: Fragment(R.layout.fragment_gallery), UnsplashPhotoAdapter.OnClickListener  {

    private val viewModel by viewModels<GalleryViewModel>()
    private var _binding: FragmentGalleryBinding?=null
    private val binding get() = _binding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)
        val adapter = UnsplashPhotoAdapter(this)
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter{ adapter.retry()},
                footer = UnsplashPhotoLoadStateAdapter{adapter.retry() },

            )
            buttonRetry.setOnClickListener{
                adapter.retry()
            }
        }

        viewModel.photos.observe(viewLifecycleOwner, Observer {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)

        })
        val myToolbar = binding.myToolbar as Toolbar
        val activity = requireActivity()
        if (activity is AppCompatActivity) {
            activity.setSupportActionBar(myToolbar) // toolbar yerine kullanmak istediğiniz ActionBar nesnesini belirtin
        }

        adapter.addLoadStateListener {loadState->
            binding.apply{
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                recyclerView.isVisible = loadState.source.refresh !is LoadState.Error

                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error
                if(loadState.source.refresh is LoadState.NotLoading && adapter.itemCount==0){
                    textViewEmpty.isVisible = true
                    recyclerView.isVisible = false
                }else{
                    textViewEmpty.isVisible = false
                }
            }



        }


        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query!=null){
                    binding.recyclerView.scrollToPosition(0)
                      viewModel.searchPhotos(query)

                }

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })


    }


    override fun click(photo: UnsplashPhoto) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}