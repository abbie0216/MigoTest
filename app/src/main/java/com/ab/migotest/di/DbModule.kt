package com.ab.migotest.di

import androidx.room.Room
import com.ab.migotest.App
import com.ab.migotest.BuildConfig
import com.ab.migotest.model.api.ApiRepository
import com.ab.migotest.model.db.AppDatabase
import com.ab.migotest.model.db.dao.PassDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
    ): AppDatabase {
        return Room.databaseBuilder(
            App.applicationContext(),
            AppDatabase::class.java,
            "migotest.db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePassDao(appDatabase: AppDatabase): PassDao {
        return appDatabase.passDao()
    }

}