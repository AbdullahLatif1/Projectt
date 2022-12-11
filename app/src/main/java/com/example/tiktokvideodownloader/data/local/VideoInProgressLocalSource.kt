package com.example.tiktokvideodownloader.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.tiktokvideodownloader.data.model.VideoInPending
import com.example.tiktokvideodownloader.data.model.VideoInProgress

class VideoInProgressLocalSource {

    private val _videoInProcessFlow = MutableStateFlow<VideoInProgress?>(null)
    val videoInProcessFlow: Flow<VideoInProgress?> = _videoInProcessFlow

    fun markVideoAsInProgress(videoInPending: VideoInPending) {
        _videoInProcessFlow.value = VideoInProgress(id = videoInPending.id, url = videoInPending.url)
    }

    fun removeVideoAsInProgress(videoInPending: VideoInPending) {
        if (_videoInProcessFlow.value?.id == videoInPending.id) {
            _videoInProcessFlow.value = null
        }
    }
}