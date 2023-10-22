package com.example.wallpaperempty.utils

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val main:CoroutineDispatcher
    val IO : CoroutineDispatcher
    val default: CoroutineDispatcher
}