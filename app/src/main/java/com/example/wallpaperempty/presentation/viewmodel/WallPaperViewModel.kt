package com.example.wallpaperempty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallpaperempty.utils.DispatcherProvider
import com.example.wallpaperempty.utils.Resource
import com.example.wallpaperempty.domain.repositories.PicsumRepository
import com.example.wallpaperempty.presentation.WallPaperUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallPaperViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider, private val repository: PicsumRepository
) : ViewModel() {

    private val _wallpaperList: MutableStateFlow<WallPaperUiState> =
        MutableStateFlow(WallPaperUiState.Loading)
    val wallpaperList get() = _wallpaperList.asStateFlow()

    fun fetchWallPapers() {

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            _wallpaperList.update { WallPaperUiState.Error(throwable.message.orEmpty()) }
        }
        viewModelScope.launch(dispatcher.IO + exceptionHandler) {
            repository.getPicsumImages().collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _wallpaperList.update { WallPaperUiState.Error(resource.message.orEmpty()) }
                    }

                    is Resource.Success -> {
                        if (resource.data.isNullOrEmpty().not()) {
                            _wallpaperList.update { WallPaperUiState.Success(resource.data!!) }
                        } else {
                            _wallpaperList.update { WallPaperUiState.EmptyList }
                        }
                    }
                }
            }
        }
    }
}