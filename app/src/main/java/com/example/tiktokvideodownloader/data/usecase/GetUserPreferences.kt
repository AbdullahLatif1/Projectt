package com.example.tiktokvideodownloader.data.usecase

import com.example.tiktokvideodownloader.data.local.UserPreferencesLocalSource
import com.example.tiktokvideodownloader.data.model.UserPreferences

class GetUserPreferences(private val userPreferencesLocalSource: UserPreferencesLocalSource) {

    operator fun invoke(): UserPreferences = userPreferencesLocalSource.getSync()
}