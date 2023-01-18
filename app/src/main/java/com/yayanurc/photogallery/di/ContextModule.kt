package com.yayanurc.photogallery.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext


@InstallIn(ActivityComponent::class)
@Module
class ContextModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context
}