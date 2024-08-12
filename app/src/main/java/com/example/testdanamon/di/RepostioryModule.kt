package com.example.testdanamon.di

import com.example.testdanamon.repository.PhotoRepository
import com.example.testdanamon.repository.PhotoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providePhotoRepository(photoRepository: PhotoRepositoryImpl): PhotoRepository
}