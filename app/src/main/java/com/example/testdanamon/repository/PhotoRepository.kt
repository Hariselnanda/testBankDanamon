package com.example.testdanamon.repository

import androidx.paging.PagingData
import com.example.testdanamon.model.Photo
import com.example.testdanamon.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getPhotos(): Flow<Resource<PagingData<Photo>>>
}