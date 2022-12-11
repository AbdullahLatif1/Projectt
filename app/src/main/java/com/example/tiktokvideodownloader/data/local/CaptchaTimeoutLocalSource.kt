package com.example.tiktokvideodownloader.data.local

import com.example.tiktokvideodownloader.data.local.persistent.SharedPreferencesManager

class CaptchaTimeoutLocalSource(
    private val appSharedPreferencesManager: SharedPreferencesManager,
    private val timeOutInMillis : Long
) {

    fun isInCaptchaTimeout(): Boolean =
        System.currentTimeMillis() < appSharedPreferencesManager.captchaTimeoutUntil

    fun onCaptchaResponseReceived() {
        appSharedPreferencesManager.captchaTimeoutUntil = System.currentTimeMillis() + timeOutInMillis
    }
}