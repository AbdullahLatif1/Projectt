package com.example.tiktokvideodownloader.di

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.tiktokvideodownloader.di.module.ViewModelModule
import com.example.tiktokvideodownloader.ui.main.MainViewModel
import com.example.tiktokvideodownloader.ui.main.queue.QueueViewModel

class ViewModelFactory(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    defaultArgs: Bundle,
    private val viewModelModule: ViewModelModule,
) : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> viewModelModule.mainViewModel(handle)
            QueueViewModel::class.java -> viewModelModule.queueViewModel
            //SettingsViewModel::class.java -> viewModelModule.settignsViewModel
            else -> throw IllegalArgumentException("Can't create viewModel for $modelClass ")
        }
        return viewModel as T
    }
}