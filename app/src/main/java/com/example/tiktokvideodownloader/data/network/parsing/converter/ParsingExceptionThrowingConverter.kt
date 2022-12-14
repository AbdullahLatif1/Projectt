package com.example.tiktokvideodownloader.data.network.parsing.converter

import okhttp3.ResponseBody
import com.example.tiktokvideodownloader.data.network.exceptions.CaptchaRequiredException
import com.example.tiktokvideodownloader.data.network.exceptions.ParsingException
import retrofit2.Converter

abstract class ParsingExceptionThrowingConverter<T> : Converter<ResponseBody, T> {

    @Throws(ParsingException::class, CaptchaRequiredException::class)
    final override fun convert(value: ResponseBody): T? =
        try {
            convertSafely(value)
        } catch (captchaRequiredException: CaptchaRequiredException) {
            throw captchaRequiredException
        } catch (throwable: Throwable) {
            throw ParsingException(cause = throwable)
        }

    abstract fun convertSafely(responseBody: ResponseBody): T?
}