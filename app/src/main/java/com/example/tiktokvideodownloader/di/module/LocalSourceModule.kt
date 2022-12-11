package com.example.tiktokvideodownloader.di.module

import com.example.tiktokvideodownloader.data.local.CaptchaTimeoutLocalSource
import com.example.tiktokvideodownloader.data.local.UserPreferencesLocalSource
import com.example.tiktokvideodownloader.data.local.VideoDownloadedLocalSource
import com.example.tiktokvideodownloader.data.local.VideoInPendingLocalSource
import com.example.tiktokvideodownloader.data.local.VideoInProgressLocalSource
import java.util.concurrent.TimeUnit

class LocalSourceModule(private val androidFileManagementModule: AndroidFileManagementModule) {

    val videoDownloadedLocalSource: VideoDownloadedLocalSource by lazy {
        VideoDownloadedLocalSource(
            saveVideoFile = androidFileManagementModule.saveVideoFile,
            sharedPreferencesManagerImpl = androidFileManagementModule.sharedPreferencesManager,
            verifyFileForUriExists = androidFileManagementModule.verifyFileForUriExists
        )
    }

    val videoInPendingLocalSource: VideoInPendingLocalSource by lazy {
        VideoInPendingLocalSource(sharedPreferencesManager = androidFileManagementModule.sharedPreferencesManager)
    }

    val videoInProgressLocalSource: VideoInProgressLocalSource by lazy { VideoInProgressLocalSource() }

    val userPreferencesLocalSource: UserPreferencesLocalSource by lazy{ UserPreferencesLocalSource(androidFileManagementModule.userPreferencesStorage) }

    val captchaTimeoutLocalSource: CaptchaTimeoutLocalSource
        get() = CaptchaTimeoutLocalSource(
            androidFileManagementModule.sharedPreferencesManager,
            DEFAULT_CAPTCHA_TIMEOUT
        )

    companion object {
        private val DEFAULT_CAPTCHA_TIMEOUT = TimeUnit.MINUTES.toMillis(10)
    }
}