package com.example.tiktokvideodownloader.di.module

import com.example.tiktokvideodownloader.data.local.VideoDownloadedLocalSource
import kotlinx.coroutines.Dispatchers
import com.example.tiktokvideodownloader.data.usecase.AddVideoToQueueUseCase
import com.example.tiktokvideodownloader.data.usecase.GetUserPreferences
import com.example.tiktokvideodownloader.data.usecase.ObserveUserPreferences
import com.example.tiktokvideodownloader.data.usecase.MoveVideoInQueueUseCase
import com.example.tiktokvideodownloader.data.usecase.RemoveVideoFromQueueUseCase
import com.example.tiktokvideodownloader.data.usecase.SetUserPreferences
import com.example.tiktokvideodownloader.data.usecase.StateOfVideosObservableUseCase
import com.example.tiktokvideodownloader.data.usecase.UrlVerificationUseCase
import com.example.tiktokvideodownloader.data.usecase.VideoDownloadingProcessorUseCase
import com.example.tiktokvideodownloader.di.module.LocalSourceModule
import com.example.tiktokvideodownloader.di.module.NetworkModule

class UseCaseModule(
    private val localSourceModule: LocalSourceModule,
    private val networkModule: NetworkModule
) {

    val stateOfVideosObservableUseCase: StateOfVideosObservableUseCase
        get() = StateOfVideosObservableUseCase(
            videoInPendingLocalSource = localSourceModule.videoInPendingLocalSource,
            videoDownloadedLocalSource = localSourceModule.videoDownloadedLocalSource,
            videoInProgressLocalSource = localSourceModule.videoInProgressLocalSource,
            dispatcher = Dispatchers.IO
        )

    private val urlVerificationUseCase: UrlVerificationUseCase
        get() = UrlVerificationUseCase()

    val addVideoToQueueUseCase: AddVideoToQueueUseCase
        get() = AddVideoToQueueUseCase(
            urlVerificationUseCase,
            localSourceModule.videoInPendingLocalSource
        )

    val removeVideoFromQueueUseCase: RemoveVideoFromQueueUseCase
        get() = RemoveVideoFromQueueUseCase(
            localSourceModule.videoInPendingLocalSource,
            localSourceModule.videoDownloadedLocalSource
        )

    val moveVideoInQueueUseCase: MoveVideoInQueueUseCase
        get() = MoveVideoInQueueUseCase(
            localSourceModule.videoInPendingLocalSource
        )

    val getUserPreferences: GetUserPreferences get() = GetUserPreferences(localSourceModule.userPreferencesLocalSource)
    val observeUserPreferences: ObserveUserPreferences get() = ObserveUserPreferences(localSourceModule.userPreferencesLocalSource)
    val setUserPreferences: SetUserPreferences get() = SetUserPreferences(localSourceModule.userPreferencesLocalSource)

    val videoDSource : VideoDownloadedLocalSource get() = localSourceModule.videoDownloadedLocalSource

    val videoDownloadingProcessorUseCase: VideoDownloadingProcessorUseCase by lazy {
        VideoDownloadingProcessorUseCase(
            tikTokDownloadRemoteSource = networkModule.tikTokDownloadRemoteSource,
            videoInPendingLocalSource = localSourceModule.videoInPendingLocalSource,
            videoDownloadedLocalSource = localSourceModule.videoDownloadedLocalSource,
            videoInProgressLocalSource = localSourceModule.videoInProgressLocalSource,
            captchaTimeoutLocalSource = localSourceModule.captchaTimeoutLocalSource,
            dispatcher = Dispatchers.IO
        )
    }
}