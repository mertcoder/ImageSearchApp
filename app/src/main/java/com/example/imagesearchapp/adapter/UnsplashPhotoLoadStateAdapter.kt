package com.example.imagesearchapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchapp.databinding.UnsplashPhotoLoadStateFooterBinding

class UnsplashPhotoLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<UnsplashPhotoLoadStateAdapter.LoadStateViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding  = UnsplashPhotoLoadStateFooterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoadStateViewHolder(binding)
    }


    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)

    }

    inner class LoadStateViewHolder(private val binding: UnsplashPhotoLoadStateFooterBinding): RecyclerView.ViewHolder(binding.root) {

        init{
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }


        fun bind(loadState: LoadState){
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState !is LoadState.Loading
            binding.textViewError.isVisible = loadState !is LoadState.Loading

        }

    }
}