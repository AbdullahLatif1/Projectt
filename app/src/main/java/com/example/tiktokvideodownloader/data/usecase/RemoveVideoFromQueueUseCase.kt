package com.example.tiktokvideodownloader.data.usecase

import com.example.tiktokvideodownloader.data.local.VideoDownloadedLocalSource
import com.example.tiktokvideodownloader.data.local.VideoInPendingLocalSource
import com.example.tiktokvideodownloader.data.model.VideoState

class RemoveVideoFromQueueUseCase(
    private val videoInPendingLocalSource: VideoInPendingLocalSource,
    private val videoDownloadedLocalSource: VideoDownloadedLocalSource
) {

    operator fun invoke(videoState: VideoState) {
        when(videoState) {
            is VideoState.Downloaded -> videoDownloadedLocalSource.removeVideo(videoState.videoDownloaded)
            is VideoState.InPending -> videoInPendingLocalSource.removeVideoFromQueue(videoState.videoInPending)
            is VideoState.InProcess -> Unit
        }
    }
}