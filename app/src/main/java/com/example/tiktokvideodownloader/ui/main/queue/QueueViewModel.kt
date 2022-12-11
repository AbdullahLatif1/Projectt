package com.example.tiktokvideodownloader.ui.main.queue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tiktokvideodownloader.data.model.VideoState
import com.example.tiktokvideodownloader.data.usecase.AddVideoToQueueUseCase
import com.example.tiktokvideodownloader.data.usecase.MoveVideoInQueueUseCase
import com.example.tiktokvideodownloader.data.usecase.RemoveVideoFromQueueUseCase
import com.example.tiktokvideodownloader.data.usecase.StateOfVideosObservableUseCase
import com.example.tiktokvideodownloader.data.usecase.VideoDownloadingProcessorUseCase
import com.example.tiktokvideodownloader.ui.shared.Event
import com.example.tiktokvideodownloader.ui.shared.asLiveData

class QueueViewModel(
    stateOfVideosObservableUseCase: StateOfVideosObservableUseCase,
    private val addVideoToQueueUseCase: AddVideoToQueueUseCase,
    private val removeVideoFromQueueUseCase: RemoveVideoFromQueueUseCase,
    private val videoDownloadingProcessorUseCase: VideoDownloadingProcessorUseCase,
    private val moveVideoInQueueUseCase: MoveVideoInQueueUseCase
) : ViewModel() {

    val downloads = asLiveData(stateOfVideosObservableUseCase())
    private val _navigationEvent = MutableLiveData<Event<NavigationEvent>>()
    val navigationEvent: LiveData<Event<NavigationEvent>> = _navigationEvent

    fun onSaveClicked(url: String) {
        addVideoToQueueUseCase(url)
        videoDownloadingProcessorUseCase.fetchVideoInState()
    }

    fun onItemClicked(path: String) {
        _navigationEvent.value = Event(NavigationEvent.OpenGallery(path))
    }

    fun onUrlClicked(url: String) {
        _navigationEvent.value = Event(NavigationEvent.OpenBrowser(url))
    }

    fun onElementDeleted(videoState: VideoState) {
        removeVideoFromQueueUseCase(videoState)
    }

    fun onElementMoved(moved: VideoState, positionDifference: Int): Boolean {
        if (moved !is VideoState.InPending) return false
        moveVideoInQueueUseCase(moved.videoInPending, positionDifference)
        return true
    }

    sealed class NavigationEvent {
        data class OpenBrowser(val url: String) : NavigationEvent()
        data class OpenGallery(val uri: String) : NavigationEvent()
    }
}