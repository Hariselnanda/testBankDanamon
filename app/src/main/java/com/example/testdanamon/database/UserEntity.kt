package com.example.testdanamon.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testdanamon.utils.Constant.APP_TABLE


@Entity(tableName = APP_TABLE)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int =0,
    @ColumnInfo(name = "username")
    val username : String="",
    @ColumnInfo(name = "email")
    val email: String="",
    @ColumnInfo(name = "password")
    val password :String="",
    @ColumnInfo(name = "role")
    val role :String=""
)
