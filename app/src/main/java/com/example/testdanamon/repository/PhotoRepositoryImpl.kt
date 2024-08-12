package com.example.testdanamon.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.testdanamon.model.Photo
import com.example.testdanamon.pagingdatasource.PhotoPagingDataSource
import com.example.testdanamon.service.PhotoService
import com.example.testdanamon.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(
    private val photoService: PhotoService
) : PhotoRepository{
    override fun getPhotos(): Flow<Resource<PagingData<Photo>>> {
        return flow {
            emit(Resource.Loading()) // Emit loading state
            val pager = Pager(
                config = PagingConfig(
                    pageSize = NETWORK_PAGE_SIZE
                ),
                pagingSourceFactory = { PhotoPagingDataSource(photoService) }
            )
            val pagingDataFlow = pager.flow
            pagingDataFlow.collect { pagingData ->
                emit(Resource.Success(pagingData))
            }
        }.catch { e ->
            emit(Resource.Error(message = e.message)) // Emit error state
            val errorMessage = e.message ?: "An error occurred"
            val errorResource = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        400 -> Resource.Error(message = errorMessage, data = null, code = 400)
                        404 -> Resource.Error(message = errorMessage, code = 404)
                        500 -> Resource.Error(message = errorMessage, code = 500)
                        else -> Resource.Error(message = errorMessage)
                    }
                }
                else -> Resource.Error(message = errorMessage)
            }
            emit(errorResource)
        }
    }
    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}

