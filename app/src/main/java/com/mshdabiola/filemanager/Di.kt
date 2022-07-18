package com.mshdabiola.filemanager

import android.app.Activity
import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//class ApplicationModule(){

//    @Provides
//    fun provideContext(
//        @ApplicationContext
//        context: Context
//    ) = context
//


//}