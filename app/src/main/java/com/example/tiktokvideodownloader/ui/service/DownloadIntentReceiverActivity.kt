package com.example.tiktokvideodownloader.ui.service

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tiktokvideodownloader.data.local.VideoDownloadedLocalSource
import com.example.tiktokvideodownloader.data.usecase.GetUserPreferences
import com.example.tiktokvideodownloader.di.ServiceLocator
import com.example.tiktokvideodownloader.ui.main.MainActivity
import com.example.tiktokvideodownloader.ui.permission.PermissionRequester

class DownloadIntentReceiverActivity : AppCompatActivity() {

    private val permissionRequester: PermissionRequester by lazy {
        ServiceLocator.permissionModule.permissionRequester
    }
    private val getUserPreferences: GetUserPreferences by lazy {
        ServiceLocator.useCaseModule.getUserPreferences
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { url ->
            if (getUserPreferences().alwaysOpenApp) {
                alwaysOpen(url)
            } else {
                openOnlyIfNeeded(url)
            }
        }
        super.onCreate(savedInstanceState)
        finish()
    }

    private fun alwaysOpen(url: String) {
        startActivity(MainActivity.buildIntent(this, url))
    }

    private fun openOnlyIfNeeded(url: String) {
        if (permissionRequester.isGranted(this)) {
            startService(QueueService.buildIntent(this, url))
        } else {
            startActivity(MainActivity.buildIntent(this, url))
        }
    }
}