package com.example.tiktokvideodownloader.di.module

import com.example.tiktokvideodownloader.ui.permission.PermissionRationaleDialogFactory
import com.example.tiktokvideodownloader.ui.permission.PermissionRequester

class PermissionModule {

    private val permissionRationaleDialogFactory: PermissionRationaleDialogFactory
        get() = PermissionRationaleDialogFactory()

    private val permissionRequesterFactory: PermissionRequester.Factory
        get() = PermissionRequester.Factory(permissionRationaleDialogFactory)

    val permissionRequester: PermissionRequester
        get() = permissionRequesterFactory.invoke()
}