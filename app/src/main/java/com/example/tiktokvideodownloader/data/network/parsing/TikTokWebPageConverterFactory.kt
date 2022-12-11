package com.example.tiktokvideodownloader.data.network.parsing

import okhttp3.ResponseBody
import com.example.tiktokvideodownloader.data.network.parsing.converter.ActualVideoPageUrlConverter
import com.example.tiktokvideodownloader.data.network.parsing.converter.ThrowIfIsCaptchaResponse
import com.example.tiktokvideodownloader.data.network.parsing.converter.VideoFileUrlConverter
import com.example.tiktokvideodownloader.data.network.parsing.converter.VideoResponseConverter
import com.example.tiktokvideodownloader.data.network.parsing.response.ActualVideoPageUrl
import com.example.tiktokvideodownloader.data.network.parsing.response.VideoFileUrl
import com.example.tiktokvideodownloader.data.network.parsing.response.VideoResponse
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class TikTokWebPageConverterFactory(private val throwIfIsCaptchaResponse: ThrowIfIsCaptchaResponse) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? =
        when (type) {
            ActualVideoPageUrl::class.java -> ActualVideoPageUrlConverter(throwIfIsCaptchaResponse)
            VideoFileUrl::class.java -> VideoFileUrlConverter(throwIfIsCaptchaResponse)
            VideoResponse::class.java -> VideoResponseConverter()
            else -> super.responseBodyConverter(type, annotations, retrofit)
        }
}