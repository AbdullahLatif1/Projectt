package com.example.tiktokvideodownloader.ui.permission

import androidx.appcompat.app.AppCompatActivity
import com.example.tiktokvideodownloader.ui.permission.PermissionRequester

class PermissionRequesterAbove28 : PermissionRequester {

    override fun invoke(activity: AppCompatActivity) {
        // nothing to do, no permission is required
    }

    override fun isGranted(activity: AppCompatActivity): Boolean = true
}