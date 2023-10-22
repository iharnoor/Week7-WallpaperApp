package com.example.wallpaperempty.presentation

import com.example.wallpaperempty.domain.entities.PicsumWallpaper

sealed class WallPaperUiState{
    object Loading : WallPaperUiState()
    object EmptyList : WallPaperUiState()
    data class Success(val data: List<PicsumWallpaper>) : WallPaperUiState()
    data class Error(val message:String) : WallPaperUiState()

}
