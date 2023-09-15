package com.example.imagesearchapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.imagesearchapp.R
import com.example.imagesearchapp.databinding.FragmentDetailsBinding

class DetailsFragment: Fragment(R.layout.fragment_details) {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    val args: DetailsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)
        val photo = args.photo

        binding.apply {
            val uri = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            Glide.with(this@DetailsFragment).load(photo.urls.full)
                .placeholder(placeHolderProgressBar(requireContext())).error(R.drawable.ic_error)
                .into(imageView)
            textViewCreator.apply {
                text = photo.user.name
                paint.isUnderlineText = true
                setOnClickListener {
                    context.startActivity(intent)
                }

            }
            if (photo.description == null) textViewDescription.isVisible = false
            textViewDescription.text = photo.description


        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun placeHolderProgressBar(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            setColorSchemeColors(ContextCompat.getColor(context, android.R.color.holo_blue_bright))

            strokeWidth = 8f
            centerRadius = 40f
            start()
        }
    }
}