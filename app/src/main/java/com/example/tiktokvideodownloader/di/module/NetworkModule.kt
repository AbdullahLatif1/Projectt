package com.example.tiktokvideodownloader.di.module

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.example.tiktokvideodownloader.data.network.TikTokDownloadRemoteSource
import com.example.tiktokvideodownloader.data.network.TikTokRetrofitService
import com.example.tiktokvideodownloader.data.network.parsing.TikTokWebPageConverterFactory
import com.example.tiktokvideodownloader.data.network.parsing.converter.ThrowIfIsCaptchaResponse
import com.example.tiktokvideodownloader.data.network.session.CookieSavingInterceptor
import com.example.tiktokvideodownloader.data.network.session.CookieStore
import com.pierfrancescosoffritti.androidyoutubeplayer.BuildConfig
import retrofit2.Converter
import retrofit2.Retrofit

class NetworkModule(private val delayBeforeRequest: Long) {

    private val throwIfIsCaptchaResponse: ThrowIfIsCaptchaResponse
        get() = ThrowIfIsCaptchaResponse()

    private val tikTokConverterFactory: Converter.Factory
        get() = TikTokWebPageConverterFactory(throwIfIsCaptchaResponse)

    private val cookieSavingInterceptor: CookieSavingInterceptor by lazy { CookieSavingInterceptor() }

    private val cookieStore: CookieStore get() = cookieSavingInterceptor

    private val okHttpClient: OkHttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor(cookieSavingInterceptor)
            .let {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                } else {
                    it
                }
            }
            .build()

    private val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://google.com")
            .addConverterFactory(tikTokConverterFactory)
            .client(okHttpClient)
            .build()

    private val tikTokRetrofitService: TikTokRetrofitService
        get() = retrofit.create(TikTokRetrofitService::class.java)

    val tikTokDownloadRemoteSource: TikTokDownloadRemoteSource
        get() = TikTokDownloadRemoteSource(delayBeforeRequest, tikTokRetrofitService, cookieStore)
}