package com.example.tiktokvideodownloader.data.usecase

import kotlinx.coroutines.flow.Flow
import com.example.tiktokvideodownloader.data.local.UserPreferencesLocalSource
import com.example.tiktokvideodownloader.data.model.UserPreferences

class ObserveUserPreferences(private val userPreferencesLocalSource: UserPreferencesLocalSource) {

    operator fun invoke(): Flow<UserPreferences> = userPreferencesLocalSource.get()
}