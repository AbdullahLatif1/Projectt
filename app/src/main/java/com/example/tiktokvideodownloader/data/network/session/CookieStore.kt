package com.example.tiktokvideodownloader.data.network.session

interface CookieStore {

    var cookie: String?

    fun clear()
}