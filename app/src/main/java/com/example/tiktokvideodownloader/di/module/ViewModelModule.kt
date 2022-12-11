package com.example.tiktokvideodownloader.di.module

import androidx.lifecycle.SavedStateHandle
import com.example.tiktokvideodownloader.ui.main.MainViewModel
import com.example.tiktokvideodownloader.ui.main.queue.QueueViewModel
import com.example.tiktokvideodownloader.ui.service.QueueServiceViewModel

class ViewModelModule(private val useCaseModule: UseCaseModule) {

    val queueServiceViewModel: QueueServiceViewModel
        get() = QueueServiceViewModel(
            useCaseModule.addVideoToQueueUseCase,
            useCaseModule.videoDownloadingProcessorUseCase
        )

    fun mainViewModel(savedStateHandle: SavedStateHandle): MainViewModel =
        MainViewModel(
            useCaseModule.videoDownloadingProcessorUseCase,
            useCaseModule.addVideoToQueueUseCase,
            savedStateHandle
        )

    val queueViewModel: QueueViewModel
        get() = QueueViewModel(
            useCaseModule.stateOfVideosObservableUseCase,
            useCaseModule.addVideoToQueueUseCase,
            useCaseModule.removeVideoFromQueueUseCase,
            useCaseModule.videoDownloadingProcessorUseCase,
            useCaseModule.moveVideoInQueueUseCase
        )

 /*   val settignsViewModel: SettingsViewModel
        get() =
        SettingsViewModel(
            useCaseModule.observeUserPreferences,
            useCaseModule.setUserPreferences
        )*/
}