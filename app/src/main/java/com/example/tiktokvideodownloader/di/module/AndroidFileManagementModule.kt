package com.example.tiktokvideodownloader.di.module

import android.content.ContentResolver
import android.content.Context
import com.example.tiktokvideodownloader.data.local.persistent.SharedPreferencesManager
import com.example.tiktokvideodownloader.data.local.persistent.SharedPreferencesManagerImpl
import com.example.tiktokvideodownloader.data.local.persistent.UserPreferencesStorage
import com.example.tiktokvideodownloader.data.local.save.video.SaveVideoFile
import com.example.tiktokvideodownloader.data.local.verify.exists.VerifyFileForUriExists
import com.example.tiktokvideodownloader.data.local.verify.exists.VerifyFileForUriExistsImpl

class AndroidFileManagementModule(private val context: Context) {
    private val contentResolver: ContentResolver
        get() = context.contentResolver

    val verifyFileForUriExists: VerifyFileForUriExists
        get() = VerifyFileForUriExistsImpl(contentResolver)

    private val sharedPreferencesManagerImpl by lazy {
        SharedPreferencesManagerImpl.create(context)
    }

    val sharedPreferencesManager: SharedPreferencesManager get() = sharedPreferencesManagerImpl

    val userPreferencesStorage: UserPreferencesStorage get() = sharedPreferencesManagerImpl

    private val saveVideoFileFactory: SaveVideoFile.Factory
        get() = SaveVideoFile.Factory(contentResolver)

    val saveVideoFile: SaveVideoFile
        get() = saveVideoFileFactory.create()
}