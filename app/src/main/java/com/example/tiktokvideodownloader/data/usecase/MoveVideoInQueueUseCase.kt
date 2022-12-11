package com.example.tiktokvideodownloader.data.usecase

import com.example.tiktokvideodownloader.data.local.VideoInPendingLocalSource
import com.example.tiktokvideodownloader.data.model.VideoInPending

class MoveVideoInQueueUseCase(
    private val videoInPendingLocalSource: VideoInPendingLocalSource
) {

    operator fun invoke(videoInPending: VideoInPending, positionDifference: Int) {
        videoInPendingLocalSource.moveBy(videoInPending, positionDifference)
    }
}