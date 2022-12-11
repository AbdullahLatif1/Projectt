package com.example.tiktokvideodownloader.di

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.tiktokvideodownloader.di.module.AndroidFileManagementModule
import com.example.tiktokvideodownloader.di.module.LocalSourceModule
import com.example.tiktokvideodownloader.di.module.NetworkModule
import com.example.tiktokvideodownloader.di.module.PermissionModule
import com.example.tiktokvideodownloader.di.module.UseCaseModule
import com.example.tiktokvideodownloader.di.module.ViewModelModule
import com.example.tiktokvideodownloader.ui.service.QueueServiceViewModel
import java.util.concurrent.TimeUnit

@Suppress("ObjectPropertyName", "MemberVisibilityCanBePrivate")
object ServiceLocator {

    private val DEFAULT_DELAY_BEFORE_REQUEST = TimeUnit.SECONDS.toMillis(4)
    private var _viewModelModule: ViewModelModule? = null
    private val viewModelModule: ViewModelModule
        get() = _viewModelModule ?: throw IllegalStateException("$this.start has not been called!")

    private var _permissionModule: PermissionModule? = null
    val permissionModule: PermissionModule
        get() = _permissionModule ?: throw IllegalStateException("$this.start has not been called!")

    private var _useCaseModule: UseCaseModule? = null
    val useCaseModule: UseCaseModule
        get() = _useCaseModule ?: throw IllegalStateException("$this.start has not been called!")

    fun viewModelFactory(
        savedStateRegistryOwner: SavedStateRegistryOwner,
        defaultArgs: Bundle
    ): ViewModelProvider.Factory =
        ViewModelFactory(savedStateRegistryOwner, defaultArgs, viewModelModule)

    val queueServiceViewModel: QueueServiceViewModel
        get() = viewModelModule.queueServiceViewModel

    fun start(context: Context) {
        val androidFileManagementModule = AndroidFileManagementModule(context)
        val localSourceModule = LocalSourceModule(androidFileManagementModule = androidFileManagementModule)
        val networkModule = NetworkModule(delayBeforeRequest = DEFAULT_DELAY_BEFORE_REQUEST)
        val useCaseModule = UseCaseModule(
            localSourceModule = localSourceModule,
            networkModule = networkModule
        )
        _useCaseModule = useCaseModule
        _permissionModule = PermissionModule()
        _viewModelModule = ViewModelModule(useCaseModule)
    }
}