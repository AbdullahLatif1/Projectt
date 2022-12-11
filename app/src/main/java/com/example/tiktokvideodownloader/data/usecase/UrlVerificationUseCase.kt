package com.example.tiktokvideodownloader.data.usecase

class UrlVerificationUseCase {

    operator fun invoke(url: String) = url.contains("tiktok")
}