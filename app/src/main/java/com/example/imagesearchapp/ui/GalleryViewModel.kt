package com.example.imagesearchapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.imagesearchapp.api.UnsplashResponse
import com.example.imagesearchapp.data.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: UnsplashRepository,
    state: SavedStateHandle
)  : ViewModel(){
    private val currenctQuery = state.getLiveData(CURRENT_QUERY,DEFAULT_QUERY)
    val photos = currenctQuery.switchMap { queryString->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)

    }

    fun searchPhotos(query: String){
        currenctQuery.postValue(query)

    }

    companion object{
        private const val CURRENT_QUERY =""
        private const val DEFAULT_QUERY = "cats"
    }


}