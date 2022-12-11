package com.example.tiktokvideodownloader.data.usecase

import com.example.tiktokvideodownloader.data.local.UserPreferencesLocalSource
import com.example.tiktokvideodownloader.data.model.UserPreferences

class SetUserPreferences(private val userPreferencesLocalSource: UserPreferencesLocalSource) {

    suspend operator fun invoke(userPreferences: UserPreferences) {
        userPreferencesLocalSource.set(userPreferences)
    }
}