package com.yayanurc.photogallery.di

import com.yayanurc.photogallery.data.local.SharedPrefDataSource
import com.yayanurc.photogallery.data.local.SharedPrefLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped


@Module
@InstallIn(ActivityComponent::class)
abstract class SharedPrefModule {

    @ActivityScoped
    @Binds
    abstract fun bindSharedPref(impl: SharedPrefLocalDataSource): SharedPrefDataSource
}