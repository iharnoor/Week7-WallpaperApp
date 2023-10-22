package com.example.wallpaperempty.presentation.ui

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.wallpaperempty.utils.DispatcherProvider
import com.example.wallpaperempty.databinding.ActivityMainBinding
import com.example.wallpaperempty.domain.entities.PicsumWallpaper
import com.example.wallpaperempty.presentation.WallPaperUiState
import com.example.wallpaperempty.presentation.adapter.WallpaperAdapter
import com.example.wallpaperempty.presentation.viewmodel.WallPaperViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val wallPaperViewModel: WallPaperViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var wallpaperAdapter: WallpaperAdapter

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()
        collectWallpapersState()
        wallPaperViewModel.fetchWallPapers()
    }

    private fun setUpViews() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }
    }

    private fun collectWallpapersState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                wallPaperViewModel.wallpaperList.collect { state ->
                    when (state) {
                        WallPaperUiState.EmptyList -> setEmptyState()
                        is WallPaperUiState.Error -> setErrorState(state.message)
                        WallPaperUiState.Loading -> setLoadingState()
                        is WallPaperUiState.Success -> populateData(state.data)
                    }
                }
            }
        }
    }

    private fun populateData(picsumWallpapers: List<PicsumWallpaper>) {
        binding.loader.isVisible = false
        wallpaperAdapter = WallpaperAdapter(picsumWallpapers, ::onItemClick)
        binding.recyclerView.adapter = wallpaperAdapter
    }

    private fun setLoadingState() {
        binding.loader.isVisible = true
    }

    private fun setErrorState(message: String) {
        binding.loader.isVisible = false
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }

    private fun setEmptyState() {
        binding.loader.isVisible = false
        Toast.makeText(this, "No Wallpapers found", Toast.LENGTH_SHORT).show()
    }

    private fun onItemClick(imageUrl: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = getBitmapFromUrl(imageUrl)
//            lifecycleScope.launch {
//                val bitmap = getBitmapFromUrl(imageUrl)
            setWallpaper(bitmap)
//            }
//            setWallpaper(bitmap)
        }
    }

    private suspend fun getBitmapFromUrl(url: String): Bitmap = withContext(dispatcherProvider.IO) {
        return@withContext try {
            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.HIGH).dontAnimate().dontTransform()
            Glide.with(this@MainActivity).asBitmap().load(url).apply(requestOptions).submit().get()
        } catch (e: Exception) {
            throw IOException("Failed to get bitmap from URL: $url", e)
        }
    }

    override fun setWallpaper(bitmap: Bitmap) {
        lifecycleScope.launch {
            withContext(dispatcherProvider.IO) {
                val wallpaperManager = WallpaperManager.getInstance(this@MainActivity)
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                    } else {
                        wallpaperManager.setBitmap(bitmap)
                    }
                    withContext(dispatcherProvider.main) {
                        Toast.makeText(
                            this@MainActivity, "Wallpaper set successfully", Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    withContext(dispatcherProvider.main) {
                        Toast.makeText(
                            this@MainActivity, "Error setting wallpaper", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

}